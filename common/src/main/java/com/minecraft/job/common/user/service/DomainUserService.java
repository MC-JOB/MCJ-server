package com.minecraft.job.common.user.service;

import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.minecraft.job.common.support.ErrorCode.ALREADY_USED_EMAIL;
import static com.minecraft.job.common.support.Preconditions.validate;

@Service
@Transactional
@RequiredArgsConstructor
public class DomainUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(String email, String password, String nickname, String interest, Long age) {
        validate(userRepository.getByEmail(email).isEmpty(), ALREADY_USED_EMAIL);

        User user = User.create(email, password, nickname, interest, age);

        return userRepository.save(user);
    }
}
