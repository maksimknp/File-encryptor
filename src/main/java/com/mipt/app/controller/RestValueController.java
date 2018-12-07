package com.mipt.app.controller;

import com.mipt.app.database.model.file.File;
import com.mipt.app.database.model.user.User;
import com.mipt.app.service.file.FileService;
import com.mipt.app.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/enc")
@CrossOrigin
public class RestValueController {

  @Autowired
  private UserService userService;

  @Autowired
  private FileService fileService;

  //TODO: return user
  @PostMapping("/user/login")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public User authorization(@RequestParam(value = "username") String username,
                               @RequestParam(value = "password") String password){
    return userService.userAuthorization(username, password);
  }

  @PostMapping("/user/reg")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public User registration(@RequestParam(value = "username") String username,
                           @RequestParam(value = "password") String password){
    return userService.createUser(username, password);
  }

  @PostMapping("/file/save")
  @ResponseStatus(HttpStatus.CREATED)
  public File addFile(@RequestParam(value = "path") String filePath,
                      @RequestParam(value = "userId") Long userId){
    return fileService.addNewFile(filePath, userId);
  }

  @GetMapping("/user")
  @ResponseStatus(HttpStatus.FOUND)
  public User getUserById(@RequestParam(value = "userId") Long userId){
    return userService.getUserById(userId);
  }

  @PostMapping("file/encrypt")
  @ResponseStatus(HttpStatus.OK)
  public File encryptFile(@RequestParam(value = "fileId") Long id){
    return fileService.encryptfile(id);
  }

  @PostMapping("file/decrypt")
  @ResponseStatus(HttpStatus.OK)
  public File decryptFile(@RequestParam(value = "fileId") Long id){
    return fileService.decryptfile(id);
  }

  //TODO: endpoint get all files  by userId
}