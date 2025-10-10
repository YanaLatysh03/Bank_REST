package com.example.bankcards.service;

import com.example.bankcards.BaseMockServiceTest;
import com.example.bankcards.util.EncryptionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EncryptionUtilTest extends BaseMockServiceTest {
    @Autowired
    EncryptionUtil encryptionUtil;

    @Test
    void testEncryptionDecryption() {
        // preconditions
        var data = "card number";
        // when and then
        Assertions.assertEquals(encryptionUtil.decrypt(encryptionUtil.encrypt(data)), data);
    }
}
