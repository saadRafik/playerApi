package com.playerApi.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "players")
public class Player {
    @Id
    private String id;
    private int level;
    private int experience;
    private List<String> monsters = new ArrayList<>();

    public void gainExperience(int xp) {
        this.experience += xp;
        checkLevelUp();
    }

    private void checkLevelUp() {
        while (this.experience >= getLevelUpThreshold()) {
            this.experience -= getLevelUpThreshold();
            this.level++;
        }
    }

    public void levelUp() {
        this.level++;
        this.experience = 0; // Réinitialise l'XP après un level-up manuel
    }

    public int getLevelUpThreshold() {
        return 100 + (this.level * 50); // Exemple : chaque niveau demande plus d'XP
    }

    public int getMaxMonsters() {
        return 10 + this.level; // Exemple : la capacité de monstres augmente avec le niveau
    }
}
