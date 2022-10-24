package com.minecraft.job.common.review.domain;

import com.minecraft.job.common.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static com.minecraft.job.common.review.domain.ReviewStatus.ACTIVATED;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    default List<Review> findAllActivated(Team team) {
        return findAllActivatedInternal(team, ACTIVATED);
    }

    @Query("""
                select r
                from Review r
                    join fetch r.team t
                where r.team = :team 
                and r.status = :status
            """)
    List<Review> findAllActivatedInternal(Team team, ReviewStatus status);
}
