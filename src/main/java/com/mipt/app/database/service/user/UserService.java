package com.mipt.app.database.service.user;

import com.mipt.app.database.model.user.User;

public interface UserService {

    boolean userAuthorization(String username, String password);

    User createUser(String username, String password);
}
