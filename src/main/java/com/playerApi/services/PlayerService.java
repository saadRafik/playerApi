package com.playerApi.services;

import com.playerApi.domain.PlayerDto;
import com.playerApi.dataAccess.PlayerRepository;
import com.playerApi.domain.Player;
import com.playerApi.exceptionHandlers.PlayerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // Adding logging capability
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerDto getPlayerDTOById(String id) {
        log.info("Fetching player with ID: {}", id);
        return new PlayerDto(findPlayerById(id));
    }

    public List<String> getMonsters(String id) {
        log.info("Fetching monsters for player ID: {}", id);
        return findPlayerById(id).getMonsters();
    }

    @Transactional
    public PlayerDto addExperience(String id, int xp) {
        Player player = findPlayerById(id);
        log.info("Adding {} XP to player ID: {}", xp, id);
        player.gainExperience(xp);
        return new PlayerDto(savePlayer(player));
    }

    @Transactional
    public PlayerDto levelUp(String id) {
        Player player = findPlayerById(id);
        log.info("Leveling up player ID: {}", id);
        player.levelUp();
        return new PlayerDto(savePlayer(player));
    }

    @Transactional
    public PlayerDto addMonster(String id, String monsterId) {
        Player player = findPlayerById(id);

        if (player.getMonsters().size() >= player.getMaxMonsters()) {
            log.warn("Player ID: {} has reached the monster limit", id);
            throw new IllegalStateException("Le joueur a atteint la limite de monstres.");
        }

        log.info("Adding monster {} to player ID: {}", monsterId, id);
        player.getMonsters().add(monsterId);
        return new PlayerDto(savePlayer(player));
    }

    @Transactional
    public void deletePlayer(String id) {
        log.info("Deleting player with ID: {}", id);
        if (!playerRepository.existsById(id)) {
            log.warn("Player ID: {} does not exist", id);
            throw new PlayerNotFoundException("Le joueur avec l'ID " + id + " est introuvable.");
        }
        playerRepository.deleteById(id);
    }

    private Player findPlayerById(String id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Player with ID: {} not found", id);
                    return new PlayerNotFoundException("Joueur inexistant: " + id);
                });
    }

    private Player savePlayer(Player player) {
        return playerRepository.save(player);
    }
}
