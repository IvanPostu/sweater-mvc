package com.app.sweater.application.controller.home;


import com.app.sweater.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/user-messages")
public class UserMessageController {


  @GetMapping(value = "/{username}")
  public String getUserMessages(
      @AuthenticationPrincipal User currentUser,
      @PathVariable(required = false, value = "username") String user
  ){

//    Iterable<Message> messages = messageRepository.findAll();

    return "view/home/userMessages/index";
  }






}
