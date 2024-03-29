package com.minecraft.job.common.resume.service;

import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.resume.domain.ResumeRepository;
import com.minecraft.job.common.resume.domain.ResumeSearchType;
import com.minecraft.job.common.resume.domain.ResumeSpecification;
import com.minecraft.job.common.user.domain.User;
import com.minecraft.job.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.minecraft.job.common.resume.domain.ResumeSearchType.*;
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

    @Override
    public void activate(Long resumeId, Long userId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        require(resume.ofUser(user));

        resume.activate();
    }

    @Override
    public void inactivate(Long resumeId, Long userId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        require(resume.ofUser(user));

        resume.inactivate();
    }

    @Override
    public void delete(Long resumeId, Long userId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        require(resume.ofUser(user));

        resume.delete();
    }

    @Override
    public Page<Resume> getResumeList(ResumeSearchType searchType, String searchName, Pageable pageable) {
        Specification<Resume> spec = getResumeSpecification(searchType, searchName);

        return resumeRepository.findAll(spec, pageable);
    }

    @Override
    public Resume getResume(Long userId) {
        Resume resume = resumeRepository.findByUser_id(userId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        require(resume.ofUser(user));

        return resume;
    }

    @Override
    public Page<Resume> getMyResumeList(ResumeSearchType searchType, String searchName, Pageable pageable, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Specification<Resume> spec = getResumeSpecification(searchType, searchName).and(ResumeSpecification.equalUser(user));

        return resumeRepository.findAll(spec, pageable);
    }

    private Specification<Resume> getResumeSpecification(ResumeSearchType searchType, String searchName) {
        Specification<Resume> spec = null;

        if(searchType == ALL) {
            spec = Specification.where(null);
        }
        if (searchType == TITLE) {
            spec = Specification.where(ResumeSpecification.likeTitle(searchName));
        }
        if (searchType == CONTENT) {
            spec = Specification.where(ResumeSpecification.likeContent(searchName));
        }
        if (searchType == TRAININGHISTORY) {
            spec = Specification.where(ResumeSpecification.likeTrainingHistory(searchName));
        }
        if (searchType == USER) {
            User user = userRepository.findByNickname(searchName);
            spec = Specification.where(ResumeSpecification.equalUser(user));
        }
        return spec;
    }
}
