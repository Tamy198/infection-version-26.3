package me.toma198.infection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class NameTag {
    public static void removeNameTags(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        Scoreboard scoreboard = manager.getMainScoreboard();

        // Find or create a team for hidden nametags
        Team team = scoreboard.getTeam("hide_nametags");
        if (team == null) {
            team = scoreboard.registerNewTeam("hide_nametags");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }

        // Add the player to the team
        team.addEntry(player.getName());

        // Make sure the player sees the updated scoreboard
        player.setScoreboard(scoreboard);
    }

    public static void reviveNameTags(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        Scoreboard scoreboard = manager.getMainScoreboard();

        // Find or create a team for hidden nametags
        Team team = scoreboard.getTeam("show_nametags");
        if (team == null) {
            team = scoreboard.registerNewTeam("show_nametags");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        }

        // Add the player to the team, using Team classes
        team.addEntry(player.getName());

        // Make sure the player sees the updated scoreboard
        player.setScoreboard(scoreboard);
    }
}
