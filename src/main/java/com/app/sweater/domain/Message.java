package com.app.sweater.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Message {
  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  @NotBlank(message = "Please fill the message.")
  @Length(max = 2048, message = "Message too long (more than 2kB)")
  private String text;

  @NotBlank(message = "Please fill the tag.")
  @Length(max = 255, message = "Message too long (more than 255)")
  private String tag;


  private String filename;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="user_id")
  private User author;

  public String getAuthorName(){
    return author != null ? this.author.getUsername() : "";
  }

  public Message(String text, String tag, User author) {
    this.text = text;
    this.tag = tag;
    this.author = author;
  }

}