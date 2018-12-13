package com.mipt.app.service.user;

import com.mipt.app.database.postgresql.model.file.File;
import com.mipt.app.database.postgresql.model.user.User;
import com.mipt.app.database.postgresql.repositories.user.UserRepository;
import com.mipt.app.exception.RegisterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User userAuthorization(String username, String password, String flashPath, String keyPath) {

        User user = userRepository.getUserByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User not found with username: " + username, null));
        String encodePassword = UserServiceUtils.md5Encode(password);
        if (encodePassword.equals(user.getPassword())){
            user.setKeyPath(keyPath);
            user.setDrivePath(flashPath);
            userRepository.save(user);
            return user;
        } else {
            throw new RuntimeException("Wrong password");
        }
    }

    @Override
    public User createUser(String username, String password, String flashPath, String keyPath) {
        String encodePassword = UserServiceUtils.md5Encode(password);
        User createdUser = new User(username, encodePassword, flashPath, keyPath);

        if (!userRepository.existsByUsername(username)) {
            createdUser = userRepository.save(createdUser);

        } else {
            throw new RegisterException(String.format(RegisterException.EXIST_USERNAME, username));
        }
        return createdUser;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public List<File> getAllFilesByUserId(Long userId) {
        return userRepository.findOne(userId).getFiles();
    }

    private void writeInFileUserKey(String password, String keyPath) {
        String userKey = UserServiceUtils.md5Encode(password);
        try(FileWriter writer = new FileWriter(keyPath, true))
        {
            writer.write(userKey);
            writer.flush();
        }
        catch(IOException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
}
