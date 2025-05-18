package com.project.staffguard.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Converter
public class SalaryEncryptor implements AttributeConverter<String, String> {
    // Must be 16, 24 or 32 bytes long. Here's a simple 32-byte (256-bit) example:
    private static final String SECRET_KEY = "0123456789abcdef0123456789abcdef";
    private static final String ALGORITHM  = "AES/ECB/PKCS5Padding";

    @Override
    public String convertToDatabaseColumn(String salary) {
        try {
            SecretKeySpec key   = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher          cph = Cipher.getInstance(ALGORITHM);
            cph.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cph.doFinal(salary.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String encryptedSalary) {
        try {
            SecretKeySpec key   = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher          cph = Cipher.getInstance(ALGORITHM);
            cph.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded   = Base64.getDecoder().decode(encryptedSalary);
            return new String(cph.doFinal(decoded));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
