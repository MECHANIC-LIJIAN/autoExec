package com.magic.ssh.config.Async;

import com.magic.ssh.entity.AsyncConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务，
 * 当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
 * 当队列满了，就继续创建线程，当线程数量大于等于maxPoolSize后，开始使用拒绝策略拒绝
 *
 * @author Jensen Zhan
 */
@Configuration
@EnableAsync
public class ThreadPoolTaskConfig{
    private final TaskAsyncConstants taskAsyncConstants;
    private final ExecAsyncContents execAsyncContents;

    @Autowired
    public ThreadPoolTaskConfig(TaskAsyncConstants initAsyncConstants, ExecAsyncContents execAsyncContents) {
        this.taskAsyncConstants = initAsyncConstants;
        this.execAsyncContents = execAsyncContents;
    }

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor  taskExecutor() {
        return this.taskExecutor(taskAsyncConstants, "Task-");
    }

    @Bean(name = "execExecutor")
    public ThreadPoolTaskExecutor  execExecutor() {
        return this.taskExecutor(execAsyncContents, "Exec-");
    }

    public ThreadPoolTaskExecutor taskExecutor(AsyncConstants asyncConstants, String prefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncConstants.getCorePoolSize());
        executor.setMaxPoolSize(asyncConstants.getMaxPoolSize());
        executor.setKeepAliveSeconds(asyncConstants.getKeepAliveSeconds());
        executor.setQueueCapacity(asyncConstants.getQueueCapacity());
        executor.setThreadNamePrefix(prefix);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}