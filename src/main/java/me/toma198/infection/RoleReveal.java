package me.toma198.infection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RoleReveal {
    private final Plugin plugin;

    public RoleReveal(Plugin plugin) {
        this.plugin = plugin;
    }

    public void revealRole(Game gameState) {
        List<Player> players = toPlayerList(gameState.getPlayers());
        List<Player> imposterList = toPlayerList(gameState.getInfected());

        List<Player> innocentList;
        if (players.size() > 1) {
            innocentList = toPlayerList(gameState.getInnocents());
        } else {
            innocentList = new ArrayList<>();
        }

        String threeSecondCountDown = ChatColor.GOLD + Integer.toString(3);
        String twoSecondCountDown = ChatColor.GOLD + Integer.toString(2);
        String oneSecondCountDown = ChatColor.GOLD + Integer.toString(1);
        String gameplayReminder = "Rmb chat/tab";

        // Countdown and imposter reveal
        printRoleTitle(threeSecondCountDown, players);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            printRoleTitle(twoSecondCountDown, players);
            System.out.println("Executed after delay!");

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                printRoleTitle(oneSecondCountDown, players);

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    // Inform imposters of their dubiousness
                    String subtitle = "";
                    String imposterMessage = ChatColor.RED + "You are the imposter:)";
                    for (Player player : imposterList) {
                        player.sendTitle(imposterMessage, subtitle, 10, 70, 20);
                        player.sendMessage("§cYou are the imposter, infect the innocents:)");
                        player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 10000f, 1f);
                    }

                    // Inform innocents of their innocence
                    String innocent = ChatColor.GREEN + "You are NOT the imposter:)";
                    for (Player player : innocentList) {
                        player.sendTitle(innocent, subtitle, 10, 70, 20);
                        player.sendMessage("§aYou are innocent:)");
                        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0,
                                10000f, 1f);
                    }
                    System.out.println("Executed after delay!");

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        // Reveal the imposters to one another
                        StringBuilder imposterReveal = new StringBuilder("§cThe imposters are: ");
                        int imposterCount = 0;
                        for (Player imposter : imposterList) {
                            imposterCount++;
                            if (imposterCount == imposterList.size()) {
                                imposterReveal.append(imposter.getName());
                            } else {
                                imposterReveal.append(imposter.getName()).append(", ");
                            }
                        }

                        // Then send the reveal message to imposters
                        for (Player player : imposterList) {
                            player.sendMessage(imposterReveal.toString());
                        }
                        System.out.println("Executed after 4 second delay!");

                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            for (Player player : players) {
                                player.sendTitle(gameplayReminder, subtitle, 5, 60, 5);
                                player.playSound(player.getLocation(),
                                        Sound.BLOCK_AMETHYST_CLUSTER_BREAK,
                                        10000f, 1f);
                                player.playSound(player.getLocation(),
                                        Sound.ENTITY_CREEPER_PRIMED,
                                        10000f, 1f);
                            }
                        }, 60L); // 3+4+3 second delay
                    }, 80L); // 3+4 second delay
                }, 20L); // 1+1+1 second
            }, 20L); // 1+1 second delay
        }, 20L); // 1 second delay
    }

    private List<Player> toPlayerList(List<UUID> playerList) {
        return playerList.stream()
                .map(uuid -> Bukkit.getPlayer(uuid))
                .collect(Collectors.toList());
    }

    // Send role information to players
    private void printRoleTitle(String title, List<Player> playerList) {
        String subtitle = "";
        for (Player player : playerList) {
            player.sendTitle(title, subtitle, 1, 6, 2);
            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_CLUSTER_BREAK,
                    10000f, 1f);
            player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED,
                    10000f, 1f);
        }
    }
}
