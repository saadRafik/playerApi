package com.playerApi.services;

import com.playerApi.domain.PlayerDto;
import com.playerApi.dataAccess.PlayerRepository;
import com.playerApi.domain.Player;
import com.playerApi.exceptionHandlers.PlayerNotFoundException;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public PlayerDto getPlayerDTOById(String id) {
        return new PlayerDto(findPlayerById(id));
    }

    public PlayerDto addExperience(String id, int xp) {
        Player player = findPlayerById(id);
        player.gainExperience(xp);
        return new PlayerDto(playerRepository.save(player));
    }

    private Player findPlayerById(String id) {
        return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
    }
}
