package org.EncryptSL.blockhunt.Commands;

import org.EncryptSL.blockhunt.ConfigC;
import org.EncryptSL.blockhunt.BlockHunt;
import org.EncryptSL.blockhunt.W;
import org.EncryptSL.blockhunt.Managers.CommandM;
import org.EncryptSL.blockhunt.Managers.MessageM;
import org.EncryptSL.blockhunt.Managers.PermissionsM;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDhelp extends DefaultCMD {
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
		int amountCommands = 0;
		for (CommandM command : W.commands) {
			if (command.usage != null) {
				amountCommands = amountCommands + 1;
			}
		}

		int maxPages = Math.round(amountCommands / 3);
		if (maxPages <= 0) {
			maxPages = 1;
		}

		int page = 1;
		if (args.length == 1) {
			MessageM.sendFMessage(
					player,
					ConfigC.chat_headerhigh,
					"header-" + BlockHunt.pdfFile.getName() + " %Nhelp page %A" + page + "%N/%A" + maxPages
			);
			int i = 1;
			for (CommandM command : W.commands) {
				if (i <= 4) {
					if (command.usage != null) {
						if (PermissionsM.hasPerm(player, command.permission, false)) {
							MessageM.sendMessage(
									player,
									"%A" + command.usage + "%N - " + W.messages.getFile().get(command.help.location)
							);
						} else {
							MessageM.sendMessage(
									player,
									"%W" + command.usage + "%N - " + W.messages.getFile().get(command.help.location)
							);
						}
						i = i + 1;
					}
				}
			}

		} else {
			try {
				page = Integer.parseInt(args[1]);
			} catch (NumberFormatException ignored) {}

			if (maxPages < page) {
				maxPages = page;
			}

			MessageM.sendFMessage(player, ConfigC.chat_headerhigh, "header-"
					+ BlockHunt.pdfFile.getName() + " %Nhelp page %A"
					+ page + "%N/%A" + maxPages);

			int i = 1;
			for (CommandM command : W.commands) {
				if (i <= (page * 4) + 4) {
					if (command.usage != null) {
						if (i >= ((page - 1) * 4) + 1
								&& i <= ((page - 1) * 4) + 4) {
							if (PermissionsM.hasPerm(player,
									command.permission, false)) {
								MessageM.sendMessage(
										player,
										"%A" + command.usage + "%N - " + W.messages.getFile().get(command.help.location)
								);
							} else {
								MessageM.sendMessage(
										player,
										"%W" + command.usage + "%N - " + W.messages.getFile().get(command.help.location)
								);
							}
						}
						i = i + 1;
					}
				}
			}
		}
		MessageM.sendFMessage(player, ConfigC.chat_headerhigh, "header-&oHelp Page");
		return true;
	}
}
