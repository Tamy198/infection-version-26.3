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
        System.out.println("Entity damage event");
        // Direct kill
        if (e.getEntityType() == EntityType.PLAYER) {
            Entity attacker = e.getDamager();
            Player victim = Bukkit.getPlayer(e.getEntity().getUniqueId());

            System.out.println("Victim health: " + victim.getHealth() + " finale damage: " + e.getDamage());
            boolean victimDeath = (victim.getHealth() - e.getDamage()) <= 0;
            System.out.println("Registered victim death: " + victimDeath + " of " + victim);
            System.out.println("Attacker: " + attacker);

            /*
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (victim.isDead()) {
                    victimDeath = true;
                }
            }, 1L);
             */
            System.out.println("hi: " + gameState.getInfected().contains(attacker.getUniqueId()));
            if (victimDeath && gameState.getInfected().contains(attacker.getUniqueId())) {
                gameState.addToConversionList(victim.getUniqueId());
                System.out.println("Should run conversion");
            }
        }
    }
}
