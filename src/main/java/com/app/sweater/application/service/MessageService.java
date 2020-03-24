package com.app.sweater.application.service;

import com.app.sweater.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MessageService {

  Page<Message> findAll(Pageable pageable);

  Page<Message> findByTag(String filter, Pageable pageable);

  Message findById(Long id);

  Message save(Message message);

  Message save(Message message, MultipartFile imageFile) throws IOException;


}
