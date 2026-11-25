package me.toma198.death.behaviour;

import me.toma198.infection.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        gameState.addToDead(player.getUniqueId());

        // Notify imposters of the death
        for (Player p : gameState.toPlayerObjectList(gameState.getInfected())) {
            p.sendMessage(Component.text("Imposter " + player.getName() + " has died!")
                    .color(NamedTextColor.RED));
        }

        if (gameState.getInfected().isEmpty()) {
            gameState.innocentsWin();
        }
    }
}
