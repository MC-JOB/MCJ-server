package com.minecraft.job.common.team.domain;

import com.minecraft.job.common.user.domain.User;
import org.springframework.data.jpa.domain.Specification;

public class TeamSpecification {

    public static Specification<Team> likeName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Team> likeDescription(String description) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<Team> equalUser(User user) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
    }
}
