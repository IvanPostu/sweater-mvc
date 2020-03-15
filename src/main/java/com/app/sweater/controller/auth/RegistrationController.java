//package com.app.sweater.controller.auth;
//
//
//import com.app.sweater.domain.Role;
//import com.app.sweater.domain.User;
//import com.app.sweater.persistence.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.Collections;
//
//@Controller
//public class RegistrationController {
//
//  private static final String LOGIN = "/login";
//  private static final String REGISTRATION = "temp/auth/registration";
//
//  @Autowired
//  private UserRepository userRepository;
//
//  @GetMapping("/registration")
//  public String registration() {
//    return REGISTRATION;
//  }
//
//  @PostMapping("/registration")
//  public String addUser(@RequestParam String username, @RequestParam String password, Model model) {
//    User userFromDb = userRepository.findByUsername(username);
//
//    if (userFromDb != null) {
//      model.addAttribute("message", "User exists!");
//      return REGISTRATION;
//    }
//
//    User user = new User();
//    user.setUsername(username);
//    user.setPassword(password);
//    user.setActive(true);
//    user.setRoles(Collections.singleton(Role.USER));
//    userRepository.save(user);
//
//    return "redirect:" + LOGIN;
//  }
//}
