package com.app.sweater.application.controller.home;

import com.app.sweater.application.controller.ControllerUtils;
import com.app.sweater.application.service.MessageService;
import com.app.sweater.domain.Message;
import com.app.sweater.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
      Model model,
      @PageableDefault(sort={"id"}, direction = Sort.Direction.DESC) Pageable pageable
  ){
    Page<Message> page;

    if(StringUtils.isEmpty(filter)){
      page = messageService.findAll(pageable);
    }else{
      page = messageService.findByTag(filter, pageable);
      model.addAttribute("filter", filter);
    }

    model.addAttribute("page", page);
    final int diapason = 5;
    model.addAttribute("paginationArray",
        ControllerUtils.calculatePages(diapason,page.getNumber(),page.getTotalPages()));

    return "view/home/main/index";
  }

  @RequestMapping(value = {"/home"}, method = RequestMethod.POST)
  public String addMessage(
      @AuthenticationPrincipal User user,
      @Valid Message message,
      BindingResult bindingResult,
      Model model,
      @RequestParam("file") MultipartFile file,
      @PageableDefault(sort={"id"}, direction = Sort.Direction.DESC) Pageable pageable

  ) throws IOException {

    message.setAuthor(user);

    if(bindingResult.hasErrors()){

      Map<String, String> errorMap = getErrors(bindingResult);
      model.mergeAttributes(errorMap);
      Page<Message> page = messageService.findAll(pageable);
      model.addAttribute("page", page);
      final int diapason = 5;
      model.addAttribute("paginationArray",
          ControllerUtils.calculatePages(diapason,page.getNumber(),page.getTotalPages()));

      return "view/home/main/index";

    }else{
      messageService.save(message, file);
      return "redirect:/home";
    }
  }

}
