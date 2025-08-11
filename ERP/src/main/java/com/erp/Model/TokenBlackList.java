package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "token_black_list", schema = "public")
@Getter
@Setter
public class TokenBlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "token")
    private String token;

    @Column(name = "expiration")
    private long expiration;
}
