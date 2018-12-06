package com.mipt.app.service.file;

import com.mipt.app.apllicationUtils.ExecuteCommand;
import com.mipt.app.database.model.file.File;
import com.mipt.app.database.model.user.User;
import com.mipt.app.database.repositories.file.FileRepository;
import com.mipt.app.database.repositories.user.UserRepository;
import com.mipt.app.enums.FileStatus;
import com.mipt.app.exception.SaveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {

    private final String ENCRYPT_FILE = "./crypto --key %s --file %s --encrypt";
    private final String DECRYPT_FILE = "./crypto --key %s --file %s --decrypt";

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
    public File addNewFile(String filePath, Long userId) {
        User owner = userRepository.findOne(userId);

        File createdFile = new File(filePath, FileStatus.DECRYPTED, owner);
        if (!fileRepository.existsByPath(filePath)){
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

        if(FileStatus.ENCRYPTED.equals(needFile.getStatus())){
            throw new RuntimeException(String.format("File with path: %s is encrypted", filePath));
        }

        //encryptFile
        ExecuteCommand.executeCommand(String.format(ENCRYPT_FILE, getKey(needFile), filePath));

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
        ExecuteCommand.executeCommand(String.format(DECRYPT_FILE, getKey(needFile), filePath));

        return fileRepository.save(changeStatusById(needFile));
    }

    private String getKey(File file){
        return file.getUser().getPassword();
    }

}
