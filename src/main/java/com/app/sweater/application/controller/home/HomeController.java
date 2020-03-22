package com.app.sweater.application.controller.home;

import com.app.sweater.application.service.MessageService;
import com.app.sweater.domain.Message;
import com.app.sweater.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.app.sweater.application.controller.ControllerUtils.getErrors;

@Controller
public class HomeController {


  @Value("${upload.path}")
  private String uploadPath;

  @Autowired
  MessageService messageService;

  @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
  public String home(
      @RequestParam(required = false, defaultValue = "") String filter,
      Model model){

    if(StringUtils.isEmpty(filter)){
      List<Message> messages = messageService.findAll();
      model.addAttribute("messages", messages);
    }else{
      List<Message> messages = messageService.findByTag(filter);
      model.addAttribute("messages",messages);
      model.addAttribute("filter", filter);
    }



    return "view/home/main/index";
  }

  @RequestMapping(value = {"/home"}, method = RequestMethod.POST)
  public String addMessage(
      @AuthenticationPrincipal User user,
      @Valid Message message,
      BindingResult bindingResult,
      Model model,
      @RequestParam("file") MultipartFile file
  ) throws IOException {

    message.setAuthor(user);

    if(bindingResult.hasErrors()){

      Map<String, String> errorMap = getErrors(bindingResult);
      model.mergeAttributes(errorMap);
      List<Message> messages = messageService.findAll();
      model.addAttribute("messages",messages);
      return "view/home/main/index";

    }else{
      model.addAttribute("message", null);
      messageService.save(message, file);
      return "redirect:/home";
    }
  }

}
