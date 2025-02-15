package com.playerApi.domain;

import java.util.*;

@Document("players")
public class Player {
    @Id
    private String id;
    private int level = 0;
    private int experience = 50;
    private List<String> monsters = new ArrayList<>();

    public void gainExperience(int xp) {
        experience += xp;
        while (experience >= getLevelUpThreshold()) {
            levelUp();
        }
    }

    private void levelUp() {
        experience = 0;
        level++;
    }

    public int getLevelUpThreshold() {
        return (int) (50 * Math.pow(1.1, level));
    }
}