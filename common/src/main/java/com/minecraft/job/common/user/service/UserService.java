package com.minecraft.job.common.user.service;

import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserSearchType;
import org.springframework.data.domain.Page;

public interface UserService {

    User create(
            String email, String password,
            String nickname, String interest, Long age
    );

    void changeInformation(Long userId, String nickname, String interest, Long age);

    void changePassword(Long userId, String password, String newPassword);

    void activate(Long userId);

    void inactivate(Long userId);

    Page<User> getUsers(UserSearchType searchType, String searchName, int page);
}
