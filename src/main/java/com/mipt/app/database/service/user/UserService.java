package com.mipt.app.database.service.user;

public interface UserService {

    boolean userAuthorization(String username, String password);
}
