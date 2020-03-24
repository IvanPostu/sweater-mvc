package com.app.sweater.application.controller.user;


import com.app.sweater.application.controller.ControllerUtils;
import com.app.sweater.application.service.MessageService;
import com.app.sweater.application.service.UserService;
import com.app.sweater.domain.Message;
import com.app.sweater.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.app.sweater.application.controller.ControllerUtils.getErrors;

@Controller
@RequestMapping(value = "/user-messages")
public class UserMessageController {

  @Autowired
  private MessageService messageService;

  @Autowired
  private UserService userService;

  @GetMapping
  @PreAuthorize("hasAuthority('USER')")
  public String getUserMessages(
      @AuthenticationPrincipal User currentUser,
      @RequestParam(required = false, value = "username") String username,
      @RequestParam(required = false, value = "updateMessageId") Long updateMessageId,
      Model model,
      @PageableDefault(sort={"id"}, direction = Sort.Direction.DESC) Pageable pageable
  ) {

    if (!StringUtils.isEmpty(username)) {
      User channelUser = userService.findByUsername(username);
      List<Message> messages = channelUser.getMessages();

      long start = pageable.getOffset();
      long end = (start + pageable.getPageSize()) > messages.size() ? messages.size() : (start + pageable.getPageSize());
      Page<Message> page = new PageImpl<>(messages.subList((int) start, (int) end), pageable, messages.size());
      final int diapason = 5;

      model.addAttribute("paginationArray",
          ControllerUtils.calculatePages(diapason,page.getNumber(),page.getTotalPages()));
      model.addAttribute("page", page);




      model.addAttribute("channelUser", channelUser);
      model.addAttribute("isCurrentUser", channelUser.equals(currentUser));
      model.addAttribute("subscriptionsCount", channelUser.getSubscriptions().size());
      model.addAttribute("subscribersCount", channelUser.getSubscribers().size());


      if (updateMessageId != null) {
        Message message = messageService.findById(updateMessageId);
        try{
          if (message.getAuthor().equals(currentUser)) {
            model.addAttribute("message", message);
          }
        }catch(NullPointerException ignored){
        }
      }

      return "view/user/messages/index";
    } else {
      return String.format("redirect:/user-messages?username=%s", currentUser.getUsername());
    }


  }

  @PostMapping(value = "/update")
  @PreAuthorize("hasAuthority('USER')")
  public String updateMessage(
      @AuthenticationPrincipal User currentUser,
      @Valid Message message,
      BindingResult bindingResult,
      Model model,
      @RequestParam("file") MultipartFile file
  ){

    try{
      Message originalMessage = messageService.findById(message.getId());

      if(originalMessage == null){
        model.addAttribute("messageForUpdateNotFound", "Message for update is not found." );
        throw new Exception();
      }

      if(originalMessage.getAuthor()==null || !originalMessage.getAuthor().equals(currentUser)){
        model.addAttribute("genericError", "Update error. It is not your message." );
        throw new Exception();
      }

      message.setAuthor(currentUser);
      message.setFilename(originalMessage.getFilename());

      if(bindingResult.hasErrors()){
        Map<String, String> errorMap = getErrors(bindingResult);
        model.mergeAttributes(errorMap);
        throw new Exception();
      }

      try{
        boolean fileImageIsChanged = !originalMessage.getFilename().contains(file.getOriginalFilename());
        if(fileImageIsChanged){
          messageService.save(message, file);
        }else{
          messageService.save(message);
        }
      }catch(NullPointerException nullPointerException){
        messageService.save(message);
      }


    }catch(Exception e){
      List<Message> messages = currentUser.getMessages();
      model.addAttribute("messages", messages);
      model.addAttribute("genericError", "Update error." );
      return "view/user/messages/index";
    }



    return "redirect:/user-messages";
  }


}
