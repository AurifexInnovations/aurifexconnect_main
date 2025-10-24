package com.erp.Service.Action;

import com.erp.Dto.Request.ActionDto;
import com.erp.Exception.ResourceFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.Action.ActionMapper;
import com.erp.Model.Action;
import com.erp.Repository.Action.ActionRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Utility.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActionService {

    private final ActionRepository actionRepository;
    private final ActionMapper actionMapper;
    private final UserIdentity userIdentity;

    public ActionDto addAction(ActionDto actionDto) {
        log.info("Into [ActionService] [addAction]");

        log.info("[ActionService] [addAction] :: Request :: {} " ,
                ObjectMapperUtils.writeValueAsString(actionDto));

        validateActionByName(actionDto.getName());

        Action action = actionMapper.map(actionDto);
        action.setActive(true);
        action.setCreatedAt(LocalDateTime.now());
        action.setCreatedBy(userIdentity.getCurrentUser().getId());

        action = actionRepository.save(action);

        log.info("Exit [ActionService] [addAction]");

        return actionMapper.map(action);
    }

    public ActionDto updateActionByActionId(long actionId , ActionDto actionDto){
        log.info("Into [ActionService] [updateActionByActionId]");

        log.info("[ActionService] [updateActionByActionId] :: ActionId {} :: Request :: {} " , actionId ,
                ObjectMapperUtils.writeValueAsString(actionDto));

        Action action = getAction(actionId);

        actionMapper.update(action , actionDto);
        action =  actionRepository.save(action);

        log.info("Exit [ActionService] [updateActionByActionId] ");

        return actionMapper.map(action);
    }

    public ActionDto getActionByActionId(long actionId){
        log.info("Into [ActionService] [updateActionByActionId] ");

        log.info("[ActionService] [updateActionByActionId] :: ActionId :: {} " , actionId);

        Action action = getAction(actionId);

        log.info("Exit [ActionService] [updateActionByActionId]");

        return actionMapper.map(action);
    }

    public List<ActionDto> getActions(){
        log.info("Into [ActionService] [getActions]");

        List<Action> actions =
                actionRepository.findAll();

        log.info("Exit [ActionService] [getActions]");

        return actionMapper.map(actions);
    }

    public ActionDto deleteActionById(long actionId){
        log.info("Into [ActionService] [deleteActionById]");

        log.info("[ActionService] [deleteActionById] :: ActionId :: {} " , actionId);

        Action action = getAction(actionId);
        action.setActive(false);

        actionRepository.save(action);

        log.info("Exit [ActionService] [deleteActionById]");

        return actionMapper.map(action);
    }

    public Action getAction(long actionId){
        Action action = actionRepository.findById(actionId);

        if(Objects.isNull(action)){
            throw new ResourceNotFoundException("action is not found with this action id " + actionId);
        }

        return action;
    }
    private void validateActionByName(String name){
        Action action = actionRepository.findByName(name);

        if(Objects.nonNull(action)){
            throw new ResourceFoundException("Action with this name is already present");
        }

    }

}
