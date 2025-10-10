package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("""
        select c from Card c
        where (:userId is null or c.user.id = :userId)
        and (:expiryDate is null or c.expiryDate = :expiryDate)
        and (:status is null or c.status = :status)
        and (:balance is null or c.balance > :balance)
    """)
    Page<Card> findByUserIdAndByFilter(
            @Param("userId") Long userId,
            @Param("expiryDate") LocalDate expiryDate,
            @Param("status") CardStatus status,
            @Param("balance") BigDecimal balance,
            Pageable pageable
    );

    List<Card> findAllByStatus(CardStatus status);

    @Query("""
        select c from Card c
        where (:expiryDate is null or c.expiryDate = :expiryDate)
        and (:userId is null or c.user.id = :userId)
        and (:status is null or c.status = :status)
        and (:balance is null or c.balance > :balance)
    """)
    Page<Card> findAllByFilter(
            @Param("expiryDate") LocalDate expiryDate,
            @Param("userId") Long userId,
            @Param("status") CardStatus status,
            @Param("balance") BigDecimal balance,
            Pageable pageable
    );

    Optional<Card> findByIdAndUserId(Long id, Long userId);
}
