package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.ErrorCode;
import com.example.bankcards.filter.UserCardSearchFilter;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Data
public class CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(CardService.class);

    public List<CardDto> getUserCardsByFilter(Long userId, UserCardSearchFilter filter) {

        int pageSize = filter.pageSize() != null
                ? filter.pageSize() : 5;

        int pageNumber = filter.pageNumber() != null
                ? filter.pageNumber() : 0;

        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        var userCards = cardRepository.findByUserIdAndByFilter(
                userId,
                filter.expiryDate(),
                filter.status(),
                filter.balance(),
                pageable
        );

        if (userCards.isEmpty()) {
            return List.of();
        }

        return userCards
                .map(cardMapper::fromCardToCardDto)
                .stream().toList();
    }

    public BigDecimal getCardBalance(Long userId, Long cardId) {
        var card = getCardById(cardId);

        if (!card.getUser().getId().equals(userId)) {
            throw new IllegalStateException(ErrorCode.E_CARD_BALANCE_ACCESS_DENIED.name());
        }

        return card.getBalance();
    }

    public void requestBlockCard(Long userId, Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow();

        if (!card.getUser().getId().equals(userId)) {
            throw new IllegalStateException(ErrorCode.E_CARD_BLOCK_REQUEST_ACCESS_DENIED.name());
        }

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new IllegalStateException(ErrorCode.E_CARD_ALREADY_BLOCKED.name());
        }

        if (card.getStatus() == CardStatus.BLOCK_REQUESTED) {
            throw new IllegalStateException(ErrorCode.E_CARD_BLOCK_REQUEST_ALREADY_SENT.name());
        }

        card.setStatus(CardStatus.BLOCK_REQUESTED);
        cardRepository.save(card);
    }

    @Transactional
    public void transferBetweenCards(Long sourceCardId, Long destinationCardId, BigDecimal amount) {
        var user = userService.getCurrentUser();
        var sourceCard = getCardById(user.id());
        var destinationCard = getCardById(user.id());

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(ErrorCode.INVALID_AMOUNT.name());
        }
        if (sourceCard.getStatus() != CardStatus.ACTIVE) {
            throw new IllegalArgumentException(ErrorCode.SOURCE_CARD_INACTIVE.name());
        }
        if (sourceCard.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(ErrorCode.INSUFFICIENT_BALANCE.name());
        }

        sourceCard.setBalance(sourceCard.getBalance().subtract(amount));
        destinationCard.setBalance(destinationCard.getBalance().add(amount));

        cardRepository.save(sourceCard);
        cardRepository.save(destinationCard);
    }

    private Card getCardById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.E_CARD_NOT_FOUND.name()));
    }
}
