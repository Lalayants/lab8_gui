package controller.utilities;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptor {
    public static String generate(String str){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-384");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(str.getBytes());
        byte[] digest = md.digest();
        String result = new BigInteger(1, digest).toString(16).toUpperCase();
        return result;
    }
}
