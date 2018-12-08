package com.mipt.app.service.file;

import com.mipt.app.exception.RegisterException;

import java.io.File;

public class FileServiceUtils {

    public static void checkFilePath(String path){
        File file = new File(path);
        if(!file.exists() || !file.isFile()){
            throw new RegisterException(String.format(RegisterException.BAD_KEY_FILE, path));
        }
    }
}
