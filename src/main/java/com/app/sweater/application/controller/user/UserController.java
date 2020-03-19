package com.app.sweater.application.controller.user;

import com.app.sweater.domain.Role;
import com.app.sweater.domain.User;
import com.app.sweater.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/user")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public String userList(Model model) {
    List<User> users = userService.findAll();
    model.addAttribute("users", users);

    return "userList";
  }

  @GetMapping("{user}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public String userEditForm(@PathVariable User user, Model model) {
    model.addAttribute("user", user);
    model.addAttribute("allRoles", Role.values());

    return "userEdit";
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public String userSave(
      @RequestParam String username,
      @RequestParam Map<String, String> form,
      @RequestParam("userId") User user
  ) {
    userService.saveUser(user, username, form);

    return "redirect:/user";
  }


  @GetMapping(value = "/profile")
  public String getProfile(Model model, @AuthenticationPrincipal User user) {
    model.addAttribute("username", user.getUsername());
    model.addAttribute("email", user.getEmail());

    return "view/user/profile/index";
  }

  @PostMapping(value = "/profile")
  public String updateProfile(
      @AuthenticationPrincipal User user,
      @RequestParam String password,
      @RequestParam String email
  ){

    userService.updateProfile(user, password, email);


    return "redirect:/user/profile";
  }


}
