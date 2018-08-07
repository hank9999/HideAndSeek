package org.EncryptSL.blockhunt.Listeners;

import org.EncryptSL.blockhunt.BlockHunt;
import org.EncryptSL.blockhunt.PermissionsC.Permissions;
import org.EncryptSL.blockhunt.SignsHandler;
import org.EncryptSL.blockhunt.Managers.PermissionsM;
import org.EncryptSL.blockhunt.Serializables.LocationSerializable;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OnSignChangeEvent implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignChangeEvent(SignChangeEvent event) {
		Player player = event.getPlayer();
		String[] lines = event.getLines();
		if (lines[0] != null) {
			if (lines[0].equalsIgnoreCase("[" + BlockHunt.pdfFile.getName()
					+ "]")) {
				if (PermissionsM.hasPerm(player, Permissions.signcreate, true)) {
					SignsHandler.createSign(event, lines,
							new LocationSerializable(event.getBlock()
									.getLocation()));
				}
			}
		}
	}
}
