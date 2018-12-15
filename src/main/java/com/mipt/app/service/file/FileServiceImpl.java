package com.mipt.app.service.file;

import com.mipt.app.apllicationUtils.ExecuteCommand;
import com.mipt.app.database.postgresql.model.file.File;
import com.mipt.app.database.postgresql.model.user.User;
import com.mipt.app.database.postgresql.repositories.file.FileRepository;
import com.mipt.app.database.postgresql.repositories.user.UserRepository;
import com.mipt.app.database.sqlite.SQLiteService;
import com.mipt.app.exception.SaveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private final String ENCRYPT_FILE = "./crypto --encrypt --key-path %s --file %s";
    private final String DECRYPT_FILE = "./crypto --decrypt --key-path %s --file %s";
    private final String DATABASE_PATH = java.io.File.separator + SQLiteService.DATABASE_NAME;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SQLiteService sqLiteService;

    @Override
    public File addNewFile(String filePath, Long userId) {
        FileServiceUtils.checkFilePath(filePath);
        User owner = userRepository.findOne(userId);

        File createdFile = new File(filePath, owner);
        if (!fileRepository.existsByPathAndUser(filePath, owner)) {
            createdFile = fileRepository.save(createdFile);
        } else {
            throw new SaveException(String.format(SaveException.FILE_SAVE_EXCEPTION, filePath));
        }

        owner.getFiles().add(createdFile);
        return createdFile;
    }

    @Override
    public File encryptfile(Long userId, String filePath) {
        File needFile = addNewFile(filePath, userId);
        User owner = needFile.getUser();
        String keyPath = owner.getKeyPath();

        //encryptFile
        String oldKey = getKeyFromFile(keyPath);
        log.info("Old key: {}", oldKey);
        String newKey = getNewKey(oldKey, owner.getPassword());
        log.info("New key: {}", newKey);
        writeInFileKey(keyPath, newKey);
        ExecuteCommand.executeCommand(String.format(ENCRYPT_FILE, keyPath, filePath));
        sqLiteService.encryptFile(
                owner.getDrivePath() + DATABASE_PATH,
                needFile.getId(),
                filePath);
        java.io.File realFile = new java.io.File(filePath);
        if(!realFile.delete()){
            throw new RuntimeException("File couldn't be deleted");
        }
        writeInFileKey(keyPath, oldKey);
        return fileRepository.save(needFile);
    }

    private String getNewKey(String oldKey, String password) {
        return base64Encode(xorWithKey(base64Decode(oldKey), base64Decode(password))).substring(0, 32);
    }

    @Override
    public boolean decryptfile(Long id) {
        File needFile = fileRepository.findOne(id);
        String filePath = needFile.getPath();
        User owner = needFile.getUser();
        String keyPath = owner.getKeyPath();

        //decryptFile
        String oldKey = getKeyFromFile(keyPath);
        log.info("Old key: {}", oldKey);
        String newKey = getNewKey(oldKey, owner.getPassword());
        log.info("New key: {}", newKey);
        writeInFileKey(keyPath, newKey);
        sqLiteService.decryptFile(owner.getDrivePath() + DATABASE_PATH, id);
        sqLiteService.delete(owner.getDrivePath() + DATABASE_PATH, id);
        ExecuteCommand.executeCommand(String.format(DECRYPT_FILE, keyPath, filePath));

        fileRepository.delete(needFile);
        writeInFileKey(keyPath, oldKey);
        return !fileRepository.exists(id);
    }

    private String getKeyFromFile(String path){
        try {
            return Files.lines(Paths.get(path), StandardCharsets.UTF_8).findFirst().get();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private byte[] xorWithKey(byte[] a, byte[] key) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ key[i%key.length]);
        }
        return out;
    }

    private byte[] base64Decode(String s) {
        try {
            BASE64Decoder d = new BASE64Decoder();
            return d.decodeBuffer(s);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private String base64Encode(byte[] bytes) {
        BASE64Encoder enc = new BASE64Encoder();
        return enc.encode(bytes).replaceAll("\\s", "");

    }

    public void writeInFileKey(String keyPath, String userKey) {
        try(FileWriter writer = new FileWriter(keyPath, false))
        {
            writer.write(userKey);
            writer.flush();
        }
        catch(IOException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

}
