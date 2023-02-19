package com.minecraft.job.common.review.domain;

import com.minecraft.job.common.team.domain.Team;
import com.minecraft.job.common.user.domain.User;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecification {

    public static Specification<Review> likeContent(String content) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("content"), "%" + content + "%");
    }

    public static Specification<Review> equalTeam(Team team) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("team"), team);
    }

    public static Specification<Review> equalUser(User user) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
    }
}
