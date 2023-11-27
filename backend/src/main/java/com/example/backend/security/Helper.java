package com.example.backend.security;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.backend.card.CardEntity;
import com.example.backend.deck.DeckEntity;
import com.example.backend.security.user.UserEntity;
import com.example.backend.trida.TridaEntity;

public class Helper {
    public static UserEntity getUserEntity(){
        return ((UserToken) SecurityContextHolder.getContext().getAuthentication()).getUserEntity();
    }


    public static boolean hasRightsForTrida(TridaEntity trida) {
        return (trida.getUserEntity().getId() == Helper.getUserEntity().getId());
    }


    public static boolean hasRightsForDeck(DeckEntity deck) {
        return (deck.getTridaEntity().getUserEntity().getId() == Helper.getUserEntity().getId());
    }

    public static boolean hasRightsForCard(CardEntity card) {
        return (card.getDeckEntity().getTridaEntity().getUserEntity().getId() == Helper.getUserEntity().getId());
    }
}
