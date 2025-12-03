package com.erp.Service.Reset;

import com.erp.Model.OtpVerification;
import com.erp.Model.User;
import com.erp.Repository.Otp.OtpRepository;
import com.erp.Repository.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private ServiceEmail serviceemail;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // STEP 1 - SEND OTP IF USER EXISTS
    public String sendOtp(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User not found! Redirect to Sign Up.";
        }

        String otp = String.format("%06d", new Random().nextInt(999999));

        OtpVerification otpData = new OtpVerification();
        otpData.setEmail(email);
        otpData.setOtp(otp);
        otpData.setCreatedAt(LocalDateTime.now());
        otpData.setVerified(false);

        otpRepository.save(otpData);

        serviceemail.sendOtpEmail(email, otp);

        return "OTP sent to email.";
    }

    // STEP 2 - VERIFY OTP WITH DB
    public String verifyOtp(String email, String otp) {

        OtpVerification dbOtp = otpRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElse(null);

        if (dbOtp == null) return "No OTP found.";

        if (dbOtp.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5)))
            return "OTP expired.";

        if (!dbOtp.getOtp().equals(otp))
            return "Invalid OTP.";

        dbOtp.setVerified(true);
        otpRepository.save(dbOtp);

        return "OTP verified successfully!";
    }

    // STEP 3 - RESET PASSWORD IF OTP VERIFIED
    public String resetPassword(String email, String newPassword, String confirmPassword) {

        // Check password match first
        if (!newPassword.equals(confirmPassword)) {
            return "New password and Confirm password do not match!";
        }

        OtpVerification dbOtp = otpRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElse(null);

        if (dbOtp == null || !dbOtp.isVerified()) {
            return "OTP not verified!";
        }

        User user = userRepository.findByEmail(email).get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Password reset successfully!";
    }
}
