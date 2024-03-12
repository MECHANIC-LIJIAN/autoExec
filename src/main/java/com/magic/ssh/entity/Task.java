package com.magic.ssh.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class Task {
    @NotNull(groups = {Task.Update.class,Task.Exec.class,Task.Delete.class})
    private Integer taskId;

    @NotBlank(groups = {Task.Update.class,Task.Create.class})
    private String taskName;

    @NotNull(groups = {Task.Update.class, Task.Create.class})
    private List<StepAction> stepData;

    private List<Action> actionList;

    private List<List<Action>> execList;

    private String remark;

    // 更新分组
    public interface Update {}
    // 更新分组
    public interface Create {}

    public interface Exec {}

    public interface Delete {}
}
