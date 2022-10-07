package com.minecraft.job.api.fixture;

import com.minecraft.job.common.resume.domain.Resume;
import com.minecraft.job.common.user.domain.User;

public class ResumeFixture {

    public static Resume create(User user) {
        return Resume.create("title", "content", "trainingHistory", user);
    }

    public static Resume getFakeResume(User user) {
        return Resume.create("fakeTitle", "fakeContent", "fakeTrainingHistory", user);
    }
}
