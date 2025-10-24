package com.erp.Mapper.Action;

import com.erp.Dto.Request.ActionDto;
import com.erp.Model.Action;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ActionMapper {

    public Action map(ActionDto actionDto) {
        return update(null, actionDto);
    }

    public Action update(Action action, ActionDto actionDto) {
        if (Objects.isNull(action)) {
            action = new Action();
        }

        if (Objects.nonNull(actionDto)) {
            action.setName(actionDto.getName());
            action.setDescription(actionDto.getDescription());
        }

        return action;
    }

    public ActionDto map(Action action) {
        return update(null, action);
    }

    public ActionDto update(ActionDto actionDto, Action action) {
        if (Objects.isNull(actionDto)) {
            actionDto = new ActionDto();
        }

        if (Objects.nonNull(action)) {
            actionDto.setId(action.getId());
            actionDto.setName(action.getName());
            actionDto.setDescription(action.getDescription());
        }

        return actionDto;
    }

    public List<ActionDto> map(List<Action> actions) {
        return update(null, actions);
    }

    public List<ActionDto> update(List<ActionDto> actionDtos, List<Action> actions) {
        if (Objects.isNull(actionDtos)) {
            actionDtos = new ArrayList<>();
        }

        if (Objects.nonNull(actions)) {
            for (Action action : actions) {
                ActionDto actionDto = map(action);
                actionDtos.add(actionDto);
            }
        }

        return actionDtos;
    }
}
