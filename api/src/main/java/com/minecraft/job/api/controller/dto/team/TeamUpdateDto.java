package com.minecraft.job.api.controller.dto.team;

public class TeamUpdateDto {

    public record TeamUpdateRequest(
            Long teamId, String name,
            String description, Long memberNum
    ) {
    }
}
