package com.example.ShopSpring.features.token;

import com.example.ShopSpring.features.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenRepository extends JpaRepository<Token,Long> {
    List<Token> findByUser(User user);
    Token findByToken(String token);
    Token findByRefreshToken(String refreshToken);

    @Modifying
    @Query("DELETE FROM Token t WHERE t.refreshExpirationDate < :date")
    int deleteByRefreshExpirationDateBefore(LocalDateTime date);
}
