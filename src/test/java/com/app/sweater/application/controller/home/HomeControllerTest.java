package com.app.sweater.application.controller.home;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@TestPropertySource("/application-test.yml")
@Sql(value = {"/create-user-before.sql", "/messages-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/messages-list-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails(value = "rick")
public class HomeControllerTest {
  @Autowired
  private HomeController controller;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void mainPageTest() throws Exception {
    this.mockMvc.perform(get("/home"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(authenticated())
        .andExpect(content().string(containsString("placeholder=\"Поиск по тегу\"")))
        .andExpect(content().string(containsString("id=\"homepageID\"")));
  }

  @Test
  public void messageListTest() throws Exception {
    this.mockMvc.perform(get("/main"))
        .andDo(print())
        .andExpect(authenticated());
//        .andExpect(content().string(containsString("card my-3")));
//        .andExpect(xpath("//*[@id='message-list']/div").nodeCount(4));
  }

  @Test
  public void filterMessageTest() throws Exception {
    this.mockMvc.perform(get("/home").param("filter", "my-tag"))
        .andDo(print())
        .andExpect(authenticated());
//        .andExpect(xpath("//*[@id='message-list']/div").nodeCount(2));
//        .andExpect(xpath("//*[@id='message-list']/div[@data-id='1']").exists())
//        .andExpect(xpath("//*[@id='message-list']/div[@data-id='3']").exists());
  }

  @Test
  public void addMessageToListTest() throws Exception {
    MockHttpServletRequestBuilder multipart = multipart("/main")
        .file("file", "123".getBytes())
        .param("text", "fifth")
        .param("tag", "new one")
        .with(csrf());

    this.mockMvc.perform(multipart)
        .andDo(print())
        .andExpect(authenticated());
//        .andExpect(xpath("//*[@id='message-list']/div").nodeCount(5))
//        .andExpect(xpath("//*[@id='message-list']/div[@data-id='10']").exists())
//        .andExpect(xpath("//*[@id='message-list']/div[@data-id='10']/div/span").string("fifth"))
//        .andExpect(xpath("//*[@id='message-list']/div[@data-id='10']/div/i").string("#new one"));
  }
}