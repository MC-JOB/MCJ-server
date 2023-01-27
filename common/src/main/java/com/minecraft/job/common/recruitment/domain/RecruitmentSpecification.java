package com.minecraft.job.common.recruitment.domain;

import com.minecraft.job.common.team.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class RecruitmentSpecification {

    public static Specification<Recruitment> likeTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Recruitment> likeContent(String content) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("content"), "%" + content + "%");
    }

    public static Specification<Recruitment> equalTeam(Team team) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("team"), team);
    }
}
