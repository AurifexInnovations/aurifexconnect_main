package com.erp.Repository.LeaderBoard;


import com.erp.Model.Leaderboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderBoardRepository  extends JpaRepository<Leaderboard,Long> {
}
