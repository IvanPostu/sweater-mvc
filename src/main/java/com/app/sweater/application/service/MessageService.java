package com.app.sweater.application.service;

import com.app.sweater.domain.dto.MessageDto;
import com.app.sweater.domain.entity.Message;
import com.app.sweater.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MessageService {

  Page<MessageDto> findAll(Pageable pageable, User user);

  Page<MessageDto> findByTag(String filter, Pageable pageable, User user);

  Page<MessageDto> messageListForUser(Pageable pageable, User currentUser, User author);

  Message findById(Long id);

  Message save(Message message);

  Message save(Message message, MultipartFile imageFile) throws IOException;


}
