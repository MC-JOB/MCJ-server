package com.minecraft.job.common.user.service;

import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DomainUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(String email, String password, String nickname, String interest, Long age) {
        User user = User.create(email, password, nickname, interest, age);

        return userRepository.save(user);
    }
}
