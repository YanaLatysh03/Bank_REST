package com.example.bankcards.service;

import com.example.bankcards.BaseMockServiceTest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CardServiceTest extends BaseMockServiceTest {
    @Autowired
    CardService cardService;

    @MockBean
    UserService userService;

    @Test
    void testGetBalanceFromNotOwnedCard() {
        // preconditions
        var user = new User();
        user.setId(1L);

        var card = new Card();
        card.setId(1L);
        card.setUser(user);

        when(cardRepository.findById(any())).thenReturn(Optional.of(card));

        // when and then
        assertThrows(IllegalStateException.class, () -> cardService.getCardBalance(2L, 1L));
    }

    @Test
    void testTransferNotAllowedForBlockedCard() {
        // preconditions
        var user = new UserDto(1L, "user@email.com", "", "", Role.ROLE_USER);

        var userEntity = new User();
        userEntity.setId(1L);

        var sourceCard = new Card();
        sourceCard.setId(1L);
        sourceCard.setUser(userEntity);
        sourceCard.setStatus(CardStatus.BLOCKED);
        sourceCard.setExpiryDate(LocalDate.now().plusYears(1));

        var destinationCard = new Card();
        destinationCard.setId(2L);
        destinationCard.setUser(userEntity);
        destinationCard.setStatus(CardStatus.BLOCKED);
        destinationCard.setBalance(BigDecimal.ZERO);
        destinationCard.setExpiryDate(LocalDate.now().plusYears(1));

        when(userService.getCurrentUser()).thenReturn(user);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(sourceCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(destinationCard));

        // when and then
        assertThrows(IllegalArgumentException.class, () -> cardService.transferBetweenCards(1L, 2L, BigDecimal.ONE));
    }

    @Test
    void testTransferNotAllowedForExpiredCard() {
        // preconditions
        var user = new UserDto(1L, "user@email.com", "", "", Role.ROLE_USER);

        var userEntity = new User();
        userEntity.setId(1L);

        var sourceCard = new Card();
        sourceCard.setId(1L);
        sourceCard.setUser(userEntity);
        sourceCard.setStatus(CardStatus.ACTIVE);
        sourceCard.setExpiryDate(LocalDate.now().minusYears(1));

        var destinationCard = new Card();
        destinationCard.setId(2L);
        destinationCard.setUser(userEntity);
        destinationCard.setBalance(BigDecimal.ZERO);
        destinationCard.setStatus(CardStatus.BLOCKED);
        destinationCard.setExpiryDate(LocalDate.now().plusYears(1));

        when(userService.getCurrentUser()).thenReturn(user);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(sourceCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(destinationCard));

        // when and then
        assertThrows(IllegalArgumentException.class, () -> cardService.transferBetweenCards(1L, 2L, BigDecimal.ONE));
    }

    @Test
    void testTransferNotAllowedForZeroAmount() {
        // preconditions
        var user = new UserDto(1L, "user@email.com", "", "", Role.ROLE_USER);

        var userEntity = new User();
        userEntity.setId(1L);

        var sourceCard = new Card();
        sourceCard.setId(1L);
        sourceCard.setUser(userEntity);
        sourceCard.setBalance(BigDecimal.ZERO);
        sourceCard.setStatus(CardStatus.ACTIVE);
        sourceCard.setExpiryDate(LocalDate.now().plusYears(1));

        var destinationCard = new Card();
        destinationCard.setId(2L);
        destinationCard.setUser(userEntity);
        destinationCard.setBalance(BigDecimal.ZERO);
        destinationCard.setStatus(CardStatus.ACTIVE);
        destinationCard.setExpiryDate(LocalDate.now().plusYears(1));

        when(userService.getCurrentUser()).thenReturn(user);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(sourceCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(destinationCard));

        // when and then
        assertThrows(IllegalArgumentException.class, () -> cardService.transferBetweenCards(1L, 2L, BigDecimal.ONE));
    }

    @Test
    void testTransferNotAllowedForAmountOverlappingBalance() {
        // preconditions
        var user = new UserDto(1L, "user@email.com", "", "", Role.ROLE_USER);

        var userEntity = new User();
        userEntity.setId(1L);

        var sourceCard = new Card();
        sourceCard.setId(1L);
        sourceCard.setUser(userEntity);
        sourceCard.setBalance(BigDecimal.ONE);
        sourceCard.setStatus(CardStatus.ACTIVE);
        sourceCard.setExpiryDate(LocalDate.now().plusYears(1));

        var destinationCard = new Card();
        destinationCard.setId(2L);
        destinationCard.setUser(userEntity);
        destinationCard.setBalance(BigDecimal.ZERO);
        destinationCard.setStatus(CardStatus.ACTIVE);
        destinationCard.setExpiryDate(LocalDate.now().plusYears(1));

        when(userService.getCurrentUser()).thenReturn(user);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(sourceCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(destinationCard));

        // when and then
        assertThrows(IllegalArgumentException.class, () -> cardService.transferBetweenCards(1L, 2L, BigDecimal.TEN));
    }

    @Test
    void testTransferNotAllowedForNotOwnedCards() {
        // preconditions
        var user = new UserDto(1L, "user@email.com", "", "", Role.ROLE_USER);

        var userEntity = new User();
        userEntity.setId(2L);

        var sourceCard = new Card();
        sourceCard.setId(1L);
        sourceCard.setUser(userEntity);
        sourceCard.setBalance(BigDecimal.ONE);
        sourceCard.setStatus(CardStatus.ACTIVE);
        sourceCard.setExpiryDate(LocalDate.now().plusYears(1));

        var destinationCard = new Card();
        destinationCard.setId(2L);
        destinationCard.setUser(userEntity);
        destinationCard.setBalance(BigDecimal.ZERO);
        destinationCard.setStatus(CardStatus.ACTIVE);
        destinationCard.setExpiryDate(LocalDate.now().plusYears(1));

        when(userService.getCurrentUser()).thenReturn(user);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(sourceCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(destinationCard));

        // when and then
        assertThrows(IllegalArgumentException.class, () -> cardService.transferBetweenCards(1L, 2L, BigDecimal.ONE));
    }

    @Test
    void testTransferSuccessful() {
        // preconditions
        var user = new UserDto(1L, "user@email.com", "", "", Role.ROLE_USER);

        var userEntity = new User();
        userEntity.setId(1L);

        var sourceCard = new Card();
        sourceCard.setId(1L);
        sourceCard.setUser(userEntity);
        sourceCard.setBalance(BigDecimal.ONE);
        sourceCard.setStatus(CardStatus.ACTIVE);
        sourceCard.setExpiryDate(LocalDate.now().plusYears(1));

        var destinationCard = new Card();
        destinationCard.setId(2L);
        destinationCard.setUser(userEntity);
        destinationCard.setBalance(BigDecimal.ZERO);
        destinationCard.setStatus(CardStatus.ACTIVE);
        destinationCard.setExpiryDate(LocalDate.now().plusYears(1));

        when(userService.getCurrentUser()).thenReturn(user);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(sourceCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(destinationCard));
        when(cardRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        cardService.transferBetweenCards(1L, 2L, BigDecimal.ONE);

        // verify
        verify(cardRepository, atLeastOnce()).save(argThat(card ->
                card.getId().equals(2L) && card.getBalance().compareTo(BigDecimal.ONE) == 0
        ));
    }
}
