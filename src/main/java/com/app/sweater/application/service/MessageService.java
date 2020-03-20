package com.app.sweater.application.service;

import com.app.sweater.domain.Message;

import java.util.List;

public interface MessageService {

  List<Message> findAll();


  List<Message> findByTag(String filter);

  List<Message> findAllFromSpecificAuthor(String username);

  Message save(Message message);
}
