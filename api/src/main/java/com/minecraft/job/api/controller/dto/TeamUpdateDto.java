package com.minecraft.job.api.controller.dto;

public class TeamUpdateDto {

    public record TeamUpdateRequest(
            Long teamId, Long userId, String name,
            String description, String logo, Long memberNum
    ) {
    }
}
