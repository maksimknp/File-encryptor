package com.mipt.app.service.file;

import com.mipt.app.database.model.file.File;

public interface FileService {

    File changeStatusById(File file);

    File addNewFile(String filePath, Long userId, String keyPath);

    File encryptfile(Long id);

    File decryptfile(Long id);
}
