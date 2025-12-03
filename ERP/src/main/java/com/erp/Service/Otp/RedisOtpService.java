package com.erp.Service.Otp;

import com.erp.Exception.Otp.InvalidOtpException;
import com.erp.Exception.Otp.OTPErrorException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Meta.MetaAdminRepository;
import com.erp.Meta.MetaUserRepository;
import com.erp.Model.Admin;
import com.erp.Model.GenericUser;
import com.erp.Model.OtpVerification;
import com.erp.Model.User;
import com.erp.Multitenancy.TenantContext;
import com.erp.Repository.Admin.AdminUserRepository;
import com.erp.Repository.Admin.AdminUserRepositoryImpl;
import com.erp.Repository.User.UserRepository;
import com.erp.Repository.User.UserRepositoryCustom;
import com.erp.Repository.User.UserRepositoryImpl;
import com.erp.Security.util.UserIdentity;
import com.erp.Security.util.UserRepositoryRegistry;
import com.erp.Service.Reset.ServiceEmail;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RedisOtpService {

    private ValueOperations<String, String> valueOps;
    private ValueOperations<String, String> verifiedOtp;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ServiceEmail serviceEmail;
    @Autowired
    private UserRepositoryRegistry userRepositoryRegistry;
    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MetaAdminRepository metaAdminRepository;
    @Autowired
    private MetaUserRepository metaUserRepository;
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;
    @Autowired
    private AdminUserRepositoryImpl adminUserRepositoryImpl;
    @Autowired
    private UserIdentity userIdentity;


    @PostConstruct
    public void init() {
        valueOps = redisTemplate.opsForValue();
        verifiedOtp = redisTemplate.opsForValue(); // for Boolean you can convert manually
    }

    public String sendOtp() {
        String email = userIdentity.getCurrentUserEmail();
        return sendOtp(email);
    }

    public String sendOtp(String email) {

        String schema = "public";
        TenantContext.setCurrentTenant(schema);

        if (metaAdminRepository.existsByAdminEmail(email)) {
            schema = metaAdminRepository.findSchemaNameByAdminEmail(email).get();
            TenantContext.setCurrentTenant(schema);
        } else {
            if (metaUserRepository.existsByUserEmail(email)) {
                schema = metaUserRepository.findSchemaNameByUserEmail(email).get();
                TenantContext.setCurrentTenant(schema);
            } else {
                throw new UserNotFoundException("User Not Found For Email : " + email);
            }
        }

        Optional<GenericUser> user = userRepositoryRegistry.findUserByEmail(email);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User Not Found With Email : " + email);
        }

        String otp = generateOtp();
        storeOtp(email, otp);

        serviceEmail.sendOtpEmail(email, otp);

        return "Otp Send On Email : " + email;
    }

    public void storeOtp(String userIdentifier, String otp) {
        String key = buildKey(userIdentifier);
        valueOps.set(key, otp, 10, TimeUnit.MINUTES);
        setVerified(userIdentifier, false);
    }

    public String verify(String email, String otp) {
        boolean verified = verifyOtp(email, otp);

        if (!verified) {
            throw new InvalidOtpException("Invalid OTP!!");
        }
        setVerified(email, true);

        return "OTP Verified For Email : " + email;
    }

    public boolean verifyOtp(String userIdentifier, String enteredOtp) {
        String key = buildKey(userIdentifier);
        String cachedOtp = valueOps.get(key);

        if (cachedOtp == null) {
            return false;  // expired or never stored
        }
        return cachedOtp.equals(enteredOtp);
    }

    private String buildKey(String id) {
        return "OTP:" + id;
    }

    private String generateOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    public String resetPassword(String email, String newPassword, String confirmPassword) {

        String otpKey = buildKey(email);

        // Check both OTP and verification exist
        if (!redisTemplate.hasKey(otpKey) || !Boolean.parseBoolean(verifiedOtp.get(email))) {
            throw new OTPErrorException("OTP Not Verified or expired!");
        }

        // Check password match first
        if (!newPassword.equals(confirmPassword)) {
            throw new OTPErrorException("New password and Confirm password do not match!");
        }

        String schema = "public";
        TenantContext.setCurrentTenant(schema);
        boolean adminExists = metaAdminRepository.existsByAdminEmail(email);

        if (adminExists) {
            schema = metaAdminRepository.findSchemaNameByAdminEmail(email).get();
            TenantContext.setCurrentTenant(schema);
            Admin admin = adminUserRepositoryImpl.findByEmailWithSchema(email, schema)
                    .orElse(null);

            if (admin != null) {
                admin.setPassword(passwordEncoder.encode(newPassword));
                adminUserRepository.save(admin);
                return "Password reset successfully!";
            }
        }


        boolean userExists = metaUserRepository.existsByUserEmail(email);

        if (!userExists) {
            throw new UserNotFoundException("User Not Found With Email : " + email);
        }

        schema = metaUserRepository.findSchemaNameByUserEmail(email).get();
        TenantContext.setCurrentTenant(schema);
        User user = userRepositoryImpl.findByEmailWithSchema(email, schema).orElse(null);

        if (user == null) {
            throw new UserNotFoundException("User Not Found With Email : " + email);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        deleteOtp(email);
        return "Password reset successfully!";
    }

    private void setVerified(String email, boolean status) {
        verifiedOtp.set(email, Boolean.toString(status), 10, TimeUnit.MINUTES);
    }

    private boolean isVerified(String email) {
        String val = verifiedOtp.get(email);
        return val != null && Boolean.parseBoolean(val);
    }

    private void deleteOtp(String email) {
        String otpKey = buildKey(email);
        redisTemplate.delete(otpKey);
        redisTemplate.delete(email);
    }

    public String verifyReset(String otp) {
        String email = userIdentity.getCurrentUserEmail();

        return verify(email, otp);
    }

    public String resetPasswordReset(String newPassword, String confirmPassword) {
        String email = userIdentity.getCurrentUserEmail();

        return resetPassword(email, newPassword, confirmPassword);
    }
}
