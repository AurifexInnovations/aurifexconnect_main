package com.erp.Controller.Feedback;

import com.erp.Dto.Request.FeedbackRequest;
import com.erp.Service.Feedback.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/customer/{cId}/task/{tId}")
    public void addFeedback(@PathVariable("cId") int customerId ,
                            @PathVariable("tId") int taskId,
                            @RequestBody FeedbackRequest feedbackRequest){
    }
}
