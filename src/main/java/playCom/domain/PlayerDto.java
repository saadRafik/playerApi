package playCom.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
