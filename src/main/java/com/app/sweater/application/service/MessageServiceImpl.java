package com.app.sweater.application.service;

import com.app.sweater.domain.Message;
import com.app.sweater.persistence.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;

  @Value("${upload.path}")
  private String uploadPath;

  @Autowired
  public MessageServiceImpl(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  @Override
  public List<Message> findAll() {
    return messageRepository.findAll();
  }

  @Override
  public List<Message> findByTag(String filterTag) {
    return messageRepository.findByTag(filterTag);
  }

  @Override
  public List<Message> findAllFromSpecificAuthor(String username) {
    return messageRepository.findAllFromSpecificAuthor(username);
  }

  @Override
  public Message findById(Long id) {
    return messageRepository.findById(id).get();
  }

  @Override
  public Message save(Message message, MultipartFile imageFile) throws IOException {
    saveFile(message, imageFile);
    return messageRepository.save(message);
  }

  @Override
  public Message save(Message message){
    return messageRepository.save(message);
  }

  private void saveFile(Message message, MultipartFile file) throws IOException {
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
  }

}
