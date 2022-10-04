package com.minecraft.job.common.resume.domain;

import java.util.EnumSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public enum ResumeStatue {
    CREATED, ACTIVATED, INACTIVATED, DELETED;

    public static final Set<ResumeStatue> CAN_MOVE_ACTIVATED = unmodifiableSet(EnumSet.of(CREATED, INACTIVATED));
    public static final Set<ResumeStatue> CAN_MOVE_INACTIVATED = unmodifiableSet(EnumSet.of(CREATED, ACTIVATED));
    public static final Set<ResumeStatue> CAN_MOVE_DELETED = unmodifiableSet(EnumSet.of(CREATED, ACTIVATED, INACTIVATED));
}
