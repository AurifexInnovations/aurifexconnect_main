package com.erp.Repository.Otp;


import com.erp.Model.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp,Long> {

    Optional<UserOtp> findByMobileNo(String mobileNo);

}
