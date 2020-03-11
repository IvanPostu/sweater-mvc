package com.app.sweater.controller;

import com.app.sweater.domain.Role;
import com.app.sweater.domain.User;
import com.app.sweater.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @GetMapping
  public String userList(Model model){
    List<User> users = userRepository.findAll();
    model.addAttribute("users", users);

    return "userList";
  }

  @GetMapping("{user}")
  public String userEditForm(@PathVariable User user, Model model){
    model.addAttribute("user", user);
    model.addAttribute("allRoles", Role.values());

    return "userEdit";
  }

  @PostMapping
  public String userSave(
      @RequestParam String username,
      @RequestParam Map<String, String> form,
      @RequestParam("userId") User user
  ){
    user.setUsername(username);

    Set<String> roles = Arrays.stream(Role.values())
        .map(item -> item.name())
        .collect(Collectors.toSet());

    user.getRoles().clear();

    for (String key : form.keySet()) {
      if(roles.contains(key)){
        user.getRoles().add(Role.valueOf(key));
      }
    }

    userRepository.save(user);


    return "redirect:/user";
  }


}
