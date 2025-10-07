package com.example.bankcards.controller;

import com.example.bankcards.filter.AdminCardSearchFilter;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.rs.CardInfoRs;
import com.example.bankcards.rs.SuccessfulRs;
import com.example.bankcards.service.AdminCardService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/api/admin/cards")
@Data
public class AdminCardController {
    private final AdminCardService adminCardService;
    private final CardMapper cardMapper;

    private final Logger log = LoggerFactory.getLogger(AdminCardController.class);

    @GetMapping("/")
    public ResponseEntity<List<CardInfoRs>> getAllCards(
            @RequestParam(name = "expiryDate", required = false) LocalDate expiryDate,
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber
    ) {
        log.info("Called getAllCards");
        var filter = new AdminCardSearchFilter(
                expiryDate,
                userId,
                pageSize,
                pageNumber
        );
        var cards = adminCardService.getAllCardsByFilter(filter);
        return ResponseEntity.ok(cards.stream().map(
                cardMapper::fromCardDtoToCardInfoRs
        ).collect(Collectors.toList()));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<CardInfoRs> createNewCard(
            @PathVariable Long userId
    ) {
        log.info("Called createNewCard, userId = " + userId);
        var newCard = adminCardService.createNewCard(userId);
        return ResponseEntity.ok(cardMapper.fromCardDtoToCardInfoRs(newCard));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<SuccessfulRs> deleteCard(
            @PathVariable Long cardId
    ) {
        log.info("Called deleteCard, cardId = " + cardId);
        adminCardService.deleteCard(cardId);
        return ResponseEntity.ok(new SuccessfulRs("Карта успешно удалена"));
    }

    @PostMapping("/{cardId}/approve-block")
    public ResponseEntity<SuccessfulRs> approveBlock(
            @PathVariable Long cardId
    ) {
        log.info("Called approveBlock, cardId = " + cardId);
        adminCardService.approveBlockRequest(cardId);
        return ResponseEntity.ok(new SuccessfulRs("Блокировка карты одобрена"));
    }

    @PostMapping("/{cardId}/reject-block")
    public ResponseEntity<SuccessfulRs> rejectBlock(
            @PathVariable Long cardId
    ) {
        log.info("Called rejectBlock, cardId = " + cardId);
        adminCardService.rejectBlockRequest(cardId);
        return ResponseEntity.ok(new SuccessfulRs("Блокировка карты отклонена"));
    }

    @GetMapping("/block-requested")
    public ResponseEntity<List<CardInfoRs>> getBlockRequestedCards() {
        log.info("Called getBlockRequestedCards");
        var blockRequestedCards = adminCardService.getBlockRequestedCards();
        return ResponseEntity.ok(
              blockRequestedCards.stream().map(
                      cardMapper::fromCardDtoToCardInfoRs
              ).collect(Collectors.toList())
        );
    }

    @PostMapping("/{cardId}/activate")
    public ResponseEntity<SuccessfulRs> activateCard(
            @PathVariable Long cardId
    ) {
        log.info("Called activateCard, cardId = " + cardId);
        adminCardService.activateCard(cardId);
        return ResponseEntity.ok(new SuccessfulRs("Карта успешно активирована"));
    }

    @PostMapping("/{cardId}/block")
    public ResponseEntity<SuccessfulRs> blockCard(
            @PathVariable Long cardId
    ) {
        log.info("Called blockCard, cardId = " + cardId);
        adminCardService.blockCard(cardId);
        return ResponseEntity.ok(new SuccessfulRs("Карта успешно заблокирована"));
    }
}
