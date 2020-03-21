package com.app.sweater.persistence;

import com.app.sweater.domain.Message;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

  List<Message> findByTag(String tag);

  List<Message> findAll();


  @Modifying
  @Query(value = "SELECT * FROM message AS m " +
      "INNER JOIN app_user AS u ON u.id=m.user_id WHERE u.username=?1 ", nativeQuery = true)
  List<Message> findAllFromSpecificAuthor( final String username );

}
