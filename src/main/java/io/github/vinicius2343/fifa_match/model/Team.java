package io.github.vinicius2343.fifa_match.model;

import io.github.vinicius2343.fifa_match.enums.TeamType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TeamType type;

    private String league;

    private String country;

    private Integer rating;
}
