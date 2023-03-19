package com.minecraft.job.api.service;

import com.minecraft.job.api.component.JwtComponent;
import com.minecraft.job.api.security.user.McjUserException;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.minecraft.job.api.component.JwtType.ACCESS;
import static com.minecraft.job.api.component.JwtType.REFRESH;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultAuthAppService implements AuthAppService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtComponent jwtComponent;

    @Override
    public Pair<String, String> login(String email, String password) {
        User user = userRepository.getByEmail(email).orElseThrow(
                () -> new McjUserException("not found User", UNAUTHORIZED)
        );

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new McjUserException("Invalid Password", UNAUTHORIZED);

        String accessToken = jwtComponent.issue(user.getId(), user.getEmail(), ACCESS);
        String refreshToken = jwtComponent.issue(user.getId(), user.getEmail(), REFRESH);

        return Pair.of(accessToken, refreshToken);
    }
}
