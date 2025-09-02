package com.erp.TechnicianApp.TechnicianService.PerformanceService;

import com.erp.Exception.User.UserNotFoundException;
import com.erp.Model.User;
import com.erp.Repository.User.UserRepository;
import com.erp.TechnicianApp.TechnicianDto.Request.PerformanceEvaluationRequest;
import com.erp.TechnicianApp.TechnicianDto.Request.PerformanceRecordRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.PerformanceResponse;
import com.erp.TechnicianApp.TechnicianException.PerformanceException.PerformanceNotFoundException;
import com.erp.TechnicianApp.TechnicianMapper.PerformanceMapper.PerformanceMapper;
import com.erp.TechnicianApp.TechnicianModel.TechnicianPerformance;
import com.erp.TechnicianApp.TechnicianRepository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PerformanceServiceImpl implements PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final UserRepository userRepository;
    private final PerformanceMapper performanceMapper;

    @Override
    public PerformanceResponse evaluatePerformance(PerformanceEvaluationRequest request) {
        Optional<User> userOpt = userRepository.findById(request.getUserId());

        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + request.getUserId());
        }

        User user = userOpt.get();

        TechnicianPerformance performance = new TechnicianPerformance();
        performance.setUser(user);
        performance.setMonth(request.getMonth());
        performance.setYear(request.getYear());

        // Mock KPI scoring (replace later with real logic)
        performance.setPunctualityScore(BigDecimal.valueOf(Math.random() * 10));
        performance.setTaskCompletionScore(BigDecimal.valueOf(Math.random() * 10));
        performance.setOvertimeScore(BigDecimal.valueOf(Math.random() * 10));
        performance.setCustomerFeedbackScore(BigDecimal.valueOf(Math.random() * 10));

        BigDecimal finalRating = performance.getPunctualityScore()
                .add(performance.getTaskCompletionScore())
                .add(performance.getOvertimeScore())
                .add(performance.getCustomerFeedbackScore())
                .divide(BigDecimal.valueOf(4), 2, BigDecimal.ROUND_HALF_UP);

        performance.setFinalRating(finalRating);

        TechnicianPerformance saved = performanceRepository.save(performance);
        return performanceMapper.toResponse(saved);
    }

    @Override
    public PerformanceResponse getPerformanceRecord(PerformanceRecordRequest request) {
        Optional<TechnicianPerformance> perfOpt = performanceRepository
                .findByUser_UserIdAndMonthAndYear(
                        request.getUserId(),
                        request.getMonth(),
                        request.getYear()
                );

        if (perfOpt.isEmpty()) {
            throw new PerformanceNotFoundException(
                    "Performance record not found for User ID: "
                            + request.getUserId()
                            + " (" + request.getMonth() + "/" + request.getYear() + ")"
            );
        }

        return performanceMapper.toResponse(perfOpt.get());
    }

    @Override
    public List<PerformanceResponse> getPerformanceHistory(Long userId) {
        List<TechnicianPerformance> history = performanceRepository.findByUser_UserId(userId);

        if (history == null || history.isEmpty()) {
            throw new PerformanceNotFoundException(
                    "No performance history found for User ID: " + userId
            );
        }

        return performanceMapper.toResponseList(history);
    }
}
