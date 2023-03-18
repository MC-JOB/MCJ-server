package com.minecraft.job.api.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minecraft.job.api.fixture.UserFixture;
import com.minecraft.job.api.security.user.DefaultMcjUser;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class ApiTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    protected User prepareLoggedInUser() {
        return prepareLoggedInUserInternal("test@email.com");
    }

    protected User prepareLoggedInUser(String email) {
        return prepareLoggedInUserInternal(email);
    }

    private User prepareLoggedInUserInternal(String email) {
        String password = passwordEncoder.encode("1234");

        User user = userRepository.save(UserFixture.create(email, password));

        userRepository.flush();

        login(user.getId());

        return user;
    }

    private void login(Long id) {
        DefaultMcjUser mcjUser = (DefaultMcjUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        mcjUser.setId(id);
    }
}
