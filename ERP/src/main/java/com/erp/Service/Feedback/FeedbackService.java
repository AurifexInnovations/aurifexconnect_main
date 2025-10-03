package com.erp.Service.Feedback;

import com.erp.Dto.Request.FeedbackRequest;
import com.erp.Dto.Response.FeedbackResponse;
import com.erp.Dto.Response.FileResponse;
import com.erp.Mapper.Feedback.FeedbackMapper;
import com.erp.Model.Feedback;
import com.erp.Model.File;
import com.erp.Model.GenericUser;
import com.erp.Repository.Feedback.FeedbackRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Service.Utility.FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final static String DIGITAL_SIGNATURE = "DIGITAL_SIGNATURE";
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final UserIdentity userIdentity;
    private final FileService fileService;


    @Transactional
    public FeedbackResponse addFeedBack(long taskId ,
                             FeedbackRequest feedbackRequest){

        GenericUser currentUser =  userIdentity.getCurrentUser();

        Feedback feedback = getFeedback(taskId , currentUser.getId());

        if(Objects.nonNull(feedback)){
//            add error here
        }

        feedback = feedbackMapper.map(feedbackRequest);
        feedback.setCreatedAt(LocalDateTime.now());

        GenericUser user = userIdentity.getCurrentUser();
        feedback.setCustomerId(user.getId());
        feedback.setActive(true);

        feedbackRepository.save(feedback);

        FeedbackResponse feedbackResponse = feedbackMapper.map(feedback);

        setDigitalSignatureData(taskId , feedbackResponse);

        return feedbackResponse;
    }

    public FeedbackResponse updateFeedback(long feedbackId ,FeedbackRequest feedbackRequest){

        Feedback feedback  = getFeedback(feedbackId);

        feedback  =
                feedbackMapper.update( feedback, feedbackRequest);

        feedback.setUpdatedAt(LocalDateTime.now());

        feedback = feedbackRepository.save(feedback);

        FeedbackResponse feedbackResponse = feedbackMapper.map(feedback);

        setDigitalSignatureData(feedback.getTaskId() , feedbackResponse);

        return feedbackResponse;
    }

    private void setDigitalSignatureData(long taskId , FeedbackResponse feedbackResponse){
        List<FileResponse> signature = fileService.getAllFiles(taskId , DIGITAL_SIGNATURE);

        feedbackResponse.setDigitalSignatureId(signature.get(0).getId());
        feedbackResponse.setDigitalSignatureUrl(signature.get(0).getUrl());
    }
    private Feedback getFeedback(long feedbackId){
        Optional<Feedback> feedbackContainer =
                feedbackRepository.findById(feedbackId);

        if(feedbackContainer.isEmpty()){
//             add error here
        }

        return  feedbackContainer.get();
    }

    @Transactional
    public void deleteFeedback(long feedbackId){
        Feedback feedback = getFeedback(feedbackId);

        List<FileResponse> signature =
                fileService.getAllFiles(feedback.getTaskId() , DIGITAL_SIGNATURE);

        fileService.deleteFile(signature.get(0).getId() , feedback.getTaskId() ,  DIGITAL_SIGNATURE);

        feedback.setActive(false);

    }

    public FeedbackResponse getFeedbackById(long feedbackId){
        Feedback feedback  = getFeedback(feedbackId);

        FeedbackResponse feedbackResponse = feedbackMapper.map(feedback);

        setDigitalSignatureData(feedback.getTaskId() , feedbackResponse);

        return feedbackResponse;

    }
    private Feedback getFeedback(long taskId , long customerId){
        Feedback feedback =
                feedbackRepository.findByTaskIdAndCustomerId(taskId , customerId);

        return feedback;
    }
}
