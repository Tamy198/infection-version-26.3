package me.toma198.death.behaviour;

import me.toma198.infection.Game;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class InfectedDeath implements DeathObserver {
    @Override
    public void onDeathEvent(Player player, Game gameState, Plugin plugin) {
        System.out.println("1. Infected death");

        if (!(gameState.getInfected().contains(player.getUniqueId()))) {
            return;
        }

        System.out.println("HII!!!, should run imposterDeath");

        player.setGameMode(GameMode.SPECTATOR);
        gameState.removeFromInfected(player.getUniqueId());

        System.out.println("Player " + player.getName() + " has died!");
        System.out.println(gameState.getInfected());

        if (gameState.getInfected().isEmpty()) {
            gameState.innocentsWin();
        }
    }
}
