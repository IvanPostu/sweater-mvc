package com.app.sweater.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Message {
  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  private String text;
  private String tag;
  private String filename;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="user_id")
  private User author;

  public String getAuthorName(){
    return author != null ? this.author.getUsername() : "\'anonymous\'";
  }

  public Message(String text, String tag, User author) {
    this.text = text;
    this.tag = tag;
    this.author = author;
  }

}