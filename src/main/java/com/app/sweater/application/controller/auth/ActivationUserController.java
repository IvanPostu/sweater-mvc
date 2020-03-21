package com.app.sweater.application.controller.auth;


import com.app.sweater.application.service.UserService;
import com.app.sweater.application.service.exceptions.UserNotActivatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ActivationUserController {

  @Autowired
  private UserService userService;

  @RequestMapping(method = RequestMethod.GET, value = "/activate/{code}")
  public String activate(
      @PathVariable("code") final String code,
      final RedirectAttributes redirectAttributes
  ) {


    try{
      String username = userService.activateUserAndReturnUsername(code);
      redirectAttributes.addFlashAttribute("activationAccountUsername", username);
      redirectAttributes.addFlashAttribute("activationAccountMessage", "Account has been activated.");
      redirectAttributes.addFlashAttribute("activationAccountMessageType", "success");
    }catch(UserNotActivatedException e){
      redirectAttributes.addFlashAttribute("activationAccountMessage", "Activation link is not valid.");
      redirectAttributes.addFlashAttribute("activationAccountMessageType", "danger");
    }


    return "redirect:/login";
  }


}
