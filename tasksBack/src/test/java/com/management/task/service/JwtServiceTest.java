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
import com.management.task.exceptions.BadRequestException;
import com.management.task.exceptions.NotFoundException;
import com.management.task.model.TokenModel;
import com.management.task.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class JwtServiceTest {

    private TokenRepository tokenRepository;
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        tokenRepository = mock(TokenRepository.class);
        jwtService = new JwtService(tokenRepository);
    }

    @Test
    void testGetAllTokens() {
        jwtService.getAllTokens();
        verify(tokenRepository).findAll();
    }

    @Test
    void testGetAllUserTokens() {
        String userId = "userId";
        jwtService.getAllUserToken(userId);
        verify(tokenRepository).findByUserRefId(userId);
    }

/*

    @Test
    void testGetAllInCompleteTasks() {
        taskService.getAllInCompleteTasks();
        verify(taskRepository).findByComplete(false);
    }

    @Test
    void testGetAllCompleteTasks() {
        taskService.getAllCompleteTasks();
        verify(taskRepository).findByComplete(true);
    }*/

    @Test
    void testUpdateUserToken_ok_when_all_conditions_validated() {
        String jwtToken = "jwtToken";
        String userId = "userId";
        TokenModel tokenModel = new TokenModel("tokenId", new Date(), "userIdRef",
                TokenType.BEARER, "jwtToken" );
        when(tokenRepository.findByUserRefIdAndJwtToken(userId, jwtToken)).thenReturn(Optional.of(tokenModel));
        assertThatCode(() -> jwtService.updateUserToken(jwtToken, userId))
                .doesNotThrowAnyException();
        verify(tokenRepository, Mockito.atLeastOnce()).save(tokenModel);
    }

    @Test
    void testUpdateUserToken_ko_when_no_token_found_in_DB() {
        String jwtToken = "jwtToken";
        String userId = "userId";
        when(tokenRepository.findByUserRefIdAndJwtToken(userId, jwtToken)).thenReturn(Optional.empty());
        assertThatCode(() -> jwtService.updateUserToken(jwtToken, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No Token found with the given information");
    }

    @Test
    void testDeleteAllToken() {

        jwtService.deleteAllToken();
        verify(tokenRepository).deleteAll();
    }

    @Test
    void testUpdateUserToken_ko_when_token_not_provided() {
        String userId = "userId";

        assertThatCode(() -> jwtService.updateUserToken(null, userId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("the user token should not be null");
    }

    @Test
    void testDeleteUserToken_ok_when_all_conditions_validated() {

        String tokenId = "tokenId";
        TokenModel tokenModel = new TokenModel("tokenId", new Date(), "userIdRef",
                TokenType.BEARER, "jwtToken" );
        when(tokenRepository.findById(tokenId)).thenReturn(Optional.of(tokenModel));
        assertThatCode(() -> jwtService.deleteUserToken(tokenId))
                .doesNotThrowAnyException();
        verify(tokenRepository, Mockito.atLeastOnce()).deleteById(tokenId);
    }

    @Test
    void testGetTokenById() {
        String tokenId = "tokenId";
        jwtService.getTokenById(tokenId);
        verify(tokenRepository).findById(tokenId);
    }

    @Test
    void testDeleteUserToken_ko_when_no_token_found_in_DB() {
        String tokenId = "tokenId";
        when(tokenRepository.findById(tokenId)).thenReturn(Optional.empty());
        assertThatCode(() -> jwtService.deleteUserToken(tokenId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No token found with this id : "+ tokenId);
    }

    @Test
    void testDeleteByUserRefId() {
        String userId = "userId";
        jwtService.deleteAllUserToken(userId);
        verify(tokenRepository).deleteByUserRefId(userId);
    }

    @Test
    void testLogout_ok_when_all_conditions_validated() {
        String jwtToken = "jwtToken";
        String tokenId = "tokenId";

        TokenModel tokenModel = new TokenModel(tokenId, new Date(), "userIdRef",
                TokenType.BEARER, "jwtToken" );
        when(tokenRepository.findByJwtToken(jwtToken)).thenReturn(Optional.of(tokenModel));

        jwtService.logout(jwtToken);
        verify(tokenRepository).deleteById(tokenId);
    }

    @Test
    void testLogout_ko_when_no_token_found_in_DB() {
        String jwtToken = "jwtToken";
        String tokenId = "tokenId";

        when(tokenRepository.findByJwtToken(jwtToken)).thenReturn(Optional.empty());

        assertThatCode(() ->jwtService.logout(jwtToken))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("invalid request");
    }

    @Test
    void testLogout_ko_when_token_is_null() {

        assertThatCode(() ->jwtService.logout(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("invalid token");
    }

}
