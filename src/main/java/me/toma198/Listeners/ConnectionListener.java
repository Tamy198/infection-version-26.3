package me.toma198.Listeners;

import me.toma198.infection.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {
    private final Game gameState;

    public ConnectionListener(Game gameState) {
        this.gameState = gameState;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        gameState.addToPlayers(e.getPlayer().getUniqueId());
        System.out.println("Added player");
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        gameState.removeFromPlayers(e.getPlayer().getUniqueId());
        System.out.println("Removed player");
    }
}
