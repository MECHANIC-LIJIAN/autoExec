package com.magic.ssh.service;
 

import com.magic.ssh.entity.Host;

import java.net.UnknownHostException;
import java.util.List;

public interface HostService {
    Host getHostById(String hostId);

    Host getHostByName(String hostName);


    Integer insertHost(Host host) throws UnknownHostException;

    Integer updateHost(Host host);

    Integer deleteHost(String hostId);

    List<Host> getHostByUser(Integer userId);

    Integer saveUser(Integer userId,String hostIds);
}
