package org.EncryptSL.blockhunt.Listeners;

import org.EncryptSL.blockhunt.Arena;
import org.EncryptSL.blockhunt.PermissionsC.Permissions;
import org.EncryptSL.blockhunt.W;
import org.EncryptSL.blockhunt.Managers.PermissionsM;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OnBlockBreakEvent implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreakEvent(BlockBreakEvent event) {
		Player player = event.getPlayer();

		for (Arena arena : W.arenaList) {
			if (arena.playersInArena.contains(player)) {
				event.setCancelled(true);
			}
		}

		if (event.getBlock().equals(Material.LEGACY_SIGN_POST)
				|| event.getBlock().equals(Material.WALL_SIGN)) {
			if (!PermissionsM.hasPerm(player, Permissions.signcreate, true)) {
				event.setCancelled(true);
			}
		}
	}
}
