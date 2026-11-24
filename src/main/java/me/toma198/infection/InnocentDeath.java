package me.toma198.infection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class InnocentDeath implements DeathObserver {
    @Override
    public void onDeathEvent(Player player, Game gameState, Plugin plugin) {
        System.out.println("2. Infected death");

        List<Player> infected = gameState.toPlayerObjectList(gameState.getInfected());

        // Even if infected is empty, the dead player (imposter if infected is empty)
        // shouldn't be innocent/in the innocent list
        if (!gameState.getInnocents().contains(player.getUniqueId())) {
            return;
        }

        // Imposter coordinates
        double impX;
        double impY;
        double impZ;

        // Innocent coordinates
        double X = player.getLocation().getX();
        double Y = player.getLocation().getY();
        double Z = player.getLocation().getZ();

        // Absolute distance squared of imposter to dead player in each dimension
        double absX;
        double absY;
        double absZ;
        double distFromImp;

        // Calculate distance of the player who died to the imposter
        boolean activateConversion = false;
        for (Player p : infected) {
            // Ensure an infected player doesn't infect themselves
            if (p.equals(player)) {
                System.out.println("Bug with innocents list");
                return;
            }

            impX = p.getLocation().getX();
            impY = p.getLocation().getY();
            impZ = p.getLocation().getZ();

            absX = Math.pow((X - impX), 2);
            absY = Math.pow((Y - impY), 2);
            absZ = Math.pow((Z - impZ), 2);

            distFromImp = Math.sqrt(absX + absY + absZ);

            // if within 30 blocks, begin conversion to imposter
            if (distFromImp <= 30) {
                activateConversion = true;
                gameState.addToConversionList(player.getUniqueId());

                System.out.println("Conversion should run");
                System.out.println("Conversion list: " + gameState.getConversionList());
            }
        }

        // else set innocent to spectator
        if (!activateConversion) {
            player.setGameMode(GameMode.SPECTATOR);

            // TODO: work out how to remove players from innocents team
            //gameState.removeFromInnocents(player.getUniqueId());

            // Inform if imposters have won
            if (gameState.getInfected().size() == gameState.getPlayers().size()) {
                gameState.infectedWin();
            }
        }

        // Activate a respawn event a tick later
        player.setRespawnLocation(player.getLocation());
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.spigot().respawn(), 1L);
    }
}
