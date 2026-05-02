package com.example.ShopSpring.features.token;

import com.example.ShopSpring.features.user.User;
import com.example.ShopSpring.security.jwt.JwtTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private static final int MAX_DEVICES = 2;
    private final TokenRepository tokenRepository;
    private final JwtTokenService jwtTokenService;

    @Value("${application.security.jwt.expiration}")
    private long tokenExpirationTime;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpirationTime;

    @Transactional
    @Override
    public Token addToken(User user, String token, boolean isMobileDevice) {
        List<Token> userTokens = tokenRepository.findByUser(user);

        // -- enforce max device limit
        enforceMaxDeviceLimit(userTokens);

        // -- add new token for user
        LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(tokenExpirationTime/1000);
        Token newToken = Token.builder()
                .user(user)
                .expirationDate(expirationDate)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .isMobile(isMobileDevice)
                .build();

        // -- add new refresh token for user
        LocalDateTime refreshExpirationDate = LocalDateTime.now().plusSeconds(refreshExpirationTime/1000);
        newToken.setRefreshExpirationDate(refreshExpirationDate);
        newToken.setRefreshToken(UUID.randomUUID().toString());

        return tokenRepository.save(newToken);
    }

    @Transactional
    @Override
    public Token refreshToken(String refreshToken, User user) {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);

        if(existingToken.getRevoked()){
            throw new RuntimeException("Security Violation: " +
                    "This refresh token has already been used. Please log in again.");
        }

        if(existingToken.getRefreshExpirationDate().isBefore(LocalDateTime.now())){
            existingToken.setExpired(true);
            tokenRepository.save(existingToken);
            throw new RuntimeException("Security Violation: " +
                    "This refresh token has expired. Please log in again.");
        }

        existingToken.setRevoked(true);
        existingToken.setExpired(true);
        tokenRepository.save(existingToken);

        List<Token> userTokens = tokenRepository.findByUser(user);

        // -- enforce max device limit
        enforceMaxDeviceLimit(userTokens);

        String newAccessToken = jwtTokenService.generateToken(user);
        String newRefreshToken = UUID.randomUUID().toString();

        Token newToken = Token.builder()
                .user(user)
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expirationDate(LocalDateTime.now().plusSeconds(tokenExpirationTime/1000))
                .refreshExpirationDate(LocalDateTime.now().plusSeconds(refreshExpirationTime/1000))
                .revoked(false)
                .expired(false)
                .isMobile(existingToken.isMobile())
                .build();

        return tokenRepository.save(newToken);
    }

    // -- if the number of tokens exceeds the limit; delete one old token.
    public void enforceMaxDeviceLimit(List<Token> userTokens) {
        if(userTokens.size() < MAX_DEVICES)
            return;

        boolean hasNonMobileToken = !userTokens.stream().allMatch(Token::isMobile);
        Token deletedToken;
        // -- priority to delete non-mobile token
        if(hasNonMobileToken){
            deletedToken =  userTokens.stream()
                    .filter(userToken -> !userToken.isMobile())
                    .findFirst()
                    .orElse(userTokens.getFirst());
        }
        else{
            deletedToken =  userTokens.getFirst();
        }
        tokenRepository.delete(deletedToken);

    }
}
