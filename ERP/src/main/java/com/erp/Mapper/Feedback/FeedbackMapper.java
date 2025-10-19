package com.erp.Mapper.Feedback;

import com.erp.Dto.Request.FeedbackRequest;
import com.erp.Dto.Response.FeedbackResponse;
import com.erp.Model.Feedback;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FeedbackMapper {

    public Feedback map(FeedbackRequest feedbackRequest){
        return update(null , feedbackRequest);
    }

    public Feedback update(Feedback feedback , FeedbackRequest feedbackRequest){
        if(Objects.isNull(feedback)){
            feedback = new Feedback();
        }

        if(Objects.nonNull(feedbackRequest)){
            feedback.setComment(feedbackRequest.getComment());
            feedback.setRating(feedbackRequest.getRating());
        }

        return feedback;
    }

    public FeedbackResponse map(Feedback feedback){
        return update(null , feedback);
    }

    public FeedbackResponse update(FeedbackResponse feedbackResponse , Feedback feedback){
        if(Objects.isNull(feedbackResponse)){
            feedbackResponse = new FeedbackResponse();
        }

        if(Objects.nonNull(feedback)){
            feedbackResponse.setId(feedback.getId());
            feedbackResponse.setCustomerId(feedback.getCustomerId());
            feedbackResponse.setTaskId(feedback.getTaskId());
            feedbackResponse.setRating(feedback.getRating());
            feedbackResponse.setComment(feedback.getComment());
        }

        return feedbackResponse;
    }
}
