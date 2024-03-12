package com.magic.ssh.mapper;

import com.magic.ssh.entity.Action;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActionMapper {

    Action queryAction(Integer actionId);

    Action queryActionByHostId(String hostId);

    Integer insertAction(Action action);

    Integer updateAction(Action action);

    Integer deleteAction(Integer actionId);

    List<Action> getActionByUserId(Integer userId);

    Integer queryNumByIds(List<Integer> actionIds);

    List<Action> queryActionsByIds(List<Integer> actionIds);
}
