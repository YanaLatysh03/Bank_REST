package com.example.bankcards.controller;

import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.SuccessCode;
import com.example.bankcards.search.UserCardSearchFilter;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.rq.TransferRq;
import com.example.bankcards.rs.CardInfoRs;
import com.example.bankcards.rs.SuccessfulRs;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/v1/api/cards")
@Data
@Tag(name = "Карты пользователя")
public class CardController {
    private final CardService cardService;
    private final UserService userService;
    private final CardMapper cardMapper;

    private final Logger log = LoggerFactory.getLogger(CardController.class);

    @GetMapping("/")
    @Operation(summary = "Получение всех карточек пользователя по фильтру и с пагинацией",
            security = @SecurityRequirement(name = "BearerAuthentication"))
    public ResponseEntity<List<CardInfoRs>> getAllCards(
            @RequestParam(name = "expiryDate", required = false) LocalDate expiryDate,
            @RequestParam(name = "status", required = false) CardStatus status,
            @RequestParam(name = "balance", required = false) BigDecimal balance,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber
    ) {
        var currentUser = userService.getCurrentUser();

        var filter = new UserCardSearchFilter(
                expiryDate,
                status,
                balance,
                pageSize,
                pageNumber
        );

        var cards = cardService.getUserCardsByFilter(currentUser.id(), filter);
        return ResponseEntity.ok(
                cards.stream().map(cardMapper::fromCardDtoToCardInfoRs).collect(Collectors.toList())
        );
    }

    @GetMapping("/{cardId}/balance")
    @Operation(summary = "Получение баланса карточки пользователя",
            security = @SecurityRequirement(name = "BearerAuthentication"))
    public ResponseEntity<BigDecimal> getBalance(
            @PathVariable Long cardId
    ) {
        var currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(cardService.getCardBalance(currentUser.id(), cardId));
    }

    @GetMapping("/{cardId}/request-block")
    @Operation(summary = "Запрос на блокировку карточки пользователя",
            security = @SecurityRequirement(name = "BearerAuthentication"))
    public ResponseEntity<SuccessfulRs> requestBlock(
            @PathVariable Long cardId
    ) {
        var currentUser = userService.getCurrentUser();
        cardService.requestBlockCard(currentUser.id(), cardId);
        return ResponseEntity.ok(new SuccessfulRs(SuccessCode.CARD_BLOCK_REQUEST_SUCCESS.name()));
    }

    @PostMapping("/transfer")
    @Operation(summary = "Перевод денег между карточками пользователя",
            security = @SecurityRequirement(name = "BearerAuthentication"))
    public ResponseEntity<SuccessfulRs> transferBetweenCards(
            @RequestBody TransferRq request
    ) {
        cardService.transferBetweenCards(
                request.sourceCardId(),
                request.destinationCardId(),
                request.amount());

        return ResponseEntity.ok(new SuccessfulRs(SuccessCode.TRANSFER_SUCCESS.name()));
    }
}
