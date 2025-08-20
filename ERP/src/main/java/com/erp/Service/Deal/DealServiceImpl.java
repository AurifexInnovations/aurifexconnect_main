package com.erp.Service.Deal;


import com.erp.Dto.Request.DealRequest;
import com.erp.Dto.Response.DealResponse;
import com.erp.Mapper.Deal.DealMapper;
import com.erp.Model.Deal;

import com.erp.Repository.Deal.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepo;
    private final DealMapper dealMapper;

    @Override
    public DealResponse create(DealRequest request) {
        Deal deal = dealMapper.mapToEntity(request);
        return dealMapper.mapToResponse(dealRepo.save(deal));
    }

    @Override
    public DealResponse update(Long id, DealRequest request) {
        Deal deal = dealRepo.findById(id).orElseThrow(() -> new RuntimeException("Deal not found"));
        dealMapper.updateEntityFromRequest(request, deal);
        return dealMapper.mapToResponse(dealRepo.save(deal));
    }

    @Override
    public DealResponse getById(Long id) {
        return dealMapper.mapToResponse(dealRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Deal not found")));
    }

    @Override
    public List<DealResponse> getAll() {
        return dealMapper.mapToResponseList(dealRepo.findAll());
    }

    @Override
    public void delete(Long id) {
        dealRepo.deleteById(id);
    }
}
