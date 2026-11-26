package me.toma198.respawn.behaviour;

import me.toma198.infection.Game;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Conversion implements RespawnObserver {
    private static final float DEFAULT_MOVEMENT_SPEED = 0.2f;

    @Override
    public void onRespawnEvent(Player player, Game gameState, Plugin plugin) {
        // Non-infected innocent death
        if (!(gameState.getConversionList().contains(player.getUniqueId()))) {
            player.setGameMode(GameMode.SPECTATOR);
            return;
        }

        // During the conversion, the player will be invincible and immobile
        // (but can still be punched)
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,
                    600, 255, true, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,
                    600, 255, true, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                    600, 255, true, true));
        }, 1L);


        // Apply freezing every tick for 30 seconds
        player.playSound(player.getLocation(), Sound.ENTITY_CREAKING_FREEZE, 1000f, 1f);
        player.setFreezeTicks(player.getMaxFreezeTicks());
        freezing(player, plugin);

        // Join the imposter team
        gameState.addToInfected(player.getUniqueId());

        // Conversion lasts 30 seconds
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.removePotionEffect(PotionEffectType.SLOWNESS);
            player.removePotionEffect(PotionEffectType.RESISTANCE);
            player.removePotionEffect(PotionEffectType.REGENERATION);

            gameState.removeFromConversionList(player.getUniqueId());

            //player.setWalkSpeed(DEFAULT_MOVEMENT_SPEED);

            // Inform if imposters have won (with suspense after the conversion lol)
            if (gameState.getInfected().size() >=
                    gameState.getPlayers().size() - gameState.getDead().size()) {
                gameState.infectedWin();
            }
        }, 600L);
    }

    // Apply freezing every tick for 30 seconds
    private void freezing(Player player, Plugin plugin) {
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                player.setFreezeTicks(player.getMaxFreezeTicks());
                player.playSound(player.getLocation(), Sound.ENTITY_CREAKING_FREEZE,
                        10000f, 1f);

                ticks++;
                if (ticks >= 20 * 30) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
