package com.magic.ssh.config.Async;

import com.magic.ssh.entity.AsyncConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "exec.async")
public class ExecAsyncContents extends AsyncConstants {
}
