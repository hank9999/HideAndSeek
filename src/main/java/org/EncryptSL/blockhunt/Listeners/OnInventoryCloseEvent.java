package org.EncryptSL.blockhunt.Listeners;

import java.util.ArrayList;

import org.EncryptSL.blockhunt.Arena;
import org.EncryptSL.blockhunt.ArenaHandler;
import org.EncryptSL.blockhunt.W;
import org.EncryptSL.blockhunt.Managers.MessageM;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OnInventoryCloseEvent implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();
		if (inv.getType().equals(InventoryType.CHEST)) {
			if (event.getView().getTitle().contains("DisguiseBlocks")) {

				ItemStack i = inv.getItem(0);

				if (i != null) {
					ItemMeta im = i.getItemMeta();
					if (im != null) {
						String arenaname = im.getDisplayName().replaceAll(MessageM.replaceAll("%NDisguiseBlocks of arena: %A"), "");
						Arena arena = null;
						for (Arena arena2 : W.arenaList) {
							if (arena2.arenaName.equalsIgnoreCase(arenaname)) {
								arena = arena2;
							}
							ArrayList<ItemStack> blocks = new ArrayList<>();
							for (ItemStack item : inv.getContents()) {
								if (item != null) {
									if (!item.getType().equals(Material.PAPER)) {
										if (item.getType().equals(Material.FLOWER_POT)) {
											blocks.add(new ItemStack(Material.FLOWER_POT));
										} else {
											blocks.add(item);
										}
									}
								}
							}

							if (arena != null) {
								arena.disguiseBlocks = blocks;
							}
							save(arena);
						}
					}
				}

			}
		}
	}

	public void save(Arena arena) {
		W.arenas.getFile().set(arena.arenaName, arena);
		W.arenas.save();
		ArenaHandler.loadArenas();
	}
}
