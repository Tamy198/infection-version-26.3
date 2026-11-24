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

        /*
        List<Player> innocents = gameState.toPlayerObjectList(gameState.getInnocents());
        List<Player> infected = gameState.toPlayerObjectList(gameState.getInfected());

        // END the imposter
        if (infected.contains(dead)) {
            System.out.println("HII!!!, should run imposterDeath");
            //imposterDeath(dead, infected);
            return;
        }

        // Innocent death or conversion
        System.out.println("HII!!!, should run innocentDeath and conversion");
        if (innocents.contains(dead)) {
            //innocentDeath(dead, infected, innocents);
        }
         */
    }

    /*
    public void imposterDeath(Player player, List<Player> infected) {
        player.setGameMode(GameMode.SPECTATOR);
        gameState.removeFromInfected(player.getUniqueId());

        System.out.println("Player " + player.getName() + " has died!");
        System.out.println(infected);

        if (infected.isEmpty()) {
            for (Player p : gameState.toPlayerObjectList(gameState.getPlayers())) {
                p.sendMessage(Component.text("Innocents have won!").color(NamedTextColor.GREEN));
            }
        }
    }
     */

    /*
    public void innocentDeath(Player player, List<Player> infected, List<Player> innocents) {
        // Imposter coordinates
        double impX;
        double impY;
        double impZ;

        // Innocent coordinates
        double X = player.getLocation().getX();
        double Y = player.getLocation().getY();
        double Z = player.getLocation().getZ();

        // Absolute distance squared of imposter to dead player in each dimension
        double absX;
        double absY;
        double absZ;
        double distFromImp;

        // Calculate distance of the player who died to the imposter
        boolean activateConversion = false;
        for (Player p : infected) {
            impX = p.getLocation().getX();
            impY = p.getLocation().getY();
            impZ = p.getLocation().getZ();

            absX = Math.pow((X - impX), 2);
            absY = Math.pow((Y - impY), 2);
            absZ = Math.pow((Z - impZ), 2);

            distFromImp = Math.sqrt(absX + absY + absZ);

            // if within 30 blocks, begin conversion to imposter
            if (distFromImp <= 30) {
                activateConversion = true;
                gameState.addToConversionList(p.getUniqueId());
            }
        }

        // else set innocent to spectator
        if (!activateConversion) {
            player.setGameMode(GameMode.SPECTATOR);
            gameState.removeFromInnocents(player.getUniqueId());
        }

        // Inform if imposters have won
        if (innocents.isEmpty()) {
            for (Player p : gameState.toPlayerObjectList(gameState.getPlayers())) {
                p.sendMessage(Component.text("Imposters have won!").color(NamedTextColor.RED));
            }
        }

        // Activate a respawn event a tick later
        // ** DOESN'T WORK? - RESPAWNS WRONG POSITION? **
        player.setRespawnLocation(player.getLocation());
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.spigot().respawn(), 1L);
    }
     */
}
