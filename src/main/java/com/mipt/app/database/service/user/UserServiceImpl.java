package com.mipt.app.database.service.user;

import com.mipt.app.database.model.user.User;
import com.mipt.app.database.repositories.user.UserRepository;
import com.mipt.app.exception.RegisterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean userAuthorization(String username, String password) {
        User user = userRepository.getUserByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User not found with username: " + username, null));
        String encodePassword = UserServiceUtils.md5Encode(password);
        return encodePassword.equals(user.getPassword());
    }

    @Override
    public User createUser(String username, String password) {
        String encodePassword = UserServiceUtils.md5Encode(password);
        User createdUser = new User(username, encodePassword);
        if (!userRepository.existsByUsername(username)) {
            userRepository.save(createdUser);
        } else {
            throw new RegisterException(String.format(RegisterException.EXIST_USERNAME, username));
        }
        return createdUser;
    }

}
