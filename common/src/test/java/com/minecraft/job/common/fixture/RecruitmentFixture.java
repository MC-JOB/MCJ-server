package com.minecraft.job.common.fixture;

import com.minecraft.job.common.recruitment.domain.Recruitment;

public class RecruitmentFixture {

    public static Recruitment create(){
        return Recruitment.create("title", "content", TeamFixture.create());
    }
}
