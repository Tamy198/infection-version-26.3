package me.toma198.infection;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface RespawnObserver {
    void onRespawnEvent(Player dead, Game gameState, Plugin plugin);
}
