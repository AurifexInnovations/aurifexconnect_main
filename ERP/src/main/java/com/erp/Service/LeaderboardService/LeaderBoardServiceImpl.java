package com.erp.Service.LeaderboardService;

import com.erp.Dto.Request.LeaderboardDTO;
import com.erp.Dto.Request.TechnicianStatsDTO;
import com.erp.Model.Leaderboard;
import com.erp.Repository.Feedback.FeedbackRepository;
import com.erp.Repository.LeaderBoard.LeaderBoardRepository;
import com.erp.Repository.Staff.StaffRepository;
import com.erp.Repository.Task.TaskRepository;
import com.erp.Repository.Task.TaskScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderBoardServiceImpl implements LeaderBoardService {


    private final LeaderBoardRepository leaderBoardRepository;

    private final FeedbackRepository feedbackRepository;

    @Transactional
    public void addLeadBoard() {
        log.info("Starting to update leaderboard...");

        try {
            List<TechnicianStatsDTO> technicianStats = feedbackRepository.getTechnicianStats();

            if (technicianStats == null && technicianStats.isEmpty()) {

            List<Leaderboard> leaderboardList = technicianStats.stream()
                    .map(stats -> {
                        Leaderboard model = new Leaderboard();
                        model.setAverageRating(stats.getAverageRating());
                        model.setTechnicianId(stats.getTechnicianId());
                        model.setTasksCompleted(stats.getTasksCompleted());
                        model.setTechnicianId(stats.getTechnicianId());
                        return model;
                    })
                    .collect(Collectors.toList());

            leaderBoardRepository.saveAll(leaderboardList);
            log.info("Leaderboard updated successfully with {} entries.", leaderboardList.size());

            }

        } catch (Exception e) {
            log.error("Error while updating leaderboard: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update leaderboard", e);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void runDailyLeaderboardUpdate() {
        log.info("Running Daily Leaderboard Update at 12 AM...");
        addLeadBoard();
    }
}
