package com.app.sweater.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Message {
  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Integer id;

  private String text;
  private String tag;



  public Message(String text, String tag) {
    this.text = text;
    this.tag = tag;
  }

}