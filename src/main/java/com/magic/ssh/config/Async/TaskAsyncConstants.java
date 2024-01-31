package com.magic.ssh.config.Async;

import com.magic.ssh.entity.AsyncConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "task.async")
public class TaskAsyncConstants extends AsyncConstants {
}
