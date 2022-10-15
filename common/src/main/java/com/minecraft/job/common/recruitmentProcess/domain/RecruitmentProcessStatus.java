package com.minecraft.job.common.recruitmentProcess.domain;

import java.util.EnumSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public enum RecruitmentProcessStatus {

    WAITING, IN_PROGRESS, PASSED, CANCELED, FAILED;

    public static final Set<RecruitmentProcessStatus> CAN_MOVE_CANCELED = unmodifiableSet(EnumSet.of(WAITING, IN_PROGRESS));

    public static final Set<RecruitmentProcessStatus> CAN_MOVE_FAILED = unmodifiableSet(EnumSet.of(WAITING, IN_PROGRESS));
}
