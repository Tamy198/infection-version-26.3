package me.toma198.infection;

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

        System.out.println("HII!!!, should run imposterDeath");
        player.setGameMode(GameMode.SPECTATOR);
        gameState.removeFromInfected(player.getUniqueId());

        System.out.println("Player " + player.getName() + " has died!");
        System.out.println(gameState.getInfected());

        if (gameState.getInfected().isEmpty()) {
            System.out.println("1.Hi");
            for (Player p : gameState.toPlayerObjectList(gameState.getPlayers())) {
                p.sendMessage(Component.text("Innocents have won!").color(NamedTextColor.GREEN));
            }
        }
    }
}
