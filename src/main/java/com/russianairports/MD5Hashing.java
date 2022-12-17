package com.russianairports;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hashing {
    public static String MD5Encrypt(String passwordToHash) throws NoSuchAlgorithmException {
        String generatedPassword = null;

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(passwordToHash.getBytes());
        byte[] bytes = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        generatedPassword = sb.toString();
        return generatedPassword;
    }
}
