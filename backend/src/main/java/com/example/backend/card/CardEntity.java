package com.example.backend.card;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.backend.AppVariables;
import com.example.backend.ImageUrlGenerator;
import com.example.backend.deck.DeckEntity;
import com.example.backend.images.ImageEntity;
import com.example.backend.images.ImageRepository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
@Table(name = "cards")
public class CardEntity {
    
    
    public CardEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hiddenPart;
    private String visiblePart;

    private int mark;
    

    public CardEntity(String hiddenPart, String visiblePart, int mark,  DeckEntity deckEntity) {
        this.hiddenPart = hiddenPart;
        this.visiblePart = visiblePart;
        this.mark = mark;
        this.deckEntity = deckEntity;
    }
    
    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DeckEntity deckEntity;
    
    /**
     *
     */
    

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
