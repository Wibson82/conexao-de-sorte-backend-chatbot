package chatbot.infrastructure.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "J0g0D0B1ch0S3cur1tyK3y2024"; // 16 chars for AES-128
    
    public String encrypt(String data) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar dados", e);
        }
    }
    
    public String decrypt(String encryptedData) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar dados", e);
        }
    }
    
    public boolean isEncrypted(String data) {
        try {
            Base64.getDecoder().decode(data);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}