package com.minecraft.job.api.controller.dto;

public class TeamUpdateDto {

    public record TeamUpdateRequest(
            Long teamId, String name,
            String description, Long memberNum
    ) {
    }
}
