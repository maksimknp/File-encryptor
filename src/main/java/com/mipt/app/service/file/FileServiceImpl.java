package com.mipt.app.service.file;

import com.mipt.app.apllicationUtils.ExecuteCommand;
import com.mipt.app.database.postgresql.model.file.File;
import com.mipt.app.database.postgresql.model.user.User;
import com.mipt.app.database.postgresql.repositories.file.FileRepository;
import com.mipt.app.database.postgresql.repositories.user.UserRepository;
import com.mipt.app.exception.SaveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public File addNewFile(String filePath, Long userId) {
        FileServiceUtils.checkFilePath(filePath);
        User owner = userRepository.findOne(userId);

        File createdFile = new File(filePath, owner);
        if (!fileRepository.existsByPathAndUser(filePath, owner)){
            createdFile = fileRepository.save(createdFile);
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

        //encryptFile
        System.out.println(new Date());
        ExecuteCommand.executeCommand(String.format(ENCRYPT_FILE, needFile.getUser().getKeyPath(), filePath));
        System.out.println(new Date());

        return fileRepository.save(needFile);
    }

    @Override
    public File decryptfile(Long id) {
        File needFile = fileRepository.findOne(id);
        String filePath = needFile.getPath();

        //decryptFile
        ExecuteCommand.executeCommand(String.format(DECRYPT_FILE, needFile.getUser().getKeyPath(), filePath));

        return fileRepository.save(needFile);
    }

}
