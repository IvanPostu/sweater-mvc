package com.app.sweater.application.controller.home;


import com.app.sweater.application.service.MessageService;
import com.app.sweater.domain.Message;
import com.app.sweater.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/user-messages")
public class UserMessageController {

  @Autowired
  private MessageService messageService;

  @GetMapping(value = "/{username}")
  public String getUserMessages(
      @AuthenticationPrincipal User currentUser,
      @PathVariable(required = false, value = "username") String username,
      Model model
  ){

    if(!StringUtils.isEmpty(username)){
      List<Message> messages = messageService.findAllFromSpecificAuthor(username);
      model.addAttribute("messages", messages);

    } else {
      return "redirect:/home";
    }


    return "view/user/messages/index";
  }






}
