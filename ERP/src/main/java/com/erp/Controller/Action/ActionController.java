package com.erp.Controller.Action;

import com.erp.Dto.Request.ActionDto;
import com.erp.Service.Action.ActionService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/action")
public class ActionController {

    private final ActionService actionService;

    @PostMapping
    public ResponseEntity<ResponseStructure<ActionDto>> addAction(@RequestBody ActionDto actionDto ) {
        actionDto  = actionService.addAction(actionDto);
        return ResponseBuilder.success(HttpStatus.CREATED, "action request submitted", actionDto);
    }

    @PutMapping("/id/{aId}")
    public ResponseEntity<ResponseStructure<ActionDto>> updateAction(@PathVariable("aId") long actionId,
                                                                     @RequestBody ActionDto actionDto ) {
        actionDto  = actionService.updateActionByActionId( actionId , actionDto);
        return ResponseBuilder.success(HttpStatus.OK, "action updated Successfully", actionDto);
    }

    @PostMapping("/id/{aId}")
    public ResponseEntity<ResponseStructure<ActionDto>> getActionByActionId(@PathVariable("aId") long actionId) {
        ActionDto actionDto  = actionService.getActionByActionId( actionId);
        return ResponseBuilder.success(HttpStatus.OK, "get action by id", actionDto);
    }

    @PostMapping
    public ResponseEntity<ListResponseStructure<ActionDto>> getAllActions() {
        List<ActionDto> actions  = actionService.getActions();
        return ResponseBuilder.success(HttpStatus.OK, "action details", actions);
    }

    @DeleteMapping("/id/{aId}")
    public ResponseEntity<ResponseStructure<ActionDto>> deleteActionById(@PathVariable("aId") long actionId) {
        ActionDto actionDto =  actionService.deleteActionById(actionId);
        return ResponseBuilder.success(HttpStatus.OK, "action deleted successfully" , actionDto);
    }
}
