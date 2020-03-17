package com.app.sweater.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

  @Value("${spring.mail.host}")
  private String host;

  @Value("${spring.mail.username}")
  private String username;

  @Value("${spring.mail.password}")
  private String password;

  @Value("${spring.mail.port}")
  private int port;

  @Value("${spring.mail.transport.protocol}")
  private String protocol;

  @Value("${spring.mail.smtp.auth}")
  private String auth;

  @Value("${spring.mail.smtp.starttls.enable}")
  private String enable;

  @Value("${spring.mail.default-encoding}")
  private String encoding;

  @Value("${spring.mail.debug}")
  private String debug;


  @Bean
  public JavaMailSender getMailSender(){
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setUsername(username);
    mailSender.setPassword(password);
    mailSender.setHost(host);
    mailSender.setPort(port);

    Properties properties = mailSender.getJavaMailProperties();

    properties.setProperty("mail.transport.protocol", protocol);
    properties.setProperty("mail.debug", debug);
    properties.setProperty("mail.smtp.auth", auth);
    properties.setProperty("mail.smtp.starttls.enable", enable);
    properties.setProperty("mail.default-encoding", enable);

    return mailSender;
  }

}
