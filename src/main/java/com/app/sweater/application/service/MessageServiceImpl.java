package com.app.sweater.application.service;

import com.app.sweater.domain.Message;
import com.app.sweater.persistence.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;

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
    return messageRepository.findAll();
  }

  @Override
  public Message save(Message message) {
    return messageRepository.save(message);
  }

}
