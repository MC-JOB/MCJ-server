package com.minecraft.job.common.recruitment.domain;

import java.util.EnumSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public enum RecruitmentStatus {
    CREATED, ACTIVATED, INACTIVATED, DELETED;

    public static final Set<RecruitmentStatus> CAN_MOVE_ACTIVATED = unmodifiableSet(EnumSet.of(CREATED, INACTIVATED));
    public static final Set<RecruitmentStatus> CAN_MOVE_INACTIVATED = unmodifiableSet(EnumSet.of(CREATED, ACTIVATED));
}
