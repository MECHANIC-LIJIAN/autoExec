package com.magic.ssh.service;
 

import com.magic.ssh.entity.Action;

import java.util.List;

public interface ActionService {
    Action getActionInfoById(Integer actionId);

    Integer insertAction(Action action);

    Integer updateAction(Action action);

    Integer deleteAction(Integer actionId);

    List<Action> getActionByUser(Integer userId);

    Integer saveUser(Integer userId,String actionIds);
}
