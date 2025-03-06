package playCom.controller;

import playCom.services.PlayerService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;
    private final RestTemplate restTemplate;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
        this.restTemplate = new RestTemplate();
    }

    // Helper method to validate token
    private boolean isTokenValid(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8080/auth/validate",
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlayer(@PathVariable String id, @RequestHeader("Authorization") String token) {
        if (!isTokenValid(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized: Invalid or expired token"));
        }
        return ResponseEntity.ok(playerService.getPlayerDTOById(id));
    }

    @GetMapping("/{id}/monsters")
    public ResponseEntity<?> getMonsters(@PathVariable String id, @RequestHeader("Authorization") String token) {
        if (!isTokenValid(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized: Invalid or expired token"));
        }
        return ResponseEntity.ok(playerService.getMonsters(id));
    }

    @PostMapping("/{id}/gain-experience")
    public ResponseEntity<?> gainExperience(@PathVariable String id, @RequestParam int xp, @RequestHeader("Authorization") String token) {
        if (!isTokenValid(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized: Invalid or expired token"));
        }
        return ResponseEntity.ok(playerService.addExperience(id, xp));
    }

    @PostMapping("/{id}/level-up")
    public ResponseEntity<?> manualLevelUp(@PathVariable String id, @RequestHeader("Authorization") String token) {
        if (!isTokenValid(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized: Invalid or expired token"));
        }
        return ResponseEntity.ok(playerService.levelUp(id));
    }

    @PostMapping("/{id}/add-monster")
    public ResponseEntity<?> addMonster(@PathVariable String id, @RequestParam String monsterId, @RequestHeader("Authorization") String token) {
        if (!isTokenValid(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized: Invalid or expired token"));
        }
        return ResponseEntity.ok(playerService.addMonster(id, monsterId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable String id, @RequestHeader("Authorization") String token) {
        if (!isTokenValid(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized: Invalid or expired token"));
        }
        playerService.deletePlayer(id);
        return ResponseEntity.ok(Map.of("message", "Player deleted successfully"));
    }
}
