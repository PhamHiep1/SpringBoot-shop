package com.example.ShopSpring.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String token;

    @Column(name="token_type",length = 50)
    private String tokenType;

    @Column(name = "expiration_date",length = 150)
    private LocalDateTime expirationDate;

    private Boolean revoke;
    private Boolean expired;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User userId;
}
