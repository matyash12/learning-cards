package com.example.backend.images;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.example.backend.card.CardEntity;
import com.example.backend.security.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@Entity
@Table(name = "images")
public class ImageEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "card_entity_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //TODO add delete in minio db
    private CardEntity cardEntity;

    
    private int position;


    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CardEntity getCardEntity() {
        return this.cardEntity;
    }

    public void setCardEntity(CardEntity cardEntity) {
        this.cardEntity = cardEntity;
    }

    public ImageEntity(String name, CardEntity cardEntity, int position) {
        this.name = name;
        this.cardEntity = cardEntity;
        this.position = position;
    }

    public long getId() {
        return this.id;
    }

    public ImageEntity() {
    }


    public void setId(long id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
