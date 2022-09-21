package com.minecraft.job.minecraftjob.domain.company;

import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

import java.time.OffsetDateTime;

import static com.minecraft.job.minecraftjob.util.Preconditions.require;

@Getter
public class CompanyVo {

    private int id;

    private String email;

    private String password;

    private String name;

    private String description;

    private String logo;

    private int score;

    private final OffsetDateTime createAt = OffsetDateTime.now();

    public void update(String email, String password, String name, String description, String logo) {
        require(Strings.isNotBlank(email));
        require(Strings.isNotBlank(password));
        require(Strings.isNotBlank(name));
        require(Strings.isNotBlank(description));

        this.email = email;
        this.password = password;
        this.name = name;
        this.description = description;
        this.logo = logo;
    }

    private CompanyVo(String email, String password, String name, String description, String logo) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.score = 0;
    }

    public static CompanyVo create(String email, String password, String name, String description, String logo) {
        require(Strings.isNotBlank(email));
        require(Strings.isNotBlank(password));
        require(Strings.isNotBlank(name));
        require(Strings.isNotBlank(description));

        return new CompanyVo(email, password, name, description, logo);
    }
}
