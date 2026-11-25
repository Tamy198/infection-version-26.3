package me.toma198.infection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class RoleReveal {
    private final Plugin plugin;

    public RoleReveal(Plugin plugin) {
        this.plugin = plugin;
    }

    public void revealRole(Game gameState) {
        List<Player> players = gameState.toPlayerObjectList(gameState.getPlayers());
        List<Player> imposterList = gameState.toPlayerObjectList(gameState.getInfected());

        List<Player> innocentList;
        if (players.size() > 1) {
            innocentList = gameState.toPlayerObjectList(gameState.getInnocents());
        } else {
            innocentList = new ArrayList<>();
        }

        TextComponent threeSecondCountDown = Component.text(3).color(NamedTextColor.GOLD);
        TextComponent twoSecondCountDown = Component.text(2).color(NamedTextColor.GOLD);
        TextComponent oneSecondCountDown = Component.text(1).color(NamedTextColor.GOLD);

        TextComponent gameplayReminder = Component.text("Rmb to turn off chat/tab")
                .color(NamedTextColor.GOLD);

        // Countdown and imposter reveal
        printRoleTitle(threeSecondCountDown, players);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            printRoleTitle(twoSecondCountDown, players);
            System.out.println("Executed after delay!");

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                printRoleTitle(oneSecondCountDown, players);

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    // Inform imposters of their dubiousness
                    printRoleTitle(Component.text("You are the imposter")
                            .color(NamedTextColor.RED), imposterList);
                    for (Player player : imposterList) {
                        player.sendMessage(Component.text("You are the imposter, " +
                                "infect the innocents:)").color(NamedTextColor.RED));
                        player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 1000f, 1f);
                    }

                    // Inform innocents of their innocence
                    printRoleTitle(Component.text("You are NOT the imposter:)")
                            .color(NamedTextColor.GREEN), innocentList);
                    for (Player player : innocentList) {
                        player.sendMessage(Component.text("You are innocent:)")
                                .color(NamedTextColor.GREEN));
                        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0,
                                1000f, 1f);
                    }
                    System.out.println("Executed after delay!");

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        // Reveal the imposters to one another
                        StringBuilder imposterReveal = new StringBuilder("The imposters are: ");
                        for (Player imposter : imposterList) {
                            imposterReveal.append(imposter.getName());
                        }

                        // TODO: comma in name
                        if (imposterReveal.lastIndexOf(",") != -1) {
                            imposterReveal.deleteCharAt(imposterReveal.lastIndexOf(","));
                        }

                        // Then send the reveal message to imposters
                        for (Player player : imposterList) {
                            player.sendMessage(Component.text(imposterReveal.toString())
                                    .color(NamedTextColor.RED));
                        }
                        System.out.println("Executed after 4 second delay!");
                        System.out.println("Player list: " + players);
                        System.out.println("Infected list: " + imposterList);
                        System.out.println("Innocent list: " + innocentList);

                        for (Player player : innocentList) {
                            player.sendMessage(gameplayReminder);
                        }
                    }, 80L); // 3+4 second delay
                }, 20L); // 1+1+1 second
            }, 20L); // 1+1 second delay
        }, 20L); // 1 second delay
    }

    // Send role information to players
    private void printRoleTitle(TextComponent message, List<Player> playerList) {
        for (Player player : playerList) {
            final Title.Times times = Title.Times.times(Duration.ofMillis(100), Duration.ofMillis(600),
                    Duration.ofMillis(200));
            final Title title = Title.title(message, Component.empty(), times);

            player.showTitle(title);

            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 10000f, 1f);
            player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 10000f, 1f);
        }
    }
}
