package amar.im.cryptoapp;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KeyHelper {
    public static final String mClearText = "121727954";
    public static final String mKeyString = "amar.im.test.TESTER_LOCALITY_CHANGE_INTENT";

    private static String mReceivedKey;

    public static String getHash(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.reset();
        messageDigest.update(key.getBytes());
        byte[] bytes = messageDigest.digest();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private static String encrypt(String text) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        String key = getHash(mReceivedKey);

        byte[] decodedKey = Base64.decode(key, Base64.DEFAULT);
        SecretKey sKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        byte[] textBytes = text.getBytes();

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sKey);
        byte[] bytes = cipher.doFinal(textBytes);

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private static String decrypt(String text) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        String key = getHash(mReceivedKey);
        byte[] decodedKey = Base64.decode(key, Base64.DEFAULT);

        SecretKey sKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        byte[] encryptedTextByte = Base64.decode(text, Base64.DEFAULT);

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sKey);
        byte[] bytes = cipher.doFinal(encryptedTextByte);

        return new String(bytes);
    }


    // these wrapper methods are to avoid having to handle exceptions where calling

    public static String getEncryptedText(String clearText, String plainKey) {
        if (TextUtils.isEmpty(clearText) || TextUtils.isEmpty(plainKey)) {
            return "One or both text values are empty";
        }

        Log.d(MainActivity.TAG, "clearText: " + clearText + " plainKey: " + plainKey);

        String encryptedText = "Unknown";
        mReceivedKey = plainKey;

        try {
            encryptedText = encrypt(clearText);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Exception occurred while encrypting text");
            Log.d(MainActivity.TAG, ex.getLocalizedMessage());
            encryptedText = ex.getLocalizedMessage();
            ex.printStackTrace();
        } finally {
            return encryptedText;
        }
    }

    public static String getDecryptedText(String encryptedText, String plainKey) {
        if (TextUtils.isEmpty(encryptedText) || TextUtils.isEmpty(plainKey)) {
            return "One or both text values are empty";
        }

        Log.d(MainActivity.TAG, "encryptedText: " + encryptedText + " plainKey: " + plainKey);

        String decryptedText = "Unknown";
        mReceivedKey = plainKey;

        try {
            decryptedText = decrypt(encryptedText);
        } catch (Exception ex) {
            Log.i(MainActivity.TAG, "Exception occurred while encrypting text");
            Log.d(MainActivity.TAG, ex.getLocalizedMessage());
            decryptedText = ex.getLocalizedMessage();
            ex.printStackTrace();
        } finally {
            return decryptedText;
        }
    }
}
