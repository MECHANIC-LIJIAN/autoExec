package com.magic.ssh.service.impl;

import com.magic.ssh.config.StatusConstants;
import com.magic.ssh.entity.*;
import com.magic.ssh.service.ActionLogService;
import com.magic.ssh.service.ActionService;
import com.magic.ssh.service.HostService;
import com.magic.ssh.service.SSHService;
import com.magic.ssh.util.ResultCode;
import com.magic.ssh.util.SSHUtil;
import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class SSHServiceImpl implements SSHService {

    private final HostService hostService;

    private final ActionService actionService;

    private final ActionLogService actionLogService;

    public SSHServiceImpl(HostService hostService, ActionService actionService, ActionLogService actionLogService) {
        this.hostService = hostService;
        this.actionService = actionService;
        this.actionLogService = actionLogService;
    }

    @Override
    public void close(String host) {
        SSHUtil.getInstance().close(host);
    }

    public Boolean syncConnect(Host userHost) {
        log.info("syncConnect {}", userHost.getHostId());
        try {
            SSHUtil.getInstance().init(userHost);
            return Boolean.TRUE;
        } catch (JSchException e) {
            log.error(e.getMessage());
            return Boolean.FALSE;
        }
    }


    @Override
    public ActionLog syncExecCmd(Action action) {

        ActionLog actionLog = new ActionLog();

        if (Objects.nonNull(action)) {
            log.debug("执行动作名 {}", action.getActionName());
        }

        log.debug("input: {}", action);
        Action queryAction;
        if (StringUtils.hasText(action.getHostId()) && StringUtils.hasText(action.getCmd())) {
            queryAction = action;
        } else {
            queryAction = actionService.getActionInfoById(action.getActionId());
        }
        if (Objects.isNull(queryAction)) {
            queryAction = new Action();
            queryAction.setActionId(0);
            buildErrLog(actionLog, ResultCode.ACTION_NOT_EXIST.getMessage());
            return actionLog;
        }
        log.debug("queryAction: {}", queryAction);

        Host queryHost = hostService.getHostInfoById(queryAction.getHostId());
        if (Objects.isNull(queryHost)) {
            buildErrLog(actionLog, ResultCode.SERVER_NOT_EXIST.getMessage());
            return actionLog;
        }
        log.debug("queryHost: {}", queryHost);

        actionLog.setActionId(queryAction.getActionId());

        String serverKey = queryAction.getHostId();
        try {
            if (SSHUtil.getInstance().getSession(serverKey) == null) {
                if (!syncConnect(queryHost)) {
                    buildErrLog(actionLog, ResultCode.JSCH_CONNECT_ERROR.getMessage());
                    return actionLog;
                }
            }
            ExecuteCommon executeCommon = getExecuteCommon(queryAction);
            ExecLog execLog = SSHUtil.getInstance()
                    .executeCommand(serverKey, queryAction.getCmd(), executeCommon);

            extractExecLog(actionLog, execLog);

            if (StringUtils.hasText(queryAction.getCheckProcess())) {
                String checkCmd = String.format("ps -aux |grep -v 'grep' | grep -i %s", queryAction.getCheckProcess());
                ExecLog checkLog = SSHUtil.getInstance()
                        .executeCommand(serverKey, checkCmd, executeCommon);

                if (StringUtils.hasText(checkLog.getExecRes())) {
                    actionLog.setCheckRes(StatusConstants.CHECK_SUCCESS);
                } else {
                    actionLog.setCheckRes(StatusConstants.CHECK_ERROR);
                }
            }

        } catch (IOException e) {
            log.error(e.getMessage());
            buildErrLog(actionLog, "输出流异常");
        } catch (JSchException e) {
            log.error(e.getMessage());
            buildErrLog(actionLog, ResultCode.JSCH_CONNECT_ERROR.getMessage());
        } finally {
            //执行后记录日志
            log.debug(actionLog.toString());
            Integer update = actionLogService.insertActionLog(actionLog);
        }
        return actionLog;
    }



    @Override
    public CompletableFuture<ActionLog> asyncExecCmd(Action action) {
        return CompletableFuture.completedFuture(syncExecCmd(action));
    }

    private void buildErrLog(ActionLog actionLog, String reason) {
        actionLog.setEndTime(actionLog.getStartTime());
        actionLog.setExecRes("");
        actionLog.setExitStatus(null);
        actionLog.setStatus(StatusConstants.ACTION_ERROR);
        actionLog.setReason(reason);
    }

    private void extractExecLog(ActionLog actionLog, ExecLog execLog) {
        actionLog.setExecRes(execLog.getExecRes());
        actionLog.setExitStatus(execLog.getExitStatus());
        actionLog.setStartTime(execLog.getStartTime());
        actionLog.setEndTime(execLog.getEndTime());
        if (Objects.equals(actionLog.getExitStatus(), StatusConstants.EXIT_SUCCESS)
                | Objects.equals(actionLog.getExitStatus(), StatusConstants.EXIT_SUCCESS_TAIL)
        ) {
            actionLog.setStatus(StatusConstants.ACTION_DONE);
        } else {
            actionLog.setStatus(StatusConstants.ACTION_ERROR);
            actionLog.setReason("命令执行失败");
        }
    }

    private ExecuteCommon getExecuteCommon(Action queryAction) {
        ExecuteCommon executeCommon = new ExecuteCommon();
        executeCommon.setMaxSize(queryAction.getMaxSize());
        executeCommon.setMaxTime(queryAction.getMaxSecond());
        executeCommon.setStopWords(queryAction.getStopWords());
        return executeCommon;
    }
}

