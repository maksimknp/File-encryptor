package com.mipt.app.service.user;

import com.mipt.app.database.postgresql.model.file.File;
import com.mipt.app.database.postgresql.model.user.User;
import com.mipt.app.database.postgresql.repositories.user.UserRepository;
import com.mipt.app.database.sqlite.SQLiteService;
import com.mipt.app.exception.RegisterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SQLiteService sqLiteService;

    @Override
    public User userAuthorization(String username, String password, String flashPath, String keyPath) {

        User user = userRepository.getUserByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User not found with username: " + username, null));
        String encodePassword = UserServiceUtils.md5Encode(password);
        if (encodePassword.equals(user.getPassword())){
            checkBothPath(keyPath, flashPath);
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
            writeInFileUserKey(keyPath);
            installDataBaseInFlashdriver(flashPath);
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

    private void writeInFileUserKey(String keyPath) {
        checkKeyFile(keyPath);
        String userKey = generateRandomHexToken(16);
        try(FileWriter writer = new FileWriter(keyPath, true))
        {
            writer.write(userKey);
            writer.flush();
        }
        catch(IOException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void installDataBaseInFlashdriver(String flashPath){
        java.io.File flash = new java.io.File(flashPath);
        if(flash.exists() && flash.isDirectory()) {
            sqLiteService.createNewDatabase(flashPath + java.io.File.separator + SQLiteService.DATABASE_NAME);
        } else {
            throw new RuntimeException("Flash driver path is not correct");
        }
    }

    public static String generateRandomHexToken(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return new BigInteger(1, token).toString(16); //hex encoding
    }

    private void checkBothPath(String keyPath, String flashPath) {
        java.io.File flash = new java.io.File(flashPath);
        if(!flash.exists() || !flash.isDirectory()){
            throw new RuntimeException("Flash path is not correct");
        }

        java.io.File keyFile = new java.io.File(keyPath);
        if(!keyFile.exists() || !keyFile.isFile()){
            throw new RuntimeException("Key path is not correct");
        }

        java.io.File db = new java.io.File(flashPath + java.io.File.separator + SQLiteService.DATABASE_NAME);
        if(!db.exists() || !db.isFile()){
            throw new RuntimeException("Database was deleted");
        }
    }

    private void checkKeyFile(String keyPath){
        java.io.File keyfile = new java.io.File(keyPath);
        if (!keyfile.exists() || !keyfile.isFile()){
            throw new RuntimeException("Key path is not correct");
        } else {
            String line = null;
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(keyfile), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (true){
                try {
                    if (!((line = br.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(!line.isEmpty()){
                    throw new RuntimeException("Key file is not empty");
                }
            }
        }
    }
}
