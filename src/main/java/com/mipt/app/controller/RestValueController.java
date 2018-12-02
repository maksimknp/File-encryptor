package com.mipt.app.controller;

import com.mipt.app.database.model.user.User;
import com.mipt.app.database.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/enc")
@CrossOrigin
public class RestValueController {

  @Autowired
  private UserService userService;

  @PostMapping("/user/login")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public boolean authorization(@RequestParam(value = "username") String username,
                               @RequestParam(value = "password") String password){
    return userService.userAuthorization(username, password);
  }

  @PostMapping("/user/reg")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public User registration(@RequestParam(value = "username") String username,
                           @RequestParam(value = "password") String password){
    return userService.createUser(username, password);
  }
}