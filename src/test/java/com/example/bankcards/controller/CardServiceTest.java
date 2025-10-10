package com.example.bankcards.controller;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.BaseMockServiceTest;
import com.example.bankcards.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CardServiceTest extends BaseMockServiceTest {
    @Autowired
    CardService cardService;

    @Test
    void testGetBalanceFromNotOwnedCard() {
        // preconditions
        var user = new User();
        user.setId(1L);

        var card = new Card();
        card.setId(1L);
        card.setUser(user);

        // when
        when(cardRepository.findById(any())).thenReturn(Optional.of(card));

        // verify
        assertThrows(IllegalStateException.class, () -> cardService.getCardBalance(2L, 1L));
    }
}
