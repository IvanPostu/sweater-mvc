package com.app.sweater;

import com.app.sweater.domain.Message;
import com.app.sweater.persistence.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {

  @Autowired
  MessageRepository messageRepository;

  @GetMapping("/")
  public String home(Model model){

    Iterable<Message> messages = messageRepository.findAll();
    model.addAttribute("messages",messages);

    return "index";
  }

  @PostMapping
  public String add(@RequestParam String text, @RequestParam String tag, Model model){

    Message message = new Message(text, tag);
    messageRepository.save(message);

    Iterable<Message> messages = messageRepository.findAll();
    model.addAttribute("messages",messages);

    return "index";
  }

  @PostMapping("filter")
  public String filter(@RequestParam String filter, Model model){



    List<Message> messages = messageRepository.findByTag(filter);
    model.addAttribute("messages",messages);

    return "index";
  }

}
