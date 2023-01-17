package com.minecraft.job.common.user.domain;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> equalEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }

    public static Specification<User> equalNickname(String nickname) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("nickname"), nickname);
    }

    public static Specification<User> likeInterest(String interest) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("interest"), "%" + interest + "%");
    }
}
