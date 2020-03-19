package com.app.sweater.application.rest;

import com.github.cage.GCage;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping(value = "/api/registration")
public class RegistrationRestController {


  @GetMapping(value = "/registration_captcha", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getCaptchaImage(
      HttpServletRequest request,
      HttpServletResponse response
  ) {

    HttpSession session = request.getSession();
    String sessionCaptcha = (String) session.getAttribute("captcha");

    GCage gCage = new GCage();
    final String token = gCage.getTokenGenerator().next();
    session.setAttribute("captcha", token);



    byte[] imgCaptcha = gCage.draw(token);
    Byte[] byteObject = new Byte[imgCaptcha.length];
    for(int i=0; i<imgCaptcha.length; i++)
      byteObject[i] = imgCaptcha[i];

    Map<String, Object> result = new HashMap<>();

    result.put("captcha", byteObject);


    return ResponseEntity.ok().body(result);
  }


}
