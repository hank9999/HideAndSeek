package org.EncryptSL.blockhunt.Commands;

import org.EncryptSL.blockhunt.Arena;
import org.EncryptSL.blockhunt.ArenaHandler;
import org.EncryptSL.blockhunt.ConfigC;
import org.EncryptSL.blockhunt.W;
import org.EncryptSL.blockhunt.Managers.ConfigM;
import org.EncryptSL.blockhunt.Managers.MessageM;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDreload extends DefaultCMD {
	/**
	 * Steffion's Engine - Made by Steffion.
	 * 
	 * You're allowed to use this engine for own usage, you're not allowed to
	 * republish the engine. Using this for your own plugin is allowed when a
	 * credit is placed somewhere in the plugin.
	 * 
	 * Thanks for your cooperate!
	 * 
	 * @author Steffion
	 */

	@Override
	public boolean exectue(Player player, Command cmd, String label,
			String[] args) {
		ConfigM.newFiles();

		W.config.load();
		W.messages.load();
		W.arenas.load();
		W.signs.load();
		W.shop.load();
		for (Arena arena : W.arenaList) {
			ArenaHandler.stopArena(arena);
		}

		ArenaHandler.loadArenas();
		MessageM.sendFMessage(player, ConfigC.normal_reloadedConfigs);
		return true;
	}
}
