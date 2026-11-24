package me.toma198.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.toma198.infection.Game;
import me.toma198.infection.RoleReveal;
import net.kyori.adventure.text.Component;
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
    public void execute(CommandSourceStack source, String[] args)
            throws CommandSyntaxException {
        if (!(source.getExecutor() instanceof Player)) {
            source.getSender().sendMessage(
                    Component.text("Only players can send commands"));
            return;
        }

        CommandSender sender = source.getSender();

        // Inform of command usage if they typed no arguments
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /infection <imposterAmount>");
            return;
        }

        int infectedCount = Integer.parseInt(args[0]);
        int playerCount = getServer().getOnlinePlayers().size();

        // Check if the argument is numeric
        for (Character c : args[0].toCharArray()) {
            if (!Character.isDigit(c)) {
                sender.sendMessage("§cImposter amount must be a positive integer");
                sender.sendMessage("§cUsage: /infection <imposterAmount>");
                return;
            }
        }

        // Checking to see the argument is valid
        if (infectedCount <= 0 || infectedCount > playerCount) {
            sender.sendMessage("§cInvalid amount of imposters, must be between 1 and the " +
                    "playerCount");
            return;
        }

        gameState.resetGame();

        gameState.assignInfected(infectedCount);

        RoleReveal roles = new RoleReveal(plugin);
        roles.revealRole(gameState);
    }
}
