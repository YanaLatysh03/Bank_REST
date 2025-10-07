package com.example.bankcards.controller;

import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.filter.UserCardSearchFilter;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.rs.CardInfoRs;
import com.example.bankcards.rs.SuccessfulRs;
import com.example.bankcards.service.UserCardService;
import com.example.bankcards.service.UserService;
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
@RequestMapping("/v1/api/users/cards")
@Data
public class UserCardController {
    private final UserCardService userCardService;
    private final UserService userService;
    private final CardMapper cardMapper;

    private final Logger log = LoggerFactory.getLogger(UserCardController.class);

    @GetMapping("/")
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

        var cards = userCardService.getUserCardsByFilter(currentUser.id(), filter);
        return ResponseEntity.ok(
                cards.stream().map(cardMapper::fromCardDtoToCardInfoRs).collect(Collectors.toList())
        );
    }

    @GetMapping("/{cardId}/balance")
    public ResponseEntity<BigDecimal> getBalance(
            @PathVariable Long cardId
    ) {
        var currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(userCardService.getCardBalance(currentUser.id(), cardId));
    }

    @GetMapping("/{cardId}/request-block")
    public ResponseEntity<SuccessfulRs> requestBlock(
            @PathVariable Long cardId
    ) {
        var currentUser = userService.getCurrentUser();
        userCardService.requestBlockCard(currentUser.id(), cardId);
        return ResponseEntity.ok(new SuccessfulRs("Запрос на блокировку карты успешно отправлен"));
    }
}
