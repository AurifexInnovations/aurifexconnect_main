package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "token_black_list", schema = "public")
public class TokenBlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String token;

    private long expiration;
}
