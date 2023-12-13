package com.example.backend.card;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.backend.deck.DeckEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "cards")
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 65535)
    private String hiddenPart;

    @Column(length = 65535)
    private String visiblePart;

    private int mark;

    /* private long tableid;

    public long getTableid() {
        return this.tableid;
    }

    public void setTableid(long table) {
        this.tableid = table;
    } */
    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DeckEntity deckEntity;

    public DeckEntity getDeckEntity() {
        return this.deckEntity;
    }

    public void setDeckEntity(DeckEntity deckEntity) {
        this.deckEntity = deckEntity;
    }


    public int getMark() {
        return this.mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHiddenPart() {
        return this.hiddenPart;
    }

    public void setHiddenPart(String hiddenPart) {
        this.hiddenPart = hiddenPart;
    }

    public String getVisiblePart() {
        return this.visiblePart;
    }

    public void setVisiblePart(String visiblePart) {
        this.visiblePart = visiblePart;
    }
    // Getters and setters

}
