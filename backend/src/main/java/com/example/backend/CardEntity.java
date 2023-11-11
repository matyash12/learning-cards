package com.example.backend;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "cards")
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hiddenPart;
    private String visiblePart;

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
