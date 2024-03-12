package com.magic.ssh.config;

public class StatusConstants {

    public static final Integer ACTION_READY = 1;
    public static final Integer ACTION_RUNNING = 2;
    public static final Integer ACTION_ERROR = 3;
    public static final Integer ACTION_DONE = 9;

    public static final Integer TASK_READY = 1;
    public static final Integer TASK_RUNNING = 2;
    public static final Integer TASK_ERROR = 3;
    public static final Integer TASK_DONE = 9;

    public static final Integer EXIT_SUCCESS = 0;
    public static final Integer EXIT_SUCCESS_TAIL = -1;
    public static final Integer EXIT_COMMON_UNKNOWN_ERROR = 1;
    public static final Integer EXIT_ERROR_CMD = 2;
    public static final Integer EXIT_CANNOT_EXEC = 126;
    public static final Integer EXIT_NO_CMD = 127;

    public static final Integer CHECK_SUCCESS = 0;
    public static final Integer CHECK_ERROR = -1;

}
