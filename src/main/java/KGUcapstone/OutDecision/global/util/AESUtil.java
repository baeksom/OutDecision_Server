package KGUcapstone.OutDecision.global.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {

    // AES 암호화 메서드
    public static String encrypt(String key, String plainText) throws Exception {
        // AES 암호화 알고리즘
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // 시크릿 키 생성
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        // 암호화 모드 설정
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        // 암호화 진행
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        // Base64 인코딩하여 반환
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // AES 복호화 메서드
    public static String decrypt(String encryptedText, String key) throws Exception {
        // AES 암호화 알고리즘
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // 시크릿 키 생성
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        // 복호화 모드 설정
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        // Base64 디코딩 후 복호화 진행
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        // 복호화된 문자열 반환
        return new String(decryptedBytes);
    }
}
