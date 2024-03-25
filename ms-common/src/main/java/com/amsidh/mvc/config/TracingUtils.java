package com.amsidh.mvc.config;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class TracingUtils {

    public static ExecutorService wrapExecutor(ExecutorService executor) {
        return Context.taskWrapping(executor);
    }

    public static CompletableFuture<Void> traceAsyncOperation(Context context, Runnable runnable) {
        return CompletableFuture.runAsync(() -> {
            try (Scope scope = context.makeCurrent()) {
                runnable.run();
            }
        });
    }
}
