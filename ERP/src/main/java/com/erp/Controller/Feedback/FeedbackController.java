package com.erp.Controller.Feedback;

import com.erp.Dto.Request.FeedbackRequest;
import com.erp.Dto.Request.InventoryMovementSummaryRequest;
import com.erp.Dto.Response.FeedbackResponse;
import com.erp.Dto.Response.InventoryMovementResponse;
import com.erp.Dto.Response.InventoryMovementSummaryResponse;
import com.erp.Service.Feedback.FeedbackService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("feedback")
@RequiredArgsConstructor
@Slf4j
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/task/{tId}")
    public ResponseEntity<ResponseStructure<FeedbackResponse>> addFeedback(@PathVariable("tId") int taskId,
                                                                           @RequestBody FeedbackRequest feedbackRequest){
        log.info("Into [FeedbackController] [addFeedback] :: taskId {} :: request :: {} " , taskId , feedbackRequest);
        FeedbackResponse feedbackResponse =  feedbackService.addFeedBack(taskId ,feedbackRequest);
        log.info("Exit [FeedbackController] [addFeedback] :: taskId {} :: response :: {} " , taskId , feedbackResponse);
        return ResponseBuilder.success(HttpStatus.CREATED, "Feedback added successfully", feedbackResponse);
    }

    @GetMapping("/{fId}")
    public ResponseEntity<ResponseStructure<FeedbackResponse>> getFeedback(@PathVariable("fId") int feedbackId)
    {
        log.info("Into [FeedbackController] [addFeedback] :: feedbackId {} " , feedbackId);
        FeedbackResponse feedbackResponse = feedbackService.getFeedbackById(feedbackId);
        log.info("Exit [FeedbackController] [addFeedback] :: response {} " , feedbackResponse);
        return ResponseBuilder.success(HttpStatus.OK, "Feedback fetch successfully", feedbackResponse);
    }

    @DeleteMapping("/{fId}")
    public ResponseEntity<ResponseStructure<String>> deleteFeedback(@PathVariable("fId") int feedbackId)
    {
        log.info("Into [FeedbackController] [deleteFeedback] :: feedbackId {} " , feedbackId);
        feedbackService.deleteFeedback(feedbackId);
        log.info("Exit [FeedbackController] [deleteFeedback] :: feedbackId {} " , feedbackId);
        return ResponseBuilder.success(HttpStatus.NO_CONTENT, "Feedback deleted successfully" , "");
    }

    @PutMapping("/{fId}")
    public ResponseEntity<ResponseStructure<FeedbackResponse>> updateFeedback(@PathVariable("fId") int feedbackId ,
                                                                    @RequestBody FeedbackRequest feedbackRequest)
    {
        log.info("Into [FeedbackController] [updateFeedback] :: feedbackId {} :: request {} " , feedbackId , feedbackRequest);
        FeedbackResponse feedbackResponse = feedbackService.updateFeedback(feedbackId , feedbackRequest);
        log.info("Exit [FeedbackController] [updateFeedback] :: feedbackId {} :: response {} " , feedbackId , feedbackResponse);
        return ResponseBuilder.success(HttpStatus.OK, "Feedback deleted successfully" , feedbackResponse);
    }


}
