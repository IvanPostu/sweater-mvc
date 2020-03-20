package com.app.sweater.application.service;

import com.app.sweater.application.service.exceptions.UserNotActivatedException;
import com.app.sweater.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService extends UserDetailsService {


  boolean addUser(User user);

  String activateUserAndReturnUsername(String code) throws UserNotActivatedException;

  List<User> findAll();

  void saveUser(User user, String username, Map<String, String> form);

  void updateProfile(User user, String password, String email);

}
