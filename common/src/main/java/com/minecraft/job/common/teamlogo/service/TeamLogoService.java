package com.minecraft.job.common.teamlogo.service;

import com.minecraft.job.common.teamlogo.domain.TeamLogo;

public interface TeamLogoService {

    TeamLogo create(Long teamId, Long userId, String name, String savedName, Long size);

    TeamLogo update(
            Long teamLogoId, Long teamId, Long userId,
            String name, String savedName, Long size
    );
}
