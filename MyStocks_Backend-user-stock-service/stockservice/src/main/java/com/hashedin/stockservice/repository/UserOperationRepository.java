package com.hashedin.stockservice.repository;

import com.hashedin.stockservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOperationRepository extends JpaRepository<User, Long> {
    public User findByUserId(Long userId);
    public User findUserByUsername(String username);
}
