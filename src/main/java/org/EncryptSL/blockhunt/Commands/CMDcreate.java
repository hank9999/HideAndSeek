package org.EncryptSL.blockhunt.Commands;

import org.EncryptSL.blockhunt.*;
import org.EncryptSL.blockhunt.Arena.ArenaState;
import org.EncryptSL.blockhunt.Managers.MessageM;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;

public class CMDcreate extends DefaultCMD {
	
	@Override
	public boolean exectue(Player player, Command cmd, String label, String[] args) {
		if (player != null) {
			if (args.length <= 1) {
				MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + BlockHunt.CMDcreate.usage);
			} else {
				if (((W.pos1.get(player) != null) && (W.pos2.get(player) != null))
						|| !W.config.getFile().getBoolean("wandEnabled")) {
					Arena arena;

					ScoreboardManager sm = Bukkit.getScoreboardManager();
					if (sm != null) {
						if (W.config.getFile().getBoolean("wandEnabled")) {
							arena = new Arena(args[1], W.pos1.get(player), W.pos2.get(player), 12, 3, 1, 50, 20, 300, 30,
									new ArrayList<>(), null, null, null, null, new ArrayList<>(),
									new ArrayList<>(), new ArrayList<>(), 10, 50, 8, new ArrayList<>(),
									ArenaState.WAITING, 0, new ArrayList<>(),
									sm.getNewScoreboard());
						} else {
							arena = new Arena(args[1], null, null, 12, 3, 1, 50, 20, 300, 30, new ArrayList<>(),
									null, null, null, null, new ArrayList<>(), new ArrayList<>(),
									new ArrayList<>(), 10, 50, 8, new ArrayList<>(), ArenaState.WAITING, 0,
									new ArrayList<>(), sm.getNewScoreboard());
						}
						W.arenas.getFile().set(args[1], arena);
						W.arenas.save();
						W.signs.load();

						W.arenaList.add(arena);
						ScoreboardHandler.createScoreboard(arena);

						MessageM.sendFMessage(player, ConfigC.normal_createCreatedArena, "name-" + args[1]);
					}
				} else {
					MessageM.sendFMessage(player, ConfigC.error_createSelectionFirst);
				}
			}
		} else {
			MessageM.sendFMessage(null, ConfigC.error_onlyIngame);
		}
		return true;
	}
}
