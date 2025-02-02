package org.EncryptSL.blockhunt.Listeners;

import org.EncryptSL.blockhunt.Arena;
import org.EncryptSL.blockhunt.ConfigC;
import org.EncryptSL.blockhunt.PermissionsC.Permissions;
import org.EncryptSL.blockhunt.W;
import org.EncryptSL.blockhunt.Managers.MessageM;
import org.EncryptSL.blockhunt.Managers.PermissionsM;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnPlayerCommandPreprocessEvent implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerCommandPreprocessEvent(
			PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();

		for (Arena arena : W.arenaList) {
			if (arena.playersInArena.contains(player)) {
				String m = event.getMessage();
				if (m.startsWith("/blockhunt")
						|| m.startsWith("/bh")
						|| m.startsWith("/seekandfind")
						|| m.startsWith("/saf")
						|| m.startsWith("/sf")
						|| m.startsWith("/hideandseek")
						|| m.startsWith("/has")
						|| m.startsWith("/hs")
						|| m.startsWith("/ban")
						|| m.startsWith("/kick")
						|| m.startsWith("/tempban")
						|| m.startsWith("/mute")
						|| m.startsWith("/reload")) {
					return;
				}

				for (String command : arena.allowedCommands) {
					if (m.startsWith("/" + command)) {
						return;
					}
				}

				if (PermissionsM
						.hasPerm(player, Permissions.allcommands, false)) {
					return;
				}

				MessageM.sendFMessage(player, ConfigC.warning_unableToCommand);
				event.setCancelled(true);
			}
		}
	}
}
