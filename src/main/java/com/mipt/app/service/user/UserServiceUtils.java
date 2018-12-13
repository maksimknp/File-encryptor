package com.mipt.app.service.user;

import org.apache.commons.codec.digest.DigestUtils;

public class UserServiceUtils {

    public static String md5Encode(String st) {
        return DigestUtils.md5Hex(st);
    }

}
