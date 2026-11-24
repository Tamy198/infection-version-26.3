package me.toma198.infection;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Conversion implements RespawnObserver {
    @Override
    public void onRespawnEvent(Player player, Game gameState, Plugin plugin) {
        System.out.println("In conversion class");
        System.out.println(player.getUniqueId());
        System.out.println("Infected list: " + gameState.getInfected());
        System.out.println("Conversion list: " + gameState.getConversionList());
        //System.out.println("Innocent list: " + gameState.getInnocents());

        if (!(gameState.getConversionList().contains(player.getUniqueId()))) {
            //gameState.removeFromConversionList(player.getUniqueId());
            System.out.println("Didn't run conversion");
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
            System.out.println("Executed potion removal after 30 seconds!");
            player.removePotionEffect(PotionEffectType.SLOWNESS);
            player.removePotionEffect(PotionEffectType.RESISTANCE);
            player.removePotionEffect(PotionEffectType.REGENERATION);
            gameState.removeFromInnocents(player.getUniqueId());
        }, 600L);
    }

    // Apply freezing every tick for 30 seconds
    private void freezing(Player player, Plugin plugin) {
        System.out.println("Freezing method ran");

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
