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

import com.management.task.exceptions.InternalServerException;
import com.management.task.model.TokenModel;
import com.management.task.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class TokenManagementThreads implements Runnable {

    private final JwtService jwtService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenManagementThreads.class);

    public TokenManagementThreads(JwtService jwtService) {
        this.jwtService = jwtService;
        Executors.newScheduledThreadPool(3)
                .scheduleAtFixedRate(this,
                60, 5, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        try {
            Thread.currentThread().setName(TokenManagementThreads.class.getName());
            LOGGER.debug("deleting all expired tokens");
            deleteExpiredToken();
        }
        catch (InternalServerException exception) {
            LOGGER.error("Error while deleting expired token from DataBase");
            throw new InternalServerException("Error while deleting expired token from DataBase", exception);
        }
    }

    private void deleteExpiredToken() throws InternalServerException {
        ExecutorService executorService = createScalableBatchExecutorService(3);
        try {
            List<CompletableFuture<Void>> completableFuturesList = new ArrayList<>();
            List<TokenModel> tokenList = jwtService.getAllTokens();
            if (tokenList.isEmpty()) {
                LOGGER.debug("No Token Found in the DataBase");
            }
            tokenList.forEach(token -> {
                if(isTokenToDelete(token, 60)) {
                    CompletableFuture<Void> traceabilityCompletableFuture = CompletableFuture.runAsync(() -> {
                            LOGGER.debug("deleting token with id : {} ", token.getId());
                            jwtService.deleteUserToken(token.getId());
                    }, executorService);
                    completableFuturesList.add(traceabilityCompletableFuture);
                }
            });

            CompletableFuture<Void> combinedFuture =
                    CompletableFuture.allOf(completableFuturesList.toArray(new CompletableFuture[0]));
            combinedFuture.get();


        }
        catch (InterruptedException exception) {
            LOGGER.error("Error when executing threads");
            Thread.currentThread().interrupt();
            throw new InternalServerException("Error when executing threads: " + exception);
        } catch (ExecutionException exception) {
            LOGGER.error("Error when executing threads");
            throw new InternalServerException("Error when executing threads: " + exception);
        } finally {
            LOGGER.debug("shutting dow the executor");
            executorService.shutdown();
        }

    }

    private boolean isTokenToDelete(TokenModel tokenModel, Integer delay ) {
        Date dateNow = new Date(System.currentTimeMillis());
        long differenceInMinutes = ((dateNow.getTime() - tokenModel.getUpdatedDate().getTime()) / (1000 * 60));
        return differenceInMinutes >= delay;
    }

    private static ThreadPoolExecutor createScalableBatchExecutorService(int maxBatchThreadPoolSize) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(maxBatchThreadPoolSize, maxBatchThreadPoolSize,
                5L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }
}
