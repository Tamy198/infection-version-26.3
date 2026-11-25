package me.toma198.Listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import me.toma198.infection.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JumpListener implements Listener {
    private final Game gameState;

    public JumpListener(Game gameState) {
        this.gameState = gameState;
    }

    @EventHandler
    public void onJump(PlayerJumpEvent e) {
        // Prevent movement when being infected
        Player player = e.getPlayer();
        if (gameState.getConversionList().contains(player.getUniqueId())) {
            e.setCancelled(true);
        }
    }
}
