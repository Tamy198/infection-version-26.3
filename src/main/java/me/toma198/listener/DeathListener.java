package me.toma198.listener;

import me.toma198.death.behaviour.DeathObserver;
import me.toma198.infection.Game;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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

    // Words associated with player killing another player
    String[] PLAYER_KILL_WORDS = {
            " by ",
            " fighting ",
            " showed ",
            " escape ",
            " as ",
            " to ",
            " of ",
            " hurt "
    };

    // Bug if a player contains these words in their name
    String[] NAMED_ITEM_KILL_WORDS = {
            " using ",
            " with ",
            " wielding "
    };

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        String deathMessage = PlainTextComponentSerializer.plainText().serialize(e.deathMessage());

        // TODO: check the players don't contain item kill words

        // Parse the attacker's (if any) name from the death message to see if killed by infected
        String attacker = "";
        for (String killWord : PLAYER_KILL_WORDS) {
            if (deathMessage.contains(killWord)) {
                attacker = deathMessage.substring(deathMessage.indexOf(killWord));

                boolean usedNamedItem = false;
                for (String itemKillWord : NAMED_ITEM_KILL_WORDS) {
                    if (attacker.contains(itemKillWord)) {
                        attacker = attacker.substring(killWord.length(),
                                attacker.indexOf(itemKillWord)).strip();
                        usedNamedItem = true;
                        break;
                    }
                }

                if (!usedNamedItem) {
                    attacker = attacker.substring(killWord.length());
                }
                break;
            }
        }

        // Check if the attacker is one of the infected
        Player dead = e.getEntity();
        for (Player infected : gameState.toPlayerObjectList(gameState.getInfected())) {
            if (infected.getName().equals(attacker)) {
                gameState.addToConversionList(dead.getUniqueId());
                break;
            }
        }

        notifyDeathObservers(dead);
    }
}
