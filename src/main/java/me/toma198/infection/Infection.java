package me.toma198.infection;

import me.toma198.Listeners.ConnectionListener;
import me.toma198.Listeners.DeathListener;
import me.toma198.Listeners.RespawnListener;
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

        DeathListener deathListener = new DeathListener(gameState, this);

        // Infected death should run before innocent death as conversion could switch innocent
        // to infected and then death will run twice, defeating the purpose of decoupling
        InfectedDeath infectedDeath = new InfectedDeath();
        deathListener.addDeathObserver(infectedDeath);

        InnocentDeath innocentDeath = new InnocentDeath();
        deathListener.addDeathObserver(innocentDeath);

        RespawnListener respawnListener = new RespawnListener(gameState, this);

        Conversion conversion = new Conversion();
        respawnListener.addRespawnObserver(conversion);

        // Register listeners
        getServer().getPluginManager().registerEvents(new ConnectionListener(gameState), this);
        getServer().getPluginManager().registerEvents(deathListener, this);
        getServer().getPluginManager().registerEvents(respawnListener, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Byeeee!!!");
    }
}
