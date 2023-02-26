package com.minecraft.job.common.recruitmentProcess.domain;

import com.minecraft.job.common.recruitment.domain.Recruitment;
import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.user.domain.User;
import org.springframework.data.jpa.domain.Specification;

public class RecruitmentProcessSpecification {

    public static Specification<RecruitmentProcess> equalUser(User user) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
    }

    public static Specification<RecruitmentProcess> equalRecruitment(Recruitment recruitment) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("recruitment"), recruitment);
    }

    public static Specification<RecruitmentProcess> equalResume(Resume resume) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("resume"), resume);
    }
}
