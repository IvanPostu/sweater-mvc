package com.app.sweater.application.controller.home;

import com.app.sweater.domain.Message;
import com.app.sweater.domain.User;
import com.app.sweater.persistence.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.app.sweater.application.controller.ControllerUtils.getErrors;

@Controller
@RequestMapping(value = "/")
public class HomeController {

  private static final Logger logger = LoggerFactory.getLogger(HomeController.class);


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



    return "view/home/index";
  }

  @PostMapping("home")
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
      errorMap.forEach((key, val)->model.addAttribute(key,val));
      model.addAttribute("message", message);

      logger.debug("Not valid add message params, errorMap:",errorMap );
    }else{
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

      model.addAttribute("message", null);
      messageRepository.save(message);
    }



    Iterable<Message> messages = messageRepository.findAll();
    model.addAttribute("messages",messages);

    return "view/home/index";
  }


}
