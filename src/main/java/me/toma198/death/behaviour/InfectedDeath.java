package me.toma198.death.behaviour;

import me.toma198.infection.Game;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class InfectedDeath implements DeathObserver {
    @Override
    public void onDeathEvent(Player player, Game gameState, Plugin plugin) {
        if (!(gameState.getInfected().contains(player.getUniqueId()))) {
            return;
        }

        player.setGameMode(GameMode.SPECTATOR);
        gameState.removeFromInfected(player.getUniqueId());

        if (gameState.getInfected().isEmpty()) {
            gameState.innocentsWin();
        }
    }
}
