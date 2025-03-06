package playCom.services;

import playCom.dataAccess.PlayerRepository;
import playCom.domain.Player;
import playCom.domain.PlayerDto;
import playCom.exceptionHandlers.PlayerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // Adding logging capability
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final WebClient webClient = WebClient.create("http://authentification-api:8080");

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

    public boolean validateToken(String token) {
        String response = webClient.post()
                .uri("/auth/validate")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response != null && !response.contains("Token expiré") && !response.contains("Token invalide");
    }

    private Player savePlayer(Player player) {
        return playerRepository.save(player);
    }
}
