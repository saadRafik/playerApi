package com.playerApi.controller;

import com.playerApi.domain.PlayerDto;
import com.playerApi.services.PlayerService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable String id) {
        return ResponseEntity.ok(playerService.getPlayerDTOById(id));
    }

    @GetMapping("/{id}/monsters")
    public ResponseEntity<List<String>> getMonsters(@PathVariable String id) {
        return ResponseEntity.ok(playerService.getMonsters(id));
    }

    @PostMapping("/{id}/gain-experience")
    public ResponseEntity<PlayerDto> gainExperience(
            @PathVariable String id,
            @RequestParam @Min(1) int xp) {
        return ResponseEntity.ok(playerService.addExperience(id, xp));
    }

    @PostMapping("/{id}/level-up")
    public ResponseEntity<PlayerDto> manualLevelUp(@PathVariable String id) {
        return ResponseEntity.ok(playerService.levelUp(id));
    }

    @PostMapping("/{id}/add-monster")
    public ResponseEntity<PlayerDto> addMonster(@PathVariable String id, @RequestParam String monsterId) {
        return ResponseEntity.ok(playerService.addMonster(id, monsterId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }
}
