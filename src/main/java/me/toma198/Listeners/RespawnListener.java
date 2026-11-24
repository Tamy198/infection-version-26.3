package me.toma198.Listeners;

import me.toma198.infection.Conversion;
import me.toma198.infection.Game;
import me.toma198.infection.RespawnObserver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class RespawnListener implements Listener {
    private final Game gameState;
    private final Plugin plugin;

    private final List<RespawnObserver> observers = new ArrayList<>();

    public RespawnListener(Game gameState, Plugin plugin) {
        this.gameState = gameState;
        this.plugin = plugin;
    }

    public void addRespawnObserver(RespawnObserver o) {
        observers.add(o);
    }

    public void removeRespawnObserver(RespawnObserver o) {
        observers.remove(o);
    }

    public void notifyRespawnObservers(Player player) {
        for (RespawnObserver observer : observers) {
            observer.onRespawnEvent(player, gameState, plugin);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        Location location = player.getLocation();

        // Delay teleportation to where they died until after the player respawns relative to
        // the client
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(location), 1L);

        System.out.println("Conversion class should be notified");
        notifyRespawnObservers(player);

        /*
        if (gameState.getConversionList().contains(player.getUniqueId())) {
            //gameState.removeFromConversionList(player.getUniqueId());

            new Conversion().onRespawnEvent(player, gameState, plugin);
        }
         */
    }
}
