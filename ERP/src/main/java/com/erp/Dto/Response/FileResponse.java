package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {
    private long id;
    private String url;
    private int sequence;
    private long uploadedBy;
}
