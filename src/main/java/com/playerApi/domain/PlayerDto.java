package com.playerApi.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {
    private String id;
    private int level;
    private int experience;
    private List<String> monsters;

    public PlayerDto(Player player) {
        this.id = player.getId();
        this.level = player.getLevel();
        this.experience = player.getExperience();
        this.monsters = player.getMonsters();
    }
}
