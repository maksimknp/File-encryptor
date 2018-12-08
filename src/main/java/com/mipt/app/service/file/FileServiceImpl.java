package com.mipt.app.service.file;

import com.mipt.app.apllicationUtils.ExecuteCommand;
import com.mipt.app.database.model.file.File;
import com.mipt.app.database.model.user.User;
import com.mipt.app.database.repositories.file.FileRepository;
import com.mipt.app.database.repositories.user.UserRepository;
import com.mipt.app.enums.FileStatus;
import com.mipt.app.exception.SaveException;
import com.mipt.app.service.user.UserServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

@Service
public class FileServiceImpl implements FileService {

    private final String ENCRYPT_FILE = "./crypto --encrypt --key-path %s --file %s";
    private final String DECRYPT_FILE = "./crypto --decrypt --key-path %s --file %s";

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public File changeStatusById(File file) {
        FileStatus newStatus = file.getStatus().equals(FileStatus.DECRYPTED)
                ? FileStatus.ENCRYPTED
                : FileStatus.DECRYPTED;
        file.setStatus(newStatus);
        return file;
    }

    @Override
    public File addNewFile(String filePath, Long userId, String keyPath) {
        FileServiceUtils.checkFilePath(filePath);
        FileServiceUtils.checkFilePath(keyPath);

        User owner = userRepository.findOne(userId);

        File createdFile = new File(filePath, owner, keyPath);
        if (!fileRepository.existsByPathAndUser(filePath, owner)){
            createdFile = fileRepository.save(createdFile);
            writeInFileUserKey(createdFile.getName() + owner.getPassword(), keyPath);
        } else {
            throw new SaveException(String.format(SaveException.FILE_SAVE_EXCEPTION, filePath));
        }

        owner.getFiles().add(createdFile);
        return createdFile;
    }

    @Override
    public File encryptfile(Long id) {
        File needFile = fileRepository.findOne(id);
        String filePath = needFile.getPath();

        if(FileStatus.ENCRYPTED.equals(needFile.getStatus())){
            throw new RuntimeException(String.format("File with path: %s is encrypted", filePath));
        }

        //encryptFile
        System.out.println(new Date());
        ExecuteCommand.executeCommand(String.format(ENCRYPT_FILE, needFile.getKeyPath(), filePath));
        System.out.println(new Date());

        return fileRepository.save(changeStatusById(needFile));
    }

    @Override
    public File decryptfile(Long id) {
        File needFile = fileRepository.findOne(id);
        String filePath = needFile.getPath();

        if(FileStatus.DECRYPTED.equals(needFile.getStatus())){
            throw new RuntimeException(String.format("File with path: %s is decrypted", filePath));
        }

        //decryptFile
        ExecuteCommand.executeCommand(String.format(DECRYPT_FILE, needFile.getKeyPath(), filePath));

        return fileRepository.save(changeStatusById(needFile));
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
