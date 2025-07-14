package com.realmon.backend.utils;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

// Utility class to run code after a transaction commits
// This is useful for actions that should only happen if the transaction is successful, such as sending notifications or updating caches.
@Component
public class TransactionHelper {

    public void runAfterCommit(Runnable runnable) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                runnable.run();
            }
        });
    }
}
