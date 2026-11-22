package me.toma198.infection;

import me.toma198.Listeners.ConnectionListener;
import me.toma198.commands.InfectionCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Infection extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Hiiii!!!");

        Game gameState = new Game();

        registerCommand("infection", List.of("i"), new InfectionCommand(this, gameState));

        getServer().getPluginManager().registerEvents(new ConnectionListener(gameState), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Byeeee!!!");
    }
}
