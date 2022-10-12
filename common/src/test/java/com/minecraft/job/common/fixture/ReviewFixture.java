package com.minecraft.job.common.fixture;

import com.minecraft.job.common.review.domain.Review;
import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.user.domain.User;

public class ReviewFixture {

    public static Review create(User user, Team team) {
        return Review.create("content", 3L, user, team);
    }
}
