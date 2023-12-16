package com.example.backend.security.user;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Name of the key.
     * Like "Matyas's key"
     */
    private String username;


    private String email;
    /*
     * Actual value of the api key "0imfnc8mVLWwsdAwJkr4Rx-Af50DDqtlx"
     */
    
    

    @JsonIgnore
    private String passwordhash;

    @JsonIgnore
    private String passwordsalt;

    /*
     * comma separated
     * ROLE_USER, ROLE_ADMIN
     */
    private String roles;
    
    public UserEntity() {
    }
    public UserEntity(String username, String email, String passwordhash, String passwordsalt, String roles) {
        this.username = username;
        this.email = email;
        this.passwordhash = passwordhash;
        this.passwordsalt = passwordsalt;
        this.roles = roles;
    }
    public String getRolesStr() {
        return this.roles;
    }
    public List<GrantedAuthority> getRoles() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(this.roles);
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public void setRoles(List<GrantedAuthority>  roles) {

        String str_roles = "";
        for (var role : roles){
            if (str_roles.equals("")){
                str_roles += role.getAuthority();
            }else{
                str_roles += ","+role.getAuthority();
            }
        }

        this.roles = str_roles;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordhash() {
        return this.passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public String getPasswordsalt() {
        return this.passwordsalt;
    }

    public void setPasswordsalt(String passwordsalt) {
        this.passwordsalt = passwordsalt;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
