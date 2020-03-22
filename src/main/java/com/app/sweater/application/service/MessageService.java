package com.app.sweater.application.service;

import com.app.sweater.domain.Message;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MessageService {

  List<Message> findAll();


  List<Message> findByTag(String filter);

  List<Message> findAllFromSpecificAuthor(String username);

  Message findById(Long id);

  Message save(Message message);

  Message save(Message message, MultipartFile imageFile) throws IOException;
}
