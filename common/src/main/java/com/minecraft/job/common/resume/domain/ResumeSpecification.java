package com.minecraft.job.common.resume.domain;

import com.minecraft.job.common.user.domain.User;
import org.springframework.data.jpa.domain.Specification;

public class ResumeSpecification {

    public static Specification<Resume> likeTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Resume> likeContent(String content) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("content"), "%" + content + "%");
    }

    public static Specification<Resume> likeTrainingHistory(String trainingHistory) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("trainingHistory"), "%" + trainingHistory + "%");
    }

    public static Specification<Resume> equalUser(User user) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
    }
}
