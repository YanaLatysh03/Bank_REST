package com.example.bankcards.util;

import com.example.bankcards.entity.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtil {
    private final String ALGO = "AES";
    @Value("${security.encryption-key}")
    private String encryptionKey;

    public String encrypt(String data) {
        try {
            var key = encryptionKey.getBytes();
            Cipher cipher = Cipher.getInstance(ALGO);
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.ENCRYPTION_ERROR.name());
        }
    }

    public String decrypt(String encrypted) {
        try {
            var key = encryptionKey.getBytes();
            Cipher cipher = Cipher.getInstance(ALGO);
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGO);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.DECRYPTION_ERROR.name());
        }
    }
}
