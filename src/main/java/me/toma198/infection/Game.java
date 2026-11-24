package me.toma198.infection;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Game {
    // Store a list of UUIDs as it's more compatable with players leaving and rejoining
    // since Player objects can change between sessions (I believe lol)
    private final List<UUID> players = new ArrayList<>();
    private List<UUID> infected = new ArrayList<>();
    private List<UUID> innocents = new ArrayList<>();

    // To track who needs to be converted
    private final List<UUID> conversionList = new ArrayList<>();

    public List<UUID> getPlayers() {
        for (UUID player : players) {
            System.out.println("Player list: " + player.toString());
        }

        // Copy player list to ensure we aren't exposing the original to mutation outside the class
        return new ArrayList<>(players);
    }

    public List<UUID> getInfected() {
        if (infected.isEmpty()) return null;
        return new ArrayList<>(infected);
    }

    public List<UUID> getInnocents() {
        if (innocents.isEmpty()) return null;
        return new ArrayList<>(innocents);
    }

    public List<UUID> getConversionList() {
        return new ArrayList<>(conversionList);
    }

    public List<Player> toPlayerObjectList(List<UUID> playerList) {
        return playerList.stream()
                .map(Bukkit::getPlayer)
                .collect(Collectors.toList());
    }

    /* Might expose internal workings of the class??
    public void addPlayer(UUID player, List<UUID> list) {
        list.add(player);
    }

    public void removePlayer(UUID player, List<UUID> list) {
        list.remove(player);
    }
     */

    public void addToPlayers(UUID player) {
        players.add(player);
    }

    public void removeFromPlayers(UUID player) {
        players.remove(player);
    }

    public void addToInfected(UUID player) {
        infected.add(player);
    }

    public void removeFromInfected(UUID player) {
        infected.remove(player);
    }

    public void removeFromInnocents(UUID player) {
        innocents.remove(player);

    }

    public void addToConversionList(UUID player) {
        conversionList.add(player);
    }

    public void removeFromConversionList(UUID player) {
        conversionList.remove(player);
    }

    public void assignInfected(int infectedCount) {
        // Ensures infected list is randomly selected every time the infection command is run
        Collections.shuffle(players);
        infected = players.subList(0, infectedCount);
        if (players.size() > 1) {
            innocents = players.subList(infectedCount, players.size());
        }
    }
}
