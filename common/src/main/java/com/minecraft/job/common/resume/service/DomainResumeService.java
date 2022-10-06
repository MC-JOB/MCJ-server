package com.minecraft.job.common.resume.service;

import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeRepository;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.minecraft.job.common.support.Preconditions.require;

@Service
@Transactional
@RequiredArgsConstructor
public class DomainResumeService implements ResumeService {

    private final ResumeRepository resumeRepository;

    private final UserRepository userRepository;

    @Override
    public Resume create(Long userId, String title, String content, String trainingHistory) {
        User user = userRepository.findById(userId).orElseThrow();

        Resume resume = Resume.create(title, content, trainingHistory, user);

        return resumeRepository.save(resume);
    }

    @Override
    public void update(Long resumeId, Long userId, String title, String content, String trainingHistory) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        require(resume.ofUser(user));

        resume.update(title, content, trainingHistory);
    }
}
