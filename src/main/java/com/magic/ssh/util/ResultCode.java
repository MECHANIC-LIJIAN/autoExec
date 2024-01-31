package com.magic.ssh.util;

public enum ResultCode {

    /* 成功状态码 */
    SUCCESS(200, "成功"),
    ERROR(500, "系统错误"),
    OP_ERROR(400, "操作失败"),
    UNAUTHORIZED(401,"无权访问该资源"),

    /* 参数错误 */
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    SERVER_NOT_EXIST(1011,"远程主机不存在"),
    SERVER_NOT_OPEN(1012,"远程主机未打开"),
    UNKNOWN_ERROR(9999, "未知错误"),

    /* JSCH */
    JSCH_CONNECT_ERROR(1021, "ssh连接错误"),

    ACTION_NOT_EXIST(1031,"动作不存在"),


    TASK_NOT_EXIST(1041,"任务不存在"),
    TASK_EXEC_ERROR(1042,"任务执行失败"),
    TASK_EXEC_DONE(1043,"任务执行完毕"),



    /* 用户错误 2001-2999*/
    USER_NOTLOGGED_IN(2001, "用户未登录"),
    USER_LOGIN_ERROR(2002, "账号不存在或密码错误"),

    SYSTEM_ERROR(10000, "系统异常，请稍后重试");

    private Integer code;
    private String message;

    private ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }
    public String getMessage() {
        return this.message;
    }
}