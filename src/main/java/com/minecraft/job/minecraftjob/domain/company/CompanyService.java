package com.minecraft.job.minecraftjob.domain.company;

public interface CompanyService {

    void create(String email, String password, String name, String description, String logo);
}
