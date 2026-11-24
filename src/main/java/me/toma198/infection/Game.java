package me.toma198.infection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    // Store a HashSet of UUIDs as it's more compatable with players leaving and rejoining
    // since Player objects can change between sessions (I believe lol) and aims to prevent
    // the bug of player UUID duplication
    private final Set<UUID> players = new HashSet<>();
    private Set<UUID> infected = new HashSet<>();
    private Set<UUID> innocents = new HashSet<>();

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
        //innocentsToRemove.add(player);
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
        List<UUID> playerRandomisation = new ArrayList<>(players);

        // Ensures infected list is randomly selected every time the infection command is run
        Collections.shuffle(playerRandomisation);
        infected = new HashSet<>(playerRandomisation.subList(0, infectedCount));
        if (players.size() > 1) {
            innocents = new HashSet<>(playerRandomisation.subList(infectedCount, players.size()));
        }
    }
}
