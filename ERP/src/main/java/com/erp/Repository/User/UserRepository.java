package com.erp.Repository.User;


import com.erp.Model.User;
import com.erp.Repository.GenericUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long>, GenericUserRepository<User> ,UserRepositoryCustom{
    Optional<User> findByEmail(String email);

    List<User> findByIsActiveTrue();

    Optional<User> findByIdOrFirstNameAndIsActiveTrue(Long id, String name);
}
