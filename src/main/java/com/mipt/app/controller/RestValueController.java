package com.mipt.app.controller;

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

  @PostMapping("/user")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public boolean authorization(@RequestParam(value = "username") String username,
                               @RequestParam(value = "password") String password){
    return userService.userAuthorization(username, password);
  }

}