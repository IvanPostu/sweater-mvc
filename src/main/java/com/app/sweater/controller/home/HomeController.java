package com.app.sweater.controller.home;

import com.app.sweater.domain.Message;
import com.app.sweater.domain.User;
import com.app.sweater.persistence.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/")
public class HomeController {

  private static final String VIEW = "view/home/index";

  @Value("${upload.path}")
  private String uploadPath;

  @Autowired
  MessageRepository messageRepository;

  @GetMapping(value = {"", "home"})
  public String home(
      @RequestParam(required = false, defaultValue = "") String filter,
      Model model){

    if(StringUtils.isEmpty(filter)){
      Iterable<Message> messages = messageRepository.findAll();
      model.addAttribute("messages",messages);
    }else{
      List<Message> messages = messageRepository.findByTag(filter);
      model.addAttribute("messages",messages);
      model.addAttribute("filter", filter);
    }



    return VIEW;
  }

  @PostMapping("home")
  public String addMessage(
      @AuthenticationPrincipal User user,
      @RequestParam String text,
      @RequestParam String tag,
      @RequestParam("file") MultipartFile file,
      Model model) throws IOException {

    Message message = new Message(text, tag, user);

    if (file != null && !file.getOriginalFilename().isEmpty()) {
      File uploadDir = new File(uploadPath);

      if (!uploadDir.exists()) {
        uploadDir.mkdir();
      }

      String uuidFile = UUID.randomUUID().toString();
      String resultFilename = uuidFile + "." + file.getOriginalFilename();

      file.transferTo(new File(uploadPath + "/" + resultFilename));

      message.setFilename(resultFilename);
    }

    messageRepository.save(message);

    Iterable<Message> messages = messageRepository.findAll();
    model.addAttribute("messages",messages);

    return VIEW;
  }



}
