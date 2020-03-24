package com.app.sweater.domain.util;

import com.app.sweater.domain.entity.User;

public abstract class MessageHelper {

  public static String getAuthorName(User author){
    return author != null ? author.getUsername() : "< none >";
  }

}
