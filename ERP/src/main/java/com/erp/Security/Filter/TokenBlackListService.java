package com.erp.Security.Filter;

import com.erp.Model.TokenBlackList;
import com.erp.Repository.TokenBlackList.TokenBlackListRepository;
import com.erp.Security.JWT.JWTService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TokenBlackListService {

    private JWTService jwtService;
    private TokenBlackListRepository tokenBlackListRepository;

    public boolean isBlackListed(String token){
        return tokenBlackListRepository.existsByToken(token);
    }
//
//    public void blackListToken(String token){
//        Claims claims =  jwtService.parseToken(token);
//        long expiration = claims.getExpiration().getTime();
//        TokenBlackList tokenBlackList = new TokenBlackList();
//        tokenBlackList.setToken(token);
//        tokenBlackList.setExpiration(expiration);
//
//        tokenBlackListRepository.save(tokenBlackList);
//    }

    @Transactional
    public void blackListToken(String token, long expirationMillis) {
        TokenBlackList entry = new TokenBlackList();
        entry.setToken(token);
        entry.setExpiration(expirationMillis);
        // do NOT set id here — let the DB generate the bigint id
        tokenBlackListRepository.save(entry);
    }

    // keep existing signature if needed; derive expiration before calling above
    @Transactional
    public void blackListToken(String token) {
        long defaultExpiry = System.currentTimeMillis(); // adjust as needed
        blackListToken(token, defaultExpiry);
    }
}
