package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.AdminCardService;
import com.example.bankcards.BaseMockServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdminCardServiceTest extends BaseMockServiceTest {
    @Autowired
    AdminCardService adminCardService;

    @Test
    void testCreateCard() {
        // preconditions
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(cardRepository.save(any())).thenAnswer(returnsFirstArg());

        // when
        var cardDto = adminCardService.createNewCard(1L);

        // verify
        verify(cardRepository, times(1)).save(any());

        // then
        assertThat(cardDto.maskedNumber().matches("\\*\\*\\*\\* \\*\\*\\*\\* \\*\\*\\*\\* \\d\\d\\d\\d"));
        assertThat(cardDto.balance() == BigDecimal.ZERO);
        assertThat(cardDto.status() == CardStatus.ACTIVE);
    }

    @Test
    void testActivateAlreadyActiveCard() {
        // preconditions
        var card = new Card();
        card.setExpiryDate(LocalDate.now().plusDays(1));
        card.setStatus(CardStatus.ACTIVE);
        when(cardRepository.findById(any())).thenReturn(Optional.of(card));

        // when and then
        assertThrows(IllegalStateException.class, () -> adminCardService.activateCard(1L));
    }

    @Test
    void testActivateExpiredCard() {
        // preconditions
        var card = new Card();
        card.setStatus(CardStatus.BLOCKED);
        card.setExpiryDate(LocalDate.now().minusYears(1L));
        when(cardRepository.findById(any())).thenReturn(Optional.of(card));

        // when and then
        assertThrows(IllegalStateException.class, () -> adminCardService.activateCard(1L));
    }
}
