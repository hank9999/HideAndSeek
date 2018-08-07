package org.EncryptSL.blockhunt.Commands;

import org.EncryptSL.blockhunt.ArenaHandler;
import org.EncryptSL.blockhunt.BlockHunt;
import org.EncryptSL.blockhunt.ConfigC;
import org.EncryptSL.blockhunt.Managers.MessageM;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDjoin extends DefaultCMD {

	@Override
	public boolean exectue(Player player, Command cmd, String label,
			String[] args) {
		if (player != null) {
			if (args.length <= 1) {
				MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments,
						"syntax-" + BlockHunt.CMDjoin.usage);
			} else {
				ArenaHandler.playerJoinArena(player, args[1]);
			}
		} else {
			MessageM.sendFMessage(player, ConfigC.error_onlyIngame);
		}
		return true;
	}
}
