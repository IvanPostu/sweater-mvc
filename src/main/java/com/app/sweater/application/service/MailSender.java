package com.app.sweater.application.service;


public interface MailSender {

  public void send(String emailTo, String subject, String message);

}
