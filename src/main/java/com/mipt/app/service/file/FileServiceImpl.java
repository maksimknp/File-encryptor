package com.mipt.app.service.file;

import com.mipt.app.apllicationUtils.ExecuteCommand;
import com.mipt.app.database.postgresql.model.file.File;
import com.mipt.app.database.postgresql.model.user.User;
import com.mipt.app.database.postgresql.repositories.file.FileRepository;
import com.mipt.app.database.postgresql.repositories.user.UserRepository;
import com.mipt.app.database.sqlite.SQLiteService;
import com.mipt.app.exception.SaveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {

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

        //encryptFile
        ExecuteCommand.executeCommand(String.format(ENCRYPT_FILE, owner.getKeyPath(), filePath));
        sqLiteService.encryptFile(
                owner.getDrivePath() + DATABASE_PATH,
                needFile.getId(),
                filePath);
        java.io.File realFile = new java.io.File(filePath);
        if(!realFile.delete()){
            throw new RuntimeException("File couldn't be deleted");
        }
        return fileRepository.save(needFile);
    }

    @Override
    public boolean decryptfile(Long id) {
        File needFile = fileRepository.findOne(id);
        String filePath = needFile.getPath();
        User owner = needFile.getUser();

        //decryptFile
        sqLiteService.decryptFile(owner.getDrivePath() + DATABASE_PATH, id);
        sqLiteService.delete(owner.getDrivePath() + DATABASE_PATH, id);
        ExecuteCommand.executeCommand(String.format(DECRYPT_FILE, owner.getKeyPath(), filePath));

        fileRepository.delete(needFile);
        return !fileRepository.exists(id);
    }

}
