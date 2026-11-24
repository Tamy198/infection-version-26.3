package me.toma198.Listeners;

import me.toma198.infection.DeathObserver;
import me.toma198.infection.Game;
import me.toma198.infection.RespawnObserver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class DeathListener implements Listener {
    private final Game gameState;
    private final Plugin plugin;

    private final List<DeathObserver> observers = new ArrayList<>();

    public DeathListener(Game gameState, Plugin plugin) {
        this.gameState = gameState;
        this.plugin = plugin;
    }

    public void addDeathObserver(DeathObserver o) {
        observers.add(o);
    }

    public void removeDeathObserver(DeathObserver o) {
        observers.remove(o);
    }

    public void notifyDeathObservers(Player dead) {
        for (DeathObserver observer : observers) {
            observer.onDeathEvent(dead, gameState, plugin);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player dead = e.getEntity();
        System.out.println(dead.getName() + " has died!");

        notifyDeathObservers(dead);
    }
}
