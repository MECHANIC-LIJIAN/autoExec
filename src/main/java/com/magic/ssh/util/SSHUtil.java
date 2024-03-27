package com.magic.ssh.util;

import com.magic.ssh.entity.ExecLog;
import com.magic.ssh.entity.Host;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.magic.ssh.service.ExecuteCallable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SSHUtil {

    private SSHUtil() {
    }

    private static final class SSHUtilHolder {
        static final SSHUtil sshUtil = new SSHUtil();
    }

    public static SSHUtil getInstance() {
        return SSHUtilHolder.sshUtil;
    }

    public static long ipToInteger(InetAddress ip) {
        byte[] bytes = ip.getAddress();
        long result = 0;
        for (byte b : bytes) {
            result <<= 8;
            result |= (b & 0xFF);
        }
        return result;
    }

    public static String getServerKey(Host host) {
        return getServerKey(host.getIpAddress(), host.getUsername());
    }

    public static String getServerKey(String ipAddress, String username) {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        long ipInt = ipToInteger(inetAddress);
        return String.join("_", ipAddress, username);
    }

    private final static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<String, Session>(128);

    private final Integer CONNECT_TIMEOUT = 30000;

    public synchronized void init(Host host) throws JSchException {
        //生成全局唯一的id
        String serverKey = host.getHostId();
        Session session;
        JSch jsch = new JSch();
        if (StringUtils.hasText(host.getIdentity()) && Files.exists(Paths.get(host.getIdentity()))) {
            jsch.addIdentity(host.getIdentity(), host.getPassphrase());
        }
        session = jsch.getSession(host.getUsername(), host.getIpAddress(), host.getPort());
        session.setPassword(host.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(CONNECT_TIMEOUT);

        sessionPools.put(serverKey, session);
        log.debug("{}/{} connect success", host.getIpAddress(), host.getUsername());
    }

    public ExecLog executeCommand(String serverKey, String cmd,
                                  ExecuteCallable<String> buffer) throws JSchException, IOException {

        Session session = sessionPools.get(serverKey);
        if (session == null) {
            log.error("请先执行init()");
            return null;
        }

        ExecLog execLog = new ExecLog();
        BufferedReader reader = null;
        ArrayList<String> resultLines = new ArrayList<>();
        ChannelExec channelExec = null;
        try {
            //使用exec方式建立连接
            channelExec = (ChannelExec) session.openChannel("exec");
            //设置输入流和错误信息流
            InputStream in = channelExec.getInputStream();
            InputStream err = channelExec.getErrStream();

            cmd = "source .bash_profile && " + cmd.trim() + " 2>&1";
            log.debug("exec command: {}", cmd);
            channelExec.setCommand(cmd);
            //建立连接，命令自动执行
            channelExec.connect();
            //记录执行开始时间
            execLog.setStartTime(System.currentTimeMillis());

            reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String msg;
            while ((msg = reader.readLine()) != null) {
                buffer.appendBuffer(msg);
                resultLines.add(msg);
                if (buffer.IamDone()) {
                    break;
                }
            }

            if (resultLines.isEmpty()) {
                reader = new BufferedReader(new InputStreamReader(err));
                String msgErr;
                while ((msgErr = reader.readLine()) != null) {
                    buffer.appendBuffer(msgErr);
                    resultLines.add(msgErr);
                    if (buffer.IamDone()) {
                        break;
                    }
                }
            }

            channelExec.disconnect();

            //判断连接关闭
            if (channelExec.isClosed()) {
                //状态码，正常是0
                execLog.setExitStatus(channelExec.getExitStatus());
            }
            execLog.setExecRes(String.join("<br>", resultLines));
            //记录执行结束始时间
            execLog.setEndTime(System.currentTimeMillis());
            log.debug("{} -> '{}' exec success, cost {}ms", session.getHost(), cmd,
                    execLog.getEndTime() - execLog.getStartTime());

            in.close();
            err.close();

        } finally {
            if (Objects.nonNull(channelExec)) {
                channelExec.disconnect();
            }
            if (Objects.nonNull(reader)) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return execLog;
    }

    public Session getSession(String hostKey) {
        return sessionPools.get(hostKey);
    }

    public void close(String hostKey) {
        if (sessionPools.containsKey(hostKey)) {
            sessionPools.get(hostKey).disconnect();
            sessionPools.remove(hostKey);
        }
        log.info("exit success");
    }

}

