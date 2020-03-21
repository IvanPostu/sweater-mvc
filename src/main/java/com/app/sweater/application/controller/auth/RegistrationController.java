package com.app.sweater.application.controller.auth;


import com.app.sweater.application.controller.ControllerUtils;
import com.app.sweater.application.service.UserService;
import com.app.sweater.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

@Controller
public class RegistrationController {


  @Autowired
  private UserService userService;


  @GetMapping("/registration")
  public String registration(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {

    return "view/auth/registration/index";
  }


  @PostMapping(value = "/registration")
  public String addUser(
      final RedirectAttributes redirectAttributes,
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam("password2") String passwordConfirmation,
      @RequestParam("captcha") String captchaCode,
      @Valid User user,
      BindingResult bindingResult,
      Model model) {

    HttpSession session = request.getSession();

    Supplier<Boolean> inputDataIsValid = () -> {

      boolean result = true;
      final String captchaErrorMsg = "Captcha code is not valid.";

      if (session.getAttribute("captcha") == null) {
        model.addAttribute("captchaError", captchaErrorMsg);
        result = false;
      } else {
        String captchaCodeFromSession = (String) session.getAttribute("captcha");
        if (StringUtils.isEmpty(captchaCode) || StringUtils.isEmpty(captchaCodeFromSession)) {
          model.addAttribute("captchaError", captchaErrorMsg);
          result = false;
        } else {
          if (!captchaCode.equals(captchaCodeFromSession)) {
            model.addAttribute("captchaError", captchaErrorMsg);
            result = false;
          }
        }

      }

      if (bindingResult.hasErrors()) {
        Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
        model.mergeAttributes(errors);
        result = false;
      }

      if (StringUtils.isEmpty(passwordConfirmation)) {
        model.addAttribute("password2Error", "Password confirmation cannot be empty.");
        result = false;
      }


      if (!user.getPassword().equals(passwordConfirmation)) {
        model.addAttribute("passwordError", "Passwords are different.");
        result = false;
      }

      if (!result) return false;


      if (!userService.addUser(user)) {
        model.addAttribute("usernameError", "User exists!");
        result = false;
      }


      return result;
    };


    if (inputDataIsValid.get()) {
      redirectAttributes.addFlashAttribute("activationAccountMessage", "Follow the link indicated in the letter in your mail");
      redirectAttributes.addFlashAttribute("activationAccountMessageType", "warning");
      return "redirect:/login";
    } else {
      return "view/auth/registration/index";
    }

  }



}
