package com.example.bankcards.controller;

import com.example.bankcards.service.BaseMockServiceTest;
import com.example.bankcards.util.EncryptionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EncryptionUtilTest extends BaseMockServiceTest {
    @Autowired
    EncryptionUtil encryptionUtil;

    @Test
    void testEncryptionDecryption() {
        var data = "card number";
        Assertions.assertEquals(encryptionUtil.decrypt(encryptionUtil.encrypt(data)), data);
    }
}
