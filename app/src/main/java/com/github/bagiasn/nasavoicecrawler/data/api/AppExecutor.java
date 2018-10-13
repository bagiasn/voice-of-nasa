package com.github.bagiasn.nasavoicecrawler.data.api;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Executor class for handling background operations across the application.
 */

public class AppExecutor {

    private static final int NUM_OF_THREADS = 3;

    private final Executor backgroundIO;
    private final ScheduledExecutorService delayedIO;
    private final MainThreadExecutor mainThreadIO;

    private AppExecutor(Executor backgroundIO,
                        ScheduledExecutorService delayedIO,
                        MainThreadExecutor mainThreadIO) {
        this.backgroundIO = backgroundIO;
        this.delayedIO = delayedIO;
        this.mainThreadIO = mainThreadIO;
    }

    public AppExecutor() { this(Executors.newFixedThreadPool(NUM_OF_THREADS),
            Executors.newSingleThreadScheduledExecutor(),
            new MainThreadExecutor()); }

    public Executor getBackgroundIO() { return backgroundIO; }

    public Executor getMainThread() { return mainThreadIO; }

    public ScheduledExecutorService getDelayedIO() { return delayedIO; }


    private static class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}

