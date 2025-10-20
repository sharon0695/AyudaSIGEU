package com.gestion.eventos.Security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

    private final Cache<String, Boolean> revokedTokens;

    public TokenBlacklistService(@Value("${jwt.expiration-millis:3600000}") long expirationMillis) {
        this.revokedTokens = CacheBuilder.newBuilder()
                .expireAfterWrite(expirationMillis, TimeUnit.MILLISECONDS)
                .build();
    }

    public void blacklist(String token) {
        revokedTokens.put(token, Boolean.TRUE);
    }

    public boolean isBlacklisted(String token) {
        return revokedTokens.getIfPresent(token) != null;
    }
}
