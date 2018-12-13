package com.mipt.app.service.file;

import com.mipt.app.database.postgresql.model.file.File;

public interface FileService {

    File addNewFile(String filePath, Long userId);

    File encryptfile(Long id);

    File decryptfile(Long id);
}
