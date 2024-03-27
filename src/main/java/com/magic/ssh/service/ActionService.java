package com.magic.ssh.service;
 

import com.magic.ssh.entity.Action;

import java.util.List;

public interface ActionService {
    Action getActionInfoById(Integer actionId);

    Action getActionByName(String actionName);

    Integer insertAction(Action action);

    Integer updateAction(Action action);

    Integer deleteAction(Integer actionId);

    List<Action> getActionByUser(Integer userId);

    Action getActionByHost(String hostId);

    Integer saveUser(Integer userId, String actionIds);
}
