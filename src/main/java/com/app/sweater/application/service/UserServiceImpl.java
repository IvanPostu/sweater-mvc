package com.app.sweater.application.service;

import com.app.sweater.application.service.exceptions.UserNotActivatedException;
import com.app.sweater.domain.Role;
import com.app.sweater.domain.User;
import com.app.sweater.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MailSender mailSender;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Value("${hostname}")
  private String hostname;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetails result =  userRepository.findByUsername(username);


    return result;
  }

  public boolean addUser(User user) {

    if (userRepository.findByUsername(user.getUsername()) != null) {
      return false;
    }

    user.setActive(false);
    user.setRoles(Collections.singleton(Role.USER));
    user.setActivationCode(UUID.randomUUID().toString());
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    userRepository.save(user);

    sendMessage(user);


    return true;
  }

  private void sendMessage(User user) {
    if (!StringUtils.isEmpty(user.getEmail())) {
      String message = String.format("Hello, %s! " +
              "Welcome to Sweater." +
              "Please, visit next link: <a href=http://%s/activate/%s>Link</a>",
          user.getUsername(),
          hostname,
          user.getActivationCode()
      );

      mailSender.send(user.getEmail(), "Activation code", message);
    }
  }


  public String activateUserAndReturnUsername(String code) throws UserNotActivatedException {

    User user = userRepository.findByActivationCode(code);

    if (user == null) {
      throw new UserNotActivatedException();
    }

    user.setActivationCode(null);
    user.setActive(true);
    userRepository.save(user);

    return user.getUsername();
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public void saveUser(User user, String username, Map<String, String> form) {
    user.setUsername(username);

    Set<String> roles = Arrays.stream(Role.values())
        .map(item -> item.name())
        .collect(Collectors.toSet());

    user.getRoles().clear();

    for (String key : form.keySet()) {
      if (roles.contains(key)) {
        user.getRoles().add(Role.valueOf(key));
      }
    }

    userRepository.save(user);

  }

  public void updateProfile(User user, String password, String email) {
    final String userEmail = user.getEmail();

    boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
        (userEmail != null && !userEmail.equals(email));
    if (isEmailChanged) {
      user.setEmail(email);

      if (!StringUtils.isEmpty(email)) {
        user.setActivationCode(UUID.randomUUID().toString());
      }

    }

    if (!StringUtils.isEmpty(password)) {
      user.setPassword(password);
    }

    userRepository.save(user);

    if (isEmailChanged) {
      sendMessage(user);
    }
  }

  public void subscribe(User currentUser, User user) {
    user.getSubscribers().add(currentUser);

    userRepository.save(user);
  }

  public void unsubscribe(User currentUser, User user) {
    user.getSubscribers().remove(currentUser);

    userRepository.save(user);
  }

}
