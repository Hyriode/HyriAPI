package fr.hyriode.hyriapi.impl.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 18/07/2021 at 18:58
 */
public class ThreadPool {

    public static final ExecutorService ASYNC_EXECUTOR = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("HyriAPI Async %1$d").build());
    public static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(32);

}
