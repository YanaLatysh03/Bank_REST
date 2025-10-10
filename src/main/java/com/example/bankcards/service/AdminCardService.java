package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.ErrorCode;
import com.example.bankcards.filter.AdminCardSearchFilter;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardNumberGeneratorUtil;
import com.example.bankcards.util.EncryptionUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class AdminCardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;
    private final EncryptionUtil encryptionUtil;

    private final Logger log = LoggerFactory.getLogger(AdminCardService.class);

    @Value("${card-number-details.bank-bin}")
    private String bin;
    @Value("${card-number-details.number-length}")
    private int numberLength;

    public CardDto createNewCard(Long userId) {
        log.info("Called createNewCard: userId = " + userId);
        var cardNumber = CardNumberGeneratorUtil.generate(bin, numberLength);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.E_USER_NOT_FOUND.name()));

        Card card = new Card();
        card.setEncryptedNumber(encryptionUtil.encrypt(cardNumber));
        card.setLast4(cardNumber.substring(cardNumber.length() - 4));
        card.setUser(user);
        card.setExpiryDate(LocalDate.now().plusYears(3));
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(BigDecimal.ZERO);

        var newCard = cardRepository.save(card);
        return cardMapper.fromCardToCardDto(newCard);
    }

    public void deleteCard(Long cardId) {
        log.info("Called deleteCard: cardId = " + cardId);
        Card card = getCardById(cardId);
        cardRepository.delete(card);
    }

    public void blockCard(Long cardId) {
        log.info("Called blockCard: cardId = " + cardId);
        Card card = getCardById(cardId);

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new IllegalStateException(ErrorCode.E_CARD_ALREADY_BLOCKED.name());
        }

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    public void activateCard(Long cardId) {
        log.info("Called activateCard: cardId = " + cardId);
        Card card = getCardById(cardId);

        if (card.getStatus() == CardStatus.ACTIVE) {
            throw new IllegalStateException(ErrorCode.E_CARD_ALREADY_ACTIVE.name());
        }

        if (card.getExpiryDate().isAfter(LocalDate.now())) {
            card.setStatus(CardStatus.ACTIVE);
            cardRepository.save(card);
        }
        else {
            log.info("Can't activate card: expiry date = " + card.getExpiryDate());
            throw new IllegalStateException(ErrorCode.E_CARD_EXPIRED.name());
        }
    }

    public List<CardDto> getAllCardsByFilter(AdminCardSearchFilter filter) {
        log.info("Called getAllCards");

        int pageSize = filter.pageSize() != null
                ? filter.pageSize() : 10;

        int pageNumber = filter.pageNumber() != null
                ? filter.pageNumber() : 0;

        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        var cards = cardRepository.findAllByFilter(
                filter.expiryDate(),
                filter.userId(),
                filter.status(),
                filter.balance(),
                pageable
        );
        return cards.stream().map(cardMapper::fromCardToCardDto).collect(Collectors.toList());
    }

    public void approveBlockRequest(Long cardId) {
        log.info("Called approveBlockRequest: cardId = " + cardId);
        Card card = getCardById(cardId);

        if (card.getStatus() != CardStatus.BLOCK_REQUESTED) {
            throw new IllegalStateException(ErrorCode.E_CARD_NOT_IN_BLOCK_REQUEST_STATUS.name());
        }

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    public void rejectBlockRequest(Long cardId) {
        log.info("Called rejectBlockRequest: cardId = " + cardId);
        Card card = getCardById(cardId);

        if (card.getStatus() != CardStatus.BLOCK_REQUESTED) {
            throw new IllegalStateException(ErrorCode.E_CARD_NOT_IN_BLOCK_REQUEST_STATUS.name());
        }

        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
    }

    public List<CardDto> getCardsRequestedToBlock() {
        log.info("Called getCardsRequestedToBlock");
        var cards = cardRepository.findAllByStatus(CardStatus.BLOCK_REQUESTED);
        return cards.stream().map(
                cardMapper::fromCardToCardDto
        ).collect(Collectors.toList());
    }

    private Card getCardById(Long cardId) {
        log.info("Called getCardById: cardId = " + cardId);
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.E_CARD_NOT_FOUND.name()));
    }
}
