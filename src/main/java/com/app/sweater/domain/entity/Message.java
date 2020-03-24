package com.app.sweater.domain.entity;


import com.app.sweater.domain.util.MessageHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

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

  @ManyToMany
  @JoinTable(
      name = "message_likes",
      joinColumns = { @JoinColumn(name = "message_id") },
      inverseJoinColumns = { @JoinColumn(name = "user_id") }
  )
  private Set<User> likes = new HashSet<>();

  public String getAuthorName(){
    return MessageHelper.getAuthorName(author);
  }

  public Message(String text, String tag, User author) {
    this.text = text;
    this.tag = tag;
    this.author = author;
  }

}