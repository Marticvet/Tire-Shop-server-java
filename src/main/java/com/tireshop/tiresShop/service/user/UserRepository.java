package com.tireshop.tiresShop.service.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tireshop.tiresShop.service.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
