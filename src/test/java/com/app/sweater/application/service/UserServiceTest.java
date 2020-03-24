package com.app.sweater.application.service;

import com.app.sweater.application.service.exceptions.UserNotActivatedException;
import com.app.sweater.domain.entity.Role;
import com.app.sweater.domain.entity.User;
import com.app.sweater.persistence.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
@TestPropertySource("/application-test.yml")
class UserServiceTest {

  @Autowired
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private MailSender mailSender;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @Test
  void addUser() {
    User user = new User();
    user.setEmail("some@mail.ru");
    boolean isUserCreated = userService.addUser(user);

    Assert.assertTrue(isUserCreated);
    Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));
    Assert.assertNotNull(user.getActivationCode());

    Mockito.verify(userRepository, Mockito.times(1)).save(user);
    Mockito.verify(mailSender, Mockito.times(1))
        .send(
            ArgumentMatchers.eq(user.getEmail()),
            ArgumentMatchers.eq("Activation code"),
            ArgumentMatchers.contains("Welcome to Sweater")
        );



  }



  @Test
  public void addUserFailTest() {
    User user = new User();

    user.setUsername("John");

    Mockito.doReturn(new User())
        .when(userRepository)
        .findByUsername("John");

    boolean isUserCreated = userService.addUser(user);

    Assert.assertFalse(isUserCreated);

    Mockito.verify(userRepository, Mockito.times(0))
        .save(ArgumentMatchers.any(User.class));
    Mockito.verify(mailSender, Mockito.times(0))
        .send(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString()
        );
  }



  @Test
  public void activateUser() throws UserNotActivatedException {
    User user = new User();

    user.setActivationCode("qwe!");

    Mockito.doReturn(user)
        .when(userRepository)
        .findByActivationCode("activate");

    String activatedUsername = userService.activateUserAndReturnUsername("activate");

//    Assert.assertNotNull(activatedUsername);
    Assert.assertNull(user.getActivationCode());

    Mockito.verify(userRepository, Mockito.times(1)).save(user);
  }


  @Test
  public void activateUserFailTest() {

    String activatedUsername = null;

    try{
      activatedUsername = userService.activateUserAndReturnUsername("activate me");
    }catch(UserNotActivatedException e){
      Assert.assertNull(activatedUsername);
    }


    Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
  }

}