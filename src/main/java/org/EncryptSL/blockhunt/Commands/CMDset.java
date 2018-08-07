package org.EncryptSL.blockhunt.Commands;

import org.EncryptSL.blockhunt.BlockHunt;
import org.EncryptSL.blockhunt.ConfigC;
import org.EncryptSL.blockhunt.InventoryHandler;
import org.EncryptSL.blockhunt.Managers.MessageM;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDset extends DefaultCMD {

	@Override
	public boolean exectue(Player player, Command cmd, String label,
			String[] args) {
		if (player != null) {
			if (args.length <= 1) {
				MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments,
						"syntax-" + BlockHunt.CMDset.usage);
			} else {
				String arenaname = args[1];
				InventoryHandler.openPanel(player, arenaname);
			}
		} else {
			MessageM.sendFMessage(player, ConfigC.error_onlyIngame);
		}
		return true;
	}
}
