package com.magic.ssh.mapper;

import com.magic.ssh.entity.Host;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HostMapper {

    Host queryHost(String hostId);

    Integer insertHost(Host host);

    Integer updateHost(Host host);

    Integer deleteHost(String username);

    List<Host> getHostByUserId(Integer userId);

}
