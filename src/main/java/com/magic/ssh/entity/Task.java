package com.magic.ssh.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Task {
    @NotNull(groups = {Task.Update.class,Task.Exec.class})
    private Integer taskId;

    @NotBlank(groups = {Task.Update.class,Task.Create.class})
    private String taskName;

    @NotBlank(groups = {Task.Update.class, Task.Create.class})
    private String actionIds;

    private List<List<Action>> actionList;

    private String remark;

    // 更新分组
    public interface Update {}
    // 更新分组
    public interface Create {}

    public interface Exec {}
}
