package me.toma198.infection;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface DeathObserver {
    void onDeathEvent(Player dead, Game gameState, Plugin plugin);
}
