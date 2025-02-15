package com.playerApi.controller;

import com.playerApi.domain.PlayerDto;
import com.playerApi.services.PlayerService;

@RestController
@RequestMapping("/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable String id) {
        return ResponseEntity.ok(playerService.getPlayerDTOById(id));
    }

    @PostMapping("/{id}/gain-experience")
    public ResponseEntity<PlayerDto> gainExperience(@PathVariable String id, @RequestParam int xp) {
        return ResponseEntity.ok(playerService.addExperience(id, xp));
    }
}