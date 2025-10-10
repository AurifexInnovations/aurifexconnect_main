package com.erp.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FeedbackRequest {

    private String comment;

    private float rating;

    private  String otp;

    private  String mobileNo;


}
