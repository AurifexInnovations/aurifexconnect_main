package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {

    private long id ;
    private long customerId;
    private long taskId;
    private long digitalSignatureId;
    private float rating;
    private String comment;
    private String digitalSignatureUrl;
}
