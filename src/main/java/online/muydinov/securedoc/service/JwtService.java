package online.muydinov.securedoc.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import online.muydinov.securedoc.domain.Token;
import online.muydinov.securedoc.domain.TokenData;
import online.muydinov.securedoc.dto.User;
import online.muydinov.securedoc.enumeration.TokenType;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public interface JwtService {
    String createToken(User user, Function<Token, String> tokenFunction);
    Optional<String> extractToken(HttpServletRequest request, String tokenType);
    void addCookie(HttpServletResponse response, User user, TokenType type);
    <T> T getTokenData(String token, Function<TokenData,T> tokenFunction);
}
