/*
 * <OZ TASKS>
 * <project to manage user tasks>
 * Copyright (C) <2023>  <ZEROUALI Oussama>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.management.task.service;

import com.management.task.dto.TokenType;
import com.management.task.dto.User;
import com.management.task.exceptions.BadRequestException;
import com.management.task.exceptions.NotFoundException;
import com.management.task.model.TokenModel;
import com.management.task.model.UserModel;
import com.management.task.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    private final TokenRepository tokenRepository;

    private static final String SECRET_KEY = "77397A244326462948404D635166546A576E5A7234753778214125442A472D4B";

    @Autowired
    public JwtService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String extractUsername(String token) {
        LOGGER.debug("Extract useName fom a JWT Token");
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserModel userDetails) {
        LOGGER.debug("Generate new JWT Token for the user with email : {}", userDetails.getEmail());
        return generateToken(Map.of(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserModel userDetails) {
        LOGGER.debug("Generate new JWT Token");
        String tokenString = Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 60 minutes / 1 hour
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        TokenModel token = new TokenModel();
        token.setUserRefId(userDetails.getId());
        token.setJwtToken(tokenString);
        token.setUpdatedDate(new Date());
        token.setTokenType(TokenType.BEARER);
        tokenRepository.save(token);

        return tokenString;
    }

    public boolean isTokenValid(String token, User userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        LOGGER.debug("Check JWT Token expiration option");
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void updateUserToken(String jwtToken, String userId) {
        LOGGER.debug("Update the user Token");
        if(Objects.isNull(jwtToken)) {
            LOGGER.error("the user token should not be null");
            throw new BadRequestException("the user token should not be null");
        }

        TokenModel tokenModel = tokenRepository.
                findByUserRefIdAndJwtToken(userId, jwtToken).orElseThrow(
                        ()-> new NotFoundException("No Token found with the given information"));

        tokenModel.setUpdatedDate(new Date());
        tokenRepository.save(tokenModel);
    }

    public void deleteUserToken(String id) {
        LOGGER.debug("Delete the user Token");
        Optional<TokenModel> tokenModelOptional = tokenRepository.findById(id);
        if(tokenModelOptional.isEmpty()) {
            LOGGER.error("No token found with this id : {} ", id);
            throw new NotFoundException("No token found with this id : "+ id);
        }
        tokenRepository.deleteById(id);
    }

    public TokenModel getTokenById(String id) {
        LOGGER.debug("Get a Token by Id");
        return tokenRepository.findById(id).orElse(null);
    }

    public List<TokenModel> getAllTokens(){
        LOGGER.debug("get all the tokens from the database");
        return tokenRepository.findAll();
    }
    public List<TokenModel> getAllUserToken(String userRefId){
        LOGGER.debug("get all the tokens of an user from the database");
        return tokenRepository.findByUserRefId(userRefId);
    }

    public void deleteAllToken(){
        LOGGER.debug("delete all the tokens in the database");
        tokenRepository.deleteAll();
    }

    public void deleteAllUserToken(String userRefId) {
        LOGGER.debug("delete all the tokens of an user in the database");
        tokenRepository.deleteByUserRefId(userRefId);

    }

    public void logout(String jwtToken) {
        LOGGER.debug("Process : user logout");

        if(jwtToken == null) {
            LOGGER.error("invalid token");
            throw new BadRequestException("invalid token");
        }
        final Optional<TokenModel> optToken = tokenRepository.findByJwtToken(jwtToken);

        if (optToken.isEmpty()) {
            LOGGER.error("invalid request");
            throw new BadRequestException("invalid request");
        }
        tokenRepository.deleteById(optToken.get().getId());
        SecurityContextHolder.clearContext();
    }

}
