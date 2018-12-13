package com.mipt.app.controller;

import com.mipt.app.database.postgresql.model.file.File;
import com.mipt.app.database.postgresql.model.user.User;
import com.mipt.app.service.file.FileService;
import com.mipt.app.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/enc")
@CrossOrigin
public class RestValueController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @PostMapping("/user/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User authorization(@RequestParam(value = "username") String username,
                              @RequestParam(value = "password") String password,
                              @RequestParam(value = "flashPath") String flashPath,
                              @RequestParam(value = "keyPath") String keyPath) {
        return userService.userAuthorization(username, password, flashPath, keyPath);
    }

    @PostMapping("/user/reg")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User registration(@RequestParam(value = "username") String username,
                             @RequestParam(value = "password") String password,
                             @RequestParam(value = "flashPath") String flashPath,
                             @RequestParam(value = "keyPath") String keyPath) {
        return userService.createUser(username, password, flashPath, keyPath);
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.FOUND)
    public User getUserById(@RequestParam(value = "userId") Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("file/encrypt")
    @ResponseStatus(HttpStatus.OK)
    public File encryptFile(@RequestParam(value = "userId") Long userId,
                            @RequestParam(value = "filePath") String filePath) {
        return fileService.encryptfile(userId, filePath);
    }

    @PostMapping("file/decrypt")
    @ResponseStatus(HttpStatus.OK)
    public Boolean decryptFile(@RequestParam(value = "fileId") Long id) {
        return fileService.decryptfile(id);
    }

    @GetMapping("/user/files")
    @ResponseStatus(HttpStatus.OK)
    public List<File> getAllFilesByUserId(@RequestParam(value = "userId") Long userId) {
        return userService.getAllFilesByUserId(userId);
    }
}
