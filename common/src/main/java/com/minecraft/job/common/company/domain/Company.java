package com.minecraft.job.common.company.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.OffsetDateTime;

import static com.minecraft.job.common.util.Preconditions.require;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    private Company(String email, String password, String name, String description, String logo) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.score = 0;
    }

    public static Company create(String email, String password, String name, String description, String logo) {
        require(Strings.isNotBlank(email));
        require(Strings.isNotBlank(password));
        require(Strings.isNotBlank(name));
        require(Strings.isNotBlank(description));

        return new Company(email, password, name, description, logo);
    }
}
