package main.java.es.octal.MotherFocaTagTool.config.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

/**
 * Created by Alvaro on 17/09/14.
 * MotherFocaTagTool
 */

public class EncryptionHandler {

    private String key;

    // Constructor

    public static void main(String[] args) {
        try {

            String key = "ThisIsASecretKey";
            byte[] ciphertext = encrypt(key, "1234567890123456");
            System.out.println("decrypted value:" + (decrypt(key, ciphertext)));

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(String key, String value)
            throws GeneralSecurityException {

        byte[] raw = key.getBytes(Charset.forName("US-ASCII"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,
                new IvParameterSpec(new byte[16]));
        return cipher.doFinal(value.getBytes(Charset.forName("US-ASCII")));
    }

    public static String decrypt(String key, byte[] encrypted)
            throws GeneralSecurityException {

        byte[] raw = key.getBytes(Charset.forName("US-ASCII"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec,
                new IvParameterSpec(new byte[16]));
        byte[] original = cipher.doFinal(encrypted);

        return new String(original, Charset.forName("US-ASCII"));
    }
}
