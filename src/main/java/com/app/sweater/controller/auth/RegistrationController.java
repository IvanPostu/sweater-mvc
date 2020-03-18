package com.app.sweater.controller.auth;


import com.app.sweater.controller.ControllerUtils;
import com.app.sweater.domain.User;
import com.app.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.function.Supplier;

@Controller
public class RegistrationController {

//  private static final String REGISTRATION = "view/auth/registration/index";

  @Autowired
  private UserService userService;

  @GetMapping("/registration")
  public String registration() {
    return "view/auth/registration/index";
  }

  @PostMapping(value = "/registration")
  public String addUser(
      @Valid User user,
      BindingResult bindingResult,
      Model model) {

    Supplier<Boolean> inputDataIsValid = () -> {


      /*
       * If Null return false.
       * */
      if(bindingResult.hasErrors()){
        Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
        model.mergeAttributes(errors);
        return false;
      }


      /*
      * No null check.
      * */
      if(!user.getPassword().equals(user.getPassword2())){
        model.addAttribute("passwordError", "Passwords are different.");
        return false;
      }

      /*
       * If not added successfully, return false.
       * */
      if (!userService.addUser(user)) {
        model.addAttribute("usernameError", "User exists!");
        return false;
      }


      return true;
    };


    if(inputDataIsValid.get()){
      return "redirect:/login";
    }else{
      return "view/auth/registration/index";
    }

  }

  @RequestMapping(method = RequestMethod.GET, value = "/activate/{code}")
  public String activate(Model model, @PathVariable String code) {

    boolean isActivated = userService.activateUser(code);

    if (isActivated) {
      model.addAttribute("activationAccountMessage", "User successfully activated");
    }

    return "view/auth/login/index";
  }


}
