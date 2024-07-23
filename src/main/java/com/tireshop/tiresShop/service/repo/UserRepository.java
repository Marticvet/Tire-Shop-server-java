package com.tireshop.tiresShop.service.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tireshop.tiresShop.service.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);

    Boolean existsByUsername(String username);
}
