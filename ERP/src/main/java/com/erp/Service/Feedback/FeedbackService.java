package com.erp.Service.Feedback;

import com.erp.Dto.Request.FeedbackRequest;
import com.erp.Dto.Response.FeedbackResponse;
import com.erp.Dto.Response.FileResponse;
import com.erp.Mapper.Feedback.FeedbackMapper;
import com.erp.Model.Feedback;
import com.erp.Model.GenericUser;
import com.erp.Model.TechnicianTaskMapper;
import com.erp.Projection.TechnitianFeedbackDetailProjection;
import com.erp.Repository.Feedback.FeedbackRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Service.TaskService.TaskService;
import com.erp.Service.Utility.FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final static String DIGITAL_SIGNATURE = "DIGITAL_SIGNATURE";
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final UserIdentity userIdentity;
    private final FileService fileService;

    private final TaskService taskService;


    @Transactional
    public FeedbackResponse addFeedBack(long taskId ,
                             FeedbackRequest feedbackRequest){
        log.info("Into [FeedbackService] [addFeedBack] ");

        GenericUser currentUser =  userIdentity.getCurrentUser();

        log.info("[FeedbackService] [addFeedBack] :: currentUserId {} " , currentUser.getId());

        Feedback feedback = getFeedback(taskId , currentUser.getId());

        if(Objects.nonNull(feedback)){
            throw new RuntimeException("Feedback alreday found");
        }

        feedback = feedbackMapper.map(feedbackRequest);
        feedback.setTaskId(taskId);
        feedback.setCreatedAt(LocalDateTime.now());

        GenericUser user = userIdentity.getCurrentUser();
        feedback.setCustomerId(user.getId());
        feedback.setActive(true);

        feedback = feedbackRepository.save(feedback);

        taskService.updateTechnitianFeedBack(taskId , feedback.getId());

        FeedbackResponse feedbackResponse = feedbackMapper.map(feedback);
        setDigitalSignatureData(taskId , feedbackResponse);

        log.info("Exit [FeedbackService] [addFeedBack] ");
        return feedbackResponse;
    }
    @Transactional
    public FeedbackResponse updateFeedback(long feedbackId ,FeedbackRequest feedbackRequest){
        log.info("Into [FeedbackService] [updateFeedback] ");

        Feedback feedback  = getFeedback(feedbackId);

        feedback  = feedbackMapper.update( feedback, feedbackRequest);

        feedback.setUpdatedAt(LocalDateTime.now());

        feedback = feedbackRepository.save(feedback);

        FeedbackResponse feedbackResponse = feedbackMapper.map(feedback);

        setDigitalSignatureData(feedback.getTaskId() , feedbackResponse);
        log.info("Exit [FeedbackService] [updateFeedback] ");

        return feedbackResponse;
    }

    private void setDigitalSignatureData(long taskId , FeedbackResponse feedbackResponse){
        log.info("Into [FeedbackService] [setDigitalSignatureData]");

        List<FileResponse> signature = fileService.getAllFiles(taskId , DIGITAL_SIGNATURE);

        feedbackResponse.setDigitalSignatureId(signature.get(0).getId());
        feedbackResponse.setDigitalSignatureUrl(signature.get(0).getUrl());

        log.info("[FeedbackService] [setDigitalSignatureData] :: signatures  {} " , signature);
        log.info("Exit [FeedbackService] [setDigitalSignatureData]");

    }
    private Feedback getFeedback(long feedbackId){
        log.info("Into [FeedbackService] [getFeedback] ");

        Optional<Feedback> feedbackContainer =
                feedbackRepository.findById(feedbackId);

        if(feedbackContainer.isEmpty()){
            throw new RuntimeException("Feedback details not found");
        }

        log.info("Exit [FeedbackService] [getFeedback] ");
        return  feedbackContainer.get();
    }

    @Transactional
    public void deleteFeedback(long feedbackId){
        log.info("Into [FeedbackService] [getFeedback] ");

        Feedback feedback = getFeedback(feedbackId);

        List<FileResponse> signature =
                fileService.getAllFiles(feedback.getTaskId() , DIGITAL_SIGNATURE);

        fileService.deleteFile(signature.get(0).getId() , feedback.getTaskId() ,  DIGITAL_SIGNATURE);
        feedback.setActive(false);

        feedbackRepository.save(feedback);

        taskService.updateTechnitianFeedBack(feedback.getTaskId(),  0);

        log.info("Exit [FeedbackService] [getFeedback] ");
    }

    public FeedbackResponse getFeedbackById(long feedbackId){
        log.info("Into [FeedbackService] [getFeedback] ");

        Feedback feedback  = getFeedback(feedbackId);

        FeedbackResponse feedbackResponse = feedbackMapper.map(feedback);
        setDigitalSignatureData(feedback.getTaskId() , feedbackResponse);

        List<TechnitianFeedbackDetailProjection> technitianFeedbackDetailProjections =
                taskService.getTechnitianFeedbackDetails(feedbackId);

        feedbackResponse.setTechnitianDetails(technitianFeedbackDetailProjections);

        log.info("Exit [FeedbackService] [getFeedbackById] ");
        return feedbackResponse;

    }


    private Feedback getFeedback(long taskId , long customerId){
        log.info("Into [FeedbackService] [getFeedback] ");

        Feedback feedback =
                feedbackRepository.findByTaskIdAndCustomerId(taskId , customerId);

        log.info("Exit [FeedbackService] [getFeedback] ");
        return feedback;
    }
}
