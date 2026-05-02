package com.example.ShopSpring.features.token;

import com.example.ShopSpring.features.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String token;//access token

    @Column(name="refresh_token")
    private String refreshToken;

    @Column(name="token_type",length = 50)
    private String tokenType;

    @Column(name = "expiration_date",length = 150)
    private LocalDateTime expirationDate;

    @Column(name = "refresh_expiration_date",length = 150)
    private LocalDateTime refreshExpirationDate;

    @Column(name = "is_mobile", columnDefinition = "TINYINT(1)")
    private boolean isMobile;

    private Boolean revoked;
    private Boolean expired;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;
}
