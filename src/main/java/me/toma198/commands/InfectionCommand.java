package me.toma198.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.toma198.infection.Game;
import me.toma198.infection.NameTag;
import me.toma198.infection.RoleReveal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public class InfectionCommand implements BasicCommand {
    private final Plugin plugin;
    private final Game gameState;

    public InfectionCommand(Plugin plugin, Game gameState) {
        this.plugin = plugin;
        this.gameState = gameState;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        if (!(source.getExecutor() instanceof Player)) {
            source.getSender().sendMessage(Component.text("Only players can send commands"));
            return;
        }

        CommandSender sender = source.getSender();

        // Inform of command usage if they typed no arguments
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /infection <imposterAmount>")
                    .color(NamedTextColor.RED));
            return;
        }

        int infectedCount = Integer.parseInt(args[0]);
        int playerCount = getServer().getOnlinePlayers().size();

        // Check if the argument is numeric
        for (Character c : args[0].toCharArray()) {
            if (!Character.isDigit(c)) {
                sender.sendMessage(Component.text("Imposter amount must be a positive integer")
                                .color(NamedTextColor.RED));
                sender.sendMessage(Component.text("Usage: /infection <imposterAmount>")
                                .color(NamedTextColor.RED));
                return;
            }
        }

        // Checking to see the argument is valid
        if (infectedCount <= 0 || infectedCount > playerCount) {
            sender.sendMessage(Component.text("Invalid amount of imposters, must be between 1" +
                            "and the playerCount").color(NamedTextColor.RED));
            return;
        }

        gameState.resetGame();

        gameState.assignInfected(infectedCount);

        for (Player player : gameState.toPlayerObjectList(gameState.getPlayers())) {
            NameTag.removeNameTags(player);
        }

        RoleReveal roles = new RoleReveal(plugin);
        roles.revealRole(gameState);
    }
}
