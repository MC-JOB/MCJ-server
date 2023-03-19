package com.minecraft.job.api.service;

import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultUserAppService implements UserAppService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(String email, String password, String nickname, String interest, Long age) {
        String encodePassword = passwordEncoder.encode(password);

        return userService.create(email, encodePassword, nickname, interest, age);
    }
}
