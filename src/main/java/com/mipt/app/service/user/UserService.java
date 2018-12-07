package com.mipt.app.service.user;

import com.mipt.app.database.model.file.File;
import com.mipt.app.database.model.user.User;

import java.util.List;

public interface UserService {

    User userAuthorization(String username, String password);

    User createUser(String username, String password);

    User getUserById(Long userId);

    List<File> getAllFilesByUserId(Long userId);
}
