package com.magic.ssh.service.impl;

import com.magic.ssh.entity.Action;
import com.magic.ssh.mapper.ActionMapper;
import com.magic.ssh.service.ActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ActionServiceImpl implements ActionService {

    private final ActionMapper actionMapper;
    public ActionServiceImpl(ActionMapper actionMapper) {
        this.actionMapper = actionMapper;
    }

    @Override
    public Action getActionInfoById(Integer actionId) {
        return actionMapper.queryAction(actionId);
    }

    @Override
    public Integer insertAction(Action action){
        return actionMapper.insertAction(action);
    }

    @Override
    public Integer updateAction(Action action) {
        Action myAction=actionMapper.queryAction(action.getActionId());
        if (Objects.isNull(myAction)){
            return -1;
        }

        return actionMapper.updateAction(action);
    }

    @Override
    public Integer deleteAction(Integer actionId) {
        Action myAction=actionMapper.queryAction(actionId);
        if (Objects.isNull(myAction)){
            return -1;
        }
        return actionMapper.deleteAction(actionId);
    }

    @Override
    public List<Action> getActionByUser(Integer useId) {
        return actionMapper.getActionByUserId(useId);
    }

    @Override
    public Integer saveUser(Integer userId, String actionIds) {
        return null;
    }
}
