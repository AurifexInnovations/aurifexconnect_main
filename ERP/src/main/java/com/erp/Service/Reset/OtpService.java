package com.erp.Service.Reset;

import com.erp.Exception.Otp.InvalidOtpException;
import com.erp.Exception.Otp.OTPErrorException;
import com.erp.Exception.Otp.OtpExpiredException;
import com.erp.Exception.Otp.OtpNotFoundException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Meta.MetaAdminRepository;
import com.erp.Meta.MetaUser;
import com.erp.Meta.MetaUserRepository;
import com.erp.Model.Admin;
import com.erp.Model.GenericUser;
import com.erp.Model.OtpVerification;
import com.erp.Model.User;
import com.erp.Multitenancy.TenantContext;
import com.erp.Repository.Admin.AdminUserRepository;
import com.erp.Repository.Otp.OtpRepository;
import com.erp.Repository.User.UserRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Security.util.UserRepositoryRegistry;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class OtpService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final ServiceEmail serviceemail;
    private final PasswordEncoder passwordEncoder;
    private final UserIdentity userIdentity;
    private final AdminUserRepository adminUserRepository;
    private final MetaAdminRepository metaAdminRepository;
    private final MetaUserRepository metaUserRepository;
    private final UserRepositoryRegistry userRepositoryRegistry;

    // STEP 1 - SEND OTP IF USER EXISTS
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
                throw new UserNotFoundException("User Not Found For Email : "+email);
            }
        }

        Optional<GenericUser> user = userRepositoryRegistry.findUserByEmail(email);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User Not Found With Email : " + email);
        }

        String otp = String.format("%06d", new Random().nextInt(999999));

        OtpVerification otpData = new OtpVerification();
        otpData.setEmail(email);
        otpData.setOtp(otp);
        otpData.setCreatedAt(LocalDateTime.now());
        otpData.setVerified(false);

        TenantContext.setCurrentTenant(schema);
        otpRepository.save(otpData);

        serviceemail.sendOtpEmail(email, otp);

        return "OTP sent to email : " + email;
    }

    // STEP 2 - VERIFY OTP WITH DB
    public String verifyOtp(String email, String otp) {

        OtpVerification dbOtp = otpRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElse(null);

        if (dbOtp == null) {
            throw new OtpNotFoundException("No OTP found.");
        }

        if (dbOtp.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new OtpExpiredException("OTP expired.");
        }

        if (!dbOtp.getOtp().equals(otp)) {
            throw new InvalidOtpException("Invalid OTP.");
        }

        dbOtp.setVerified(true);
        otpRepository.save(dbOtp);

        return "OTP verified successfully!";
    }

    // STEP 3 - RESET PASSWORD IF OTP VERIFIED
    public String resetPassword(String email, String newPassword, String confirmPassword) {

        // Check password match first
        if (!newPassword.equals(confirmPassword)) {
            throw new OTPErrorException("New password and Confirm password do not match!");
        }

        OtpVerification dbOtp = otpRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElse(null);

        if (dbOtp == null) {
            throw new OTPErrorException("OTP Not Found!!");
        }
        if (!dbOtp.isVerified()) {
            throw new OTPErrorException("OTP Not Verified!!");
        }

        Admin admin = adminUserRepository.findByEmail(email)
                .orElse(null);

        if (admin != null) {
            admin.setPassword(passwordEncoder.encode(newPassword));
            adminUserRepository.save(admin);
            return "Password reset successfully!";
        }

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            throw new UserNotFoundException("User Not Found With Email : " + email);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "Password reset successfully!";
    }
}
