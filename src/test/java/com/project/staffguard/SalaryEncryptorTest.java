package com.project.staffguard;

import com.project.staffguard.config.SalaryEncryptor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SalaryEncryptorTest {
    private SalaryEncryptor encryptor = new SalaryEncryptor();

    @Test
    void encryptDecrypt_Success() {
        String original = "50000";
        String encrypted = encryptor.convertToDatabaseColumn(original);
        String decrypted = encryptor.convertToEntityAttribute(encrypted);

        assertEquals(original, decrypted);
    }
}