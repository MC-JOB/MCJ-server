package com.minecraft.job.api.service;

import org.springframework.data.util.Pair;

public interface AuthAppService {
    Pair<String, String> login(String email, String password);
}
