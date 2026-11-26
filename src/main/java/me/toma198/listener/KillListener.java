package me.toma198.listener;

import me.toma198.infection.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class KillListener implements Listener {
    private final Game gameState;

    public KillListener(Game gameState) {
        this.gameState = gameState;
    }

    @EventHandler
    public void onKill(EntityDamageByEntityEvent e) {
        // Direct kill
        if (e.getEntityType() == EntityType.PLAYER) {
            Entity attacker = e.getDamager();
            Player victim = Bukkit.getPlayer(e.getEntity().getUniqueId());

            boolean victimDeath = (victim.getHealth() - e.getDamage()) <= 0;

            /*
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (victim.isDead()) {
                    victimDeath = true;
                }
            }, 1L);
             */

            if (victimDeath && gameState.getInfected().contains(attacker.getUniqueId())) {
                gameState.addToConversionList(victim.getUniqueId());
            }
        }
    }
}
