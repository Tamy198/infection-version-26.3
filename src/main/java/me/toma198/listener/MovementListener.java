package me.toma198.listener;

import me.toma198.infection.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {
    private final Game gameState;

    public MovementListener(Game gameState) {
        this.gameState = gameState;
    }

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent e) {
        // Prevent movement when being infected
        Player player = e.getPlayer();
        if (gameState.getConversionList().contains(player.getUniqueId())) {
            e.setTo(e.getFrom());
            e.setCancelled(true);
        }
    }
}
