package com.magic.ssh.entity;
import javax.validation.constraints.*;

import lombok.Data;

@Data
public class Action {

    @NotNull(groups ={Update.class,Exec.class})
    @Min(value = 1, groups ={Update.class,Exec.class})
    private int actionId;

    @NotBlank(groups ={Update.class,Create.class})
    private String actionName;

    @NotBlank(groups ={Update.class,Create.class})
    @Null(groups = Exec.class)
    private String hostId;
    private String hostName;

    @NotBlank(groups ={Update.class,Create.class})
    @Null(groups = Exec.class)
    private String cmd;

    @Min(1)
    @Max(100)
    private int maxSize = 5;

    @Min(1)
    @Max(300)
    private int maxSecond = 120;

    private String stopWords;
    private String checkProcess;

    private Integer step;


    // 更新分组
    public interface Update {}
    // 更新分组
    public interface Create {}

    public interface Exec {}
}
