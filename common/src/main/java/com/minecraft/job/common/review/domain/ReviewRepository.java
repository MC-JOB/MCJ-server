package com.minecraft.job.common.review.domain;

import com.minecraft.job.common.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findReviewByTeam(Team team);
}
