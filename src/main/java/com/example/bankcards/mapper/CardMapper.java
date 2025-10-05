package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardMapper {
    @Mapping(target = "maskedNumber", expression = "java(\"**** **** **** \" + card.getLast4())")
    CardDto fromCardToCardDto(Card card);

}
