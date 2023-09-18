package kriptiranje;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.*;

public class Enkripcija {
    static Cipher cipher;
    SecretKey secretKey;

    public Enkripcija() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        this.secretKey = keyGenerator.generateKey();
        cipher = Cipher.getInstance("AES");
    }

    public String encrypt(String plainText) throws Exception {
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);
        return encryptedText;
    }

    public String decrypt(String encryptedText) throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }

    //java Last unit does not have enough valid bits
}