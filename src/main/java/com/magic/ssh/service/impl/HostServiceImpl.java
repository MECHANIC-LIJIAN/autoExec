package com.magic.ssh.service.impl;

import com.magic.ssh.entity.Action;
import com.magic.ssh.entity.Host;
import com.magic.ssh.mapper.HostMapper;
import com.magic.ssh.service.ActionService;
import com.magic.ssh.service.HostService;
import com.magic.ssh.util.SSHUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HostServiceImpl implements HostService {

    private final HostMapper hostMapper;

    public HostServiceImpl(HostMapper hostMapper) {
        this.hostMapper = hostMapper;
    }

    @Override
    public Host getHostInfoById(String hostId) {
        return hostMapper.queryHost(hostId);
    }

    @Override
    public Integer insertHost(Host host) throws UnknownHostException {
        host.setHostId(SSHUtil.getServerKey(host));
        if (Objects.nonNull(hostMapper.queryHost(host.getHostId()))) {
            return 0;
        }
        if (!StringUtils.hasText(host.getHostname())) {
            host.setHostname(host.getIpAddress());
        }
        return hostMapper.insertHost(host);
    }

    @Override
    public Integer updateHost(Host host) {
        Host myHost = hostMapper.queryHost(host.getHostId());
        if (Objects.isNull(myHost)) {
            return -1;
        }
        SSHUtil.getInstance().close(host.getHostId());
        return hostMapper.updateHost(host);
    }

    @Override
    public Integer deleteHost(String hostId) {
        Host myHost = hostMapper.queryHost(hostId);
        if (Objects.isNull(myHost)) {
            return -1;
        }
        return hostMapper.deleteHost(hostId);
    }

    @Override
    public List<Host> getHostByUser(Integer useId) {
        List<Host> hostList = hostMapper.getHostByUserId(useId);
        hostList.forEach(o -> o.setPassword(null));
        return hostList;

    }

    @Override
    public Integer saveUser(Integer userId, String hostIds) {
        return null;
    }
}
