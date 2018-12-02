package com.mipt.app.database.service.user;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    public boolean userAuthorization(String username, String password){
        return false;
    }
}
