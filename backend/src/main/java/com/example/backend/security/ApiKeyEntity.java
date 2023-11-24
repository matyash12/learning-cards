package com.example.backend.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "apikey")
public class ApiKeyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Name of the key.
     * Like "Matyas's key"
     */
    private String name;
    /*
     * Actual value of the api key "0imfnc8mVLWwsdAwJkr4Rx-Af50DDqtlx"
     */
    private String value;

    /*
     * comma separated
     * ROLE_USER, ROLE_ADMIN
     */
    private String roles;

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

    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
