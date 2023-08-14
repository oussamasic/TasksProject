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

package com.management.task.security;

import com.management.task.converter.UserConverter;
import com.management.task.dto.User;
import com.management.task.exceptions.UnAuthorizedException;
import com.management.task.model.TokenModel;
import com.management.task.model.UserModel;
import com.management.task.repository.TokenRepository;
import com.management.task.repository.UserRepository;
import com.management.task.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final String UNAUTHORIZED_REQUEST = "You are not authorized to make this request";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, UnAuthorizedException {

        String authHeader = request.getHeader("Authorization");

        String token = null;
        String userEmail = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userEmail = jwtService.extractUsername(token);
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserModel userDetails = userRepository.findByEmail(userEmail)
                    .orElseThrow(()->
                            new UnAuthorizedException(UNAUTHORIZED_REQUEST));

            Optional<TokenModel> optionalTokenModel = tokenRepository
                    .findByUserRefIdAndJwtToken(userDetails.getId(), token);

            if(optionalTokenModel.isEmpty()) {
                LOGGER.error(UNAUTHORIZED_REQUEST);
                throw new UnAuthorizedException(UNAUTHORIZED_REQUEST);
            }

            User userDto = UserConverter.convertUserModelToUserDto(userDetails);
            userDto.setPassword(null);

            if (jwtService.isTokenValid(token, userDto)) {
                optionalTokenModel.get().setUpdatedDate(new Date());
                tokenRepository.save(optionalTokenModel.get());

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDto, null,
                                new ArrayList<>());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
