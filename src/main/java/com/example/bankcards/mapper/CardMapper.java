package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.rs.CardInfoRs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardMapper {
    @Mapping(target = "maskedNumber", expression = "java(\"**** **** **** \" + card.getLast4())")
    @Mapping(target = "user_id", expression = "java(card.getUser().getId())")
    CardDto fromCardToCardDto(Card card);

    CardInfoRs fromCardDtoToCardInfoRs(CardDto card);
}
