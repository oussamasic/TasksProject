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

package com.management.task.threads;

import com.management.task.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BusinessApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessApplication.class);

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        LOGGER.info("Starting all scheduled operations");

        ApplicationContext applicationContext = event.getApplicationContext();
        JwtService jwtService = applicationContext.getBean(JwtService.class);

        new TokenManagementThreads(jwtService);

    }

}
