package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketSearchResponse {

    private List<TicketResponseDTO> tickets;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}
