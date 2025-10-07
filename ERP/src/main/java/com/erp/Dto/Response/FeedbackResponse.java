package com.erp.Dto.Response;

import com.erp.Projection.TechnicianResponse;
import com.erp.Projection.TechnitianFeedbackDetailProjection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)  // ✅ Ignore null fields during JSON serialization
public class FeedbackResponse {

    private long id ;
    private long customerId;
    private long taskId;
    private List<TechnitianFeedbackDetailProjection> technitianDetails;
    private long digitalSignatureId;
    private float rating;
    private String comment;
    private String digitalSignatureUrl;
}
