package com.playerApi.dataAccess;

import com.playerApi.domain.Player;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {}