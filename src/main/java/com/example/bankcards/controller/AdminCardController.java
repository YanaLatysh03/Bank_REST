package com.example.bankcards.controller;

import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.SuccessCode;
import com.example.bankcards.filter.AdminCardSearchFilter;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.rq.CreateNewCardRq;
import com.example.bankcards.rs.CardInfoRs;
import com.example.bankcards.rs.SuccessfulRs;
import com.example.bankcards.service.AdminCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/api/admin/cards")
@Data
@Tag(name = "Управление картами (администратор)")
public class AdminCardController {
    private final AdminCardService adminCardService;
    private final CardMapper cardMapper;

    private final Logger log = LoggerFactory.getLogger(AdminCardController.class);

    @GetMapping("/")
    @Operation(summary = "Получение всех карточек по фильтру и с пагинацией")
    public ResponseEntity<List<CardInfoRs>> getAllCards(
            @RequestParam(name = "expiryDate", required = false) LocalDate expiryDate,
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "status", required = false) CardStatus status,
            @RequestParam(name = "balance", required = false) BigDecimal balance,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber
    ) {
        log.info("Called getAllCards");
        var filter = new AdminCardSearchFilter(
                expiryDate,
                userId,
                status,
                balance,
                pageSize,
                pageNumber
        );
        var cards = adminCardService.getAllCardsByFilter(filter);
        return ResponseEntity.ok(cards.stream().map(
                cardMapper::fromCardDtoToCardInfoRs
        ).collect(Collectors.toList()));
    }

    @PostMapping("/")
    @Operation(summary = "Создание новой карточки")
    public ResponseEntity<CardInfoRs> createNewCard(
            @RequestBody CreateNewCardRq rq
            ) {
        log.info("Called createNewCard, userId = " + rq.userId());
        var newCard = adminCardService.createNewCard(rq.userId());
        return ResponseEntity.ok(cardMapper.fromCardDtoToCardInfoRs(newCard));
    }

    @DeleteMapping("/{cardId}")
    @Operation(summary = "Удаление карточки")
    public ResponseEntity<SuccessfulRs> deleteCard(
            @PathVariable Long cardId
    ) {
        log.info("Called deleteCard, cardId = " + cardId);
        adminCardService.deleteCard(cardId);
        return ResponseEntity.ok(new SuccessfulRs(SuccessCode.CARD_DELETED_SUCCESS.name()));
    }

    @PostMapping("/{cardId}/approve-block")
    @Operation(summary = "Подтверждение запроса на блокировку карточки")
    public ResponseEntity<SuccessfulRs> approveBlock(
            @PathVariable Long cardId
    ) {
        log.info("Called approveBlock, cardId = " + cardId);
        adminCardService.approveBlockRequest(cardId);
        return ResponseEntity.ok(new SuccessfulRs(SuccessCode.CARD_BLOCK_APPROVED.name()));
    }

    @PostMapping("/{cardId}/reject-block")
    @Operation(summary = "Отклонение запроса на блокировку карточки")
    public ResponseEntity<SuccessfulRs> rejectBlock(
            @PathVariable Long cardId
    ) {
        log.info("Called rejectBlock, cardId = " + cardId);
        adminCardService.rejectBlockRequest(cardId);
        return ResponseEntity.ok(new SuccessfulRs(SuccessCode.CARD_BLOCK_REJECTED.name()));
    }

    @GetMapping("/block-requested")
    @Operation(summary = "Получение всех карточек с запросом на блокировку")
    public ResponseEntity<List<CardInfoRs>> getBlockRequestedCards() {
        log.info("Called getBlockRequestedCards");
        var blockRequestedCards = adminCardService.getCardsRequestedToBlock();
        return ResponseEntity.ok(
              blockRequestedCards.stream().map(
                      cardMapper::fromCardDtoToCardInfoRs
              ).collect(Collectors.toList())
        );
    }

    @PostMapping("/{cardId}/activate")
    @Operation(summary = "Активирование карточки")
    public ResponseEntity<SuccessfulRs> activateCard(
            @PathVariable Long cardId
    ) {
        log.info("Called activateCard, cardId = " + cardId);
        adminCardService.activateCard(cardId);
        return ResponseEntity.ok(new SuccessfulRs(SuccessCode.CARD_ACTIVATION_SUCCESS.name()));
    }

    @PostMapping("/{cardId}/block")
    @Operation(summary = "Блокировка карточки")
    public ResponseEntity<SuccessfulRs> blockCard(
            @PathVariable Long cardId
    ) {
        log.info("Called blockCard, cardId = " + cardId);
        adminCardService.blockCard(cardId);
        return ResponseEntity.ok(new SuccessfulRs(SuccessCode.CARD_BLOCK_SUCCESS.name()));
    }
}
