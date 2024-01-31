package com.magic.ssh.entity;

import lombok.Data;

@Data
public abstract class AsyncConstants {
    private int corePoolSize;
    private int maxPoolSize;
    private int keepAliveSeconds;
    private int queueCapacity;
}