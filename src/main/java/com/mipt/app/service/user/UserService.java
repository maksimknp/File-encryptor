package com.mipt.app.service.user;

import com.mipt.app.database.model.user.User;

public interface UserService {

    User userAuthorization(String username, String password);

    User createUser(String username, String password);

    User getUserById(Long userId);
}
