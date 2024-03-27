package com.magic.ssh.entity;
import javax.validation.constraints.*;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class Action {

    @NotNull(groups ={Update.class,Exec.class})
    @Min(value = 1, groups ={Update.class,Exec.class})
    private int actionId;

    @ExcelProperty("动作名")
    @NotBlank(groups ={Update.class,Create.class})
    private String actionName;

    @NotBlank(groups ={Update.class,Create.class})
    @Null(groups = Exec.class)
    private String hostId;

    @ExcelProperty("主机名")
    private String hostName;

    @ExcelProperty("命令内容")
    @NotBlank(groups ={Update.class,Create.class})
    @Null(groups = Exec.class)
    private String cmd;

    @ExcelProperty("最大输出行数")
    @Min(1)
    @Max(100)
    private int maxSize = 5;

    @ExcelProperty("最长等待时间")
    @Min(1)
    @Max(300)
    private int maxSecond = 120;

    @ExcelProperty("输出终止关键词")
    private String stopWords;

    @ExcelProperty("执行后检测进程")
    private String checkProcess;

    private Integer step;


    // 更新分组
    public interface Update {}
    // 更新分组
    public interface Create {}

    public interface Exec {}

    public boolean importChk() {
        return StringUtils.hasText(hostName) && StringUtils.hasText(actionName) && StringUtils.hasText(
                cmd);
    }
}
