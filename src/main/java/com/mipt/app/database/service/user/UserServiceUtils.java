package com.mipt.app.database.service.user;

import org.apache.commons.codec.digest.DigestUtils;

public class UserServiceUtils {

    public static String md5Encode(String st) {
        String md5Hex = DigestUtils.md5Hex(st);

        return md5Hex;
    }
}
