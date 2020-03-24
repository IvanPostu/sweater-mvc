package com.app.sweater.application.controller.home;

import com.app.sweater.application.controller.ControllerUtils;
import com.app.sweater.application.service.MessageService;
import com.app.sweater.domain.dto.MessageDto;
import com.app.sweater.domain.entity.Message;
import com.app.sweater.domain.entity.User;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

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
      @PageableDefault(sort={"id"}, direction = Sort.Direction.DESC) Pageable pageable,
      @AuthenticationPrincipal User user
  ){
    Page<MessageDto> page;

    if(StringUtils.isEmpty(filter)){
      page = messageService.findAll(pageable, user);
    }else{
      page = messageService.findByTag(filter, pageable, user);
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
      Page<MessageDto> page = messageService.findAll(pageable, user);
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


  @GetMapping("/messages/{message}/like")
  public String like(
      @AuthenticationPrincipal User currentUser,
      @PathVariable Message message,
      RedirectAttributes redirectAttributes,
      @RequestHeader(required = false) String referer
  ) {
    Set<User> likes = message.getLikes();

    if (likes.contains(currentUser)) {
      likes.remove(currentUser);
    } else {
      likes.add(currentUser);
    }

    UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

    components.getQueryParams()
        .entrySet()
        .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

    return "redirect:" + components.getPath();
  }

}
