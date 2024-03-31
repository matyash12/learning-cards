package com.example.backend.learn.SupportClasses;

import com.example.backend.card.CardEntity;

public class CardWithMarkCount {
    private final CardEntity card;
    private final MarkCount markCount;

    public CardWithMarkCount(CardEntity card, MarkCount markCount) {
        this.card = card;
        this.markCount = markCount;
    }

    public CardEntity getTheCard() {
        return card;
    }

    public MarkCount getMarkCount() {
        return markCount;
    }
}
