package com.app.sweater.controller.auth;


import com.app.sweater.domain.User;
import com.app.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

  private static final String REGISTRATION = "view/auth/registration/index";

  @Autowired
  private UserService userService;

  @GetMapping("/registration")
  public String registration() {
    return REGISTRATION;
  }

  @PostMapping(value = "/registration")
  public String addUser(User user, String firstname, String lastname, Model model) {

    if (!userService.addUser(user)) {
      model.addAttribute("message", "User exists!");
      return REGISTRATION;
    }



    return "redirect:/login";
  }

  @RequestMapping(method = RequestMethod.GET, value = "/activate/{code}")
  public String activate(Model model, @PathVariable String code){

    boolean isActivated = userService.activateUser(code);

    if(isActivated){
      model.addAttribute("activationAccountMessage", "User successfully activated");
    }

    return "view/auth/login/index";
  }


}
