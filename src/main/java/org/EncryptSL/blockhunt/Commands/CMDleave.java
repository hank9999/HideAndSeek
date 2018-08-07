package org.EncryptSL.blockhunt.Commands;

import org.EncryptSL.blockhunt.ArenaHandler;
import org.EncryptSL.blockhunt.ConfigC;
import org.EncryptSL.blockhunt.Managers.MessageM;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDleave extends DefaultCMD {

	@Override
	public boolean exectue(Player player, Command cmd, String label,
			String[] args) {
		if (player != null) {
			ArenaHandler.playerLeaveArena(player, true, true);
		} else {
			MessageM.sendFMessage(player, ConfigC.error_onlyIngame);
		}
		return true;
	}
}
