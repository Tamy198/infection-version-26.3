package me.toma198.infection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
    private final Set<UUID> dead = new HashSet<>();

    // To track who needs to be converted
    private final Set<UUID> conversionList = new HashSet<>();

    public List<UUID> getPlayers() {
        // Copy player list to ensure we aren't exposing the original to mutation outside the class
        return new ArrayList<>(players);
    }

    public List<UUID> getInfected() {
        return new ArrayList<>(infected);
    }

    public List<UUID> getInnocents() {
        return new ArrayList<>(innocents);
    }

    public List<UUID> getDead() {
        return new ArrayList<>(dead);
    }

    public List<UUID> getConversionList() {
        return new ArrayList<>(conversionList);
    }

    public List<Player> toPlayerObjectList(List<UUID> playerList) {
        return playerList.stream()
                .map(Bukkit::getPlayer)
                .collect(Collectors.toList());
    }

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

    public void addToDead(UUID player) {
        dead.add(player);
    }

    public void removeFromDead(UUID player) {
        dead.add(player);
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
        Random random = new Random();
        Collections.shuffle(playerRandomisation, random);
        infected = new HashSet<>(playerRandomisation.subList(0, infectedCount));

        if (players.size() > 1) {
            innocents = new HashSet<>(playerRandomisation.subList(infectedCount, players.size()));
        }
    }

    public void infectedWin() {
        // Use online players to avoid error with disconnects
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text("Imposters have won!").color(NamedTextColor.RED));
            NameTag.reviveNameTags(player);
        }
        resetGame();
    }

    public void innocentsWin() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text("Innocents have won!").color(NamedTextColor.GREEN));
            NameTag.reviveNameTags(player);
        }
        resetGame();
    }

    public void resetGame() {
        infected.clear();
        innocents.clear();
        dead.clear();
        conversionList.clear();
    }
}
