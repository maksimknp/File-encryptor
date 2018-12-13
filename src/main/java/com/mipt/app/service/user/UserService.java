package com.mipt.app.service.user;

import com.mipt.app.database.postgresql.model.file.File;
import com.mipt.app.database.postgresql.model.user.User;

import java.util.List;

public interface UserService {

    User userAuthorization(String username, String password, String flashPath, String keyPath);

    User createUser(String username, String password, String flashPath, String keyPath);

    User getUserById(Long userId);

    List<File> getAllFilesByUserId(Long userId);
}
