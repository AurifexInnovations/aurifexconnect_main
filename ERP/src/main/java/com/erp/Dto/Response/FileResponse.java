package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileResponse {
    private long id;
    private String url;
    private int sequence;
    private long uploadedBy;
}
