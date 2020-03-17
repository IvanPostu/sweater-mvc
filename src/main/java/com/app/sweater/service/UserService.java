package com.app.sweater.service;

import com.app.sweater.domain.Role;
import com.app.sweater.domain.User;
import com.app.sweater.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  MailSender mailSender;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username);
  }

  public boolean addUser(User user){

    if(userRepository.findByUsername(user.getUsername())!=null){
      return false;
    }

    user.setActive(false);
    user.setRoles(Collections.singleton(Role.USER));
    user.setActivationCode(UUID.randomUUID().toString());

    userRepository.save(user);

    if(!StringUtils.isEmpty(user.getEmail())){
      String message = String.format("Hello, %s! \n" +
          "Welcome to Sweater.\n" +
              "Please, visit next link: <a href=http://127.0.0.1:8080/activate/%s>Link</a>",
          user.getUsername(),
          user.getActivationCode()
      );

      mailSender.send(user.getEmail(), "Activation code", message);
    }


    return true;
  }


  public boolean activateUser(String code) {

    User user = userRepository.findByActivationCode(code);

    if(user == null){
      return false;
    }

    user.setActivationCode(null);
    user.setActive(true);
    userRepository.save(user);


    return true;
  }
}
