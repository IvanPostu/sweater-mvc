package com.app.sweater.application.service;

import com.app.sweater.application.service.exceptions.UserNotActivatedException;
import com.app.sweater.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {


  boolean addUser(User user);

  String activateUserAndReturnUsername(String code) throws UserNotActivatedException;

  List<User> findAll();

  User findByUsername(String username);

  void saveUser(User user, String username, Map<String, String> form);

  void updateProfile(User user, String password, String email);

  void subscribe(User currentUser, User user);

  void unsubscribe(User currentUser, User user);

}
