package com.example.ShopSpring.features.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="social_accounts")
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 20)
    private String provider;

    @Column(name="provider_id",nullable = false, length = 50)
    private String providerId;

    @Column(nullable = false,length = 150)
    private String name;

    @Column(nullable = false,length = 150)
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
