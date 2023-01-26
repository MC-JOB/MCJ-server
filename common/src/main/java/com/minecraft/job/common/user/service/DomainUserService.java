package com.minecraft.job.common.user.service;

import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import com.minecraft.job.common.user.domain.UserSearchType;
import com.minecraft.job.common.user.domain.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public void changeInformation(Long userId, String nickname, String interest, Long age) {
        User user = userRepository.findById(userId).orElseThrow();

        user.changeInformation(nickname, interest, age);
    }

    @Override
    public void changePassword(Long userId, String password, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow();

        user.changePassword(password, newPassword);
    }

    @Override
    public void activate(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        user.activate();
    }

    @Override
    public void inactivate(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        user.inactivate();
    }

    @Override
    public Page<User> getUsers(UserSearchType searchType, String searchName, int page) {

        Specification<User> spec = getUserSpecification(searchType, searchName);

        PageRequest pageRequest = PageRequest.of(page, 10);

        return userRepository.findAll(spec, pageRequest);
    }

    private Specification<User> getUserSpecification(UserSearchType searchType, String searchName) {
        Specification<User> spec = null;

        if(searchType == UserSearchType.NICKNAME) {
            spec = Specification.where(UserSpecification.equalNickname(searchName));
        }
        if(searchType == UserSearchType.EMAIL) {
            spec = Specification.where(UserSpecification.equalEmail(searchName));
        }
        if(searchType == UserSearchType.INTEREST) {
            spec = Specification.where(UserSpecification.likeInterest(searchName));
        }
        return spec;
    }
}
