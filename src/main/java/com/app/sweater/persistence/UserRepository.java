package com.app.sweater.persistence;

import com.app.sweater.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);

  User findByActivationCode(String code);
}
