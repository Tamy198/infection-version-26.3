package me.toma198.infection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Game {
    // Store a list of UUIDs as it's more compatable with players leaving and rejoining
    // since Player objects can change between sessions (I believe lol)
    private final List<UUID> players = new ArrayList<>();
    private List<UUID> infected = new ArrayList<>();
    private List<UUID> innocents = new ArrayList<>();

    public List<UUID> getPlayers() {
        for (UUID player : players) {
            System.out.println("Player list: " + player.toString());
        }
        return copyList(players);
    }

    private List<UUID> copyList(List<UUID> players) {
        List<UUID> listCopy = new ArrayList<>();
        for (UUID player : players) {
            listCopy.add(player);
        }
        return listCopy;
    }

    public List<UUID> getInfected() {
        if (infected.isEmpty()) return null;
        return infected;
    }

    public List<UUID> getInnocents() {
        if (innocents.isEmpty()) return null;
        return innocents;
    }

    public void addPlayer(UUID player) {
        players.add(player);
    }

    public void removePlayer(UUID player) {
        players.remove(player);
    }

    public void assignInfected(int infectedCount) {
        Collections.shuffle(players);
        infected = players.subList(0, infectedCount);
        if (players.size() > 1) {
            innocents = players.subList(infectedCount, players.size());
        }
    }
}
