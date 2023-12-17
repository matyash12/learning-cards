package com.example.backend.recovery.recoveryTokens;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.backend.security.user.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="recoverytokens")
public class RecoveryTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity userEntity;
    
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date dateExpired;

    private boolean used = false; 
    
    public boolean isUsed() {
        return used;
    }



    public void setUsed(boolean used) {
        this.used = used;
    }



    public RecoveryTokenEntity(UserEntity userEntity, String token, Date dateCreated, Date dateExpired) {
        this.userEntity = userEntity;
        this.token = token;
        this.dateCreated = dateCreated;
        this.dateExpired = dateExpired;
    }

    

    public RecoveryTokenEntity() {
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity user) {
        this.userEntity = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public java.util.Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(java.util.Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public java.util.Date getDateExpired() {
        return dateExpired;
    }

    public void setDateExpired(java.util.Date dateExpired) {
        this.dateExpired = dateExpired;
    }

    
}
