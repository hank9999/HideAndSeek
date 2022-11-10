package org.EncryptSL.blockhunt;

import org.EncryptSL.blockhunt.Arena.ArenaState;
import org.EncryptSL.blockhunt.Managers.MessageM;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardHandler {
	public static void createScoreboard(Arena arena) {
		if ((Boolean) W.config.get(ConfigC.scoreboard_enabled)) {
			Scoreboard board = arena.scoreboard;
			if (board.getObjective(arena.arenaName) != null) {
				updateScoreboard(arena);
				return;
			}

			Objective object = board.registerNewObjective(BlockHunt.cutString(arena.arenaName, 32), "dummy", "dummy1");
			object.setDisplaySlot(DisplaySlot.SIDEBAR);
			object.setDisplayName(BlockHunt.cutString(MessageM.replaceAll((String) W.config.get(ConfigC.scoreboard_title)), 32));
			Score timeleft = object.getScore(BlockHunt.cutString(MessageM.replaceAll((String) W.config.get(ConfigC.scoreboard_timeleft)), 32));
			timeleft.setScore(arena.timer);
			Score seekers = object.getScore(BlockHunt.cutString(MessageM.replaceAll((String) W.config.get(ConfigC.scoreboard_seekers)), 32));
			seekers.setScore(arena.seekers.size());
			Score hiders = object.getScore(BlockHunt.cutString(MessageM.replaceAll((String) W.config.get(ConfigC.scoreboard_hiders)), 32));
			hiders.setScore(arena.playersInArena.size() - arena.seekers.size());
			if (arena.gameState == ArenaState.INGAME) {
				for (Player pl : arena.playersInArena) {
					pl.setScoreboard(board);
				}
			} else {
				for (Player pl : arena.playersInArena) {
					ScoreboardManager sm = Bukkit.getScoreboardManager();
					if (sm != null) {
						pl.setScoreboard(sm.getNewScoreboard());
					}
				}
			}
		}
	}

	public static void updateScoreboard(Arena arena) {
		if ((Boolean) W.config.get(ConfigC.scoreboard_enabled)) {
			Scoreboard board = arena.scoreboard;
			Objective object = board.getObjective(DisplaySlot.SIDEBAR);
			if (object != null) {
				object.setDisplayName(BlockHunt.cutString(MessageM.replaceAll((String) W.config.get(ConfigC.scoreboard_title)), 32));
				Score timeleft = object.getScore(BlockHunt.cutString(MessageM.replaceAll((String) W.config.get(ConfigC.scoreboard_timeleft)), 32));
				timeleft.setScore(arena.timer);
				Score seekers = object.getScore(BlockHunt.cutString(MessageM.replaceAll((String) W.config.get(ConfigC.scoreboard_seekers)), 32));
				seekers.setScore(arena.seekers.size());
				Score hiders = object.getScore(BlockHunt.cutString(MessageM.replaceAll((String) W.config.get(ConfigC.scoreboard_hiders)), 32));
				hiders.setScore(arena.playersInArena.size() - arena.seekers.size());
				if (arena.gameState == ArenaState.INGAME) {
					for (Player pl : arena.playersInArena) {
						pl.setScoreboard(board);
					}
				} else {
					for (Player pl : arena.playersInArena) {
						ScoreboardManager sm = Bukkit.getScoreboardManager();
						if (sm != null) {
							pl.setScoreboard(sm.getNewScoreboard());
						}
					}
				}
			}
		}
	}

	public static void removeScoreboard(Player player) {
		ScoreboardManager sm = Bukkit.getScoreboardManager();
		if (sm != null) {
			player.setScoreboard(sm.getNewScoreboard());
		}
	}
}
