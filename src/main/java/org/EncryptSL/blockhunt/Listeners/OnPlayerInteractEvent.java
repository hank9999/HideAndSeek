package org.EncryptSL.blockhunt.Listeners;

import org.EncryptSL.blockhunt.Arena;
import org.EncryptSL.blockhunt.Arena.ArenaState;
import org.EncryptSL.blockhunt.ArenaHandler;
import org.EncryptSL.blockhunt.ConfigC;
import org.EncryptSL.blockhunt.InventoryHandler;
import org.EncryptSL.blockhunt.PermissionsC.Permissions;
import org.EncryptSL.blockhunt.SignsHandler;
import org.EncryptSL.blockhunt.SolidBlockHandler;
import org.EncryptSL.blockhunt.W;
import org.EncryptSL.blockhunt.Managers.MessageM;
import org.EncryptSL.blockhunt.Managers.PermissionsM;
import org.EncryptSL.blockhunt.Serializables.LocationSerializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class OnPlayerInteractEvent implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (PermissionsM.hasPerm(player, Permissions.create, false)) {
			ItemStack item = player.getInventory().getItemInMainHand();
			if (item.getType() != Material.AIR) {
				ItemMeta im = item.getItemMeta();
				if (im != null && im.hasDisplayName()) {
					if (im.getDisplayName().equals(MessageM.replaceAll((String) W.config.get(ConfigC.wandName)))) {
						Action action = event.getAction();
						if (event.hasBlock()) {
							if (block != null) {
								LocationSerializable location = new LocationSerializable(block.getLocation());
								if (action.equals(Action.LEFT_CLICK_BLOCK)) {
									event.setCancelled(true);
									if (W.pos1.get(player) == null || !W.pos1.get(player).equals(location)) {
										MessageM.sendFMessage(
												player,
												ConfigC.normal_wandSetPosition,
												"number-1",
												"pos-%N(%A" + location.getBlockX() + "%N, %A" + location.getBlockY() + "%N, %A" + location.getBlockZ() + "%N)",
												"x-" + location.getBlockX(), "y-" + location.getBlockY(),
												"z-" + location.getBlockZ());
										W.pos1.put(player, location);
									}
								} else if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
									event.setCancelled(true);
									if (W.pos2.get(player) == null || !W.pos2.get(player).equals(location)) {
										MessageM.sendFMessage(
												player,
												ConfigC.normal_wandSetPosition,
												"number-2",
												"pos-%N(%A" + location.getBlockX() + "%N, %A" + location.getBlockY() + "%N, %A" + location.getBlockZ() + "%N)",
												"x-" + location.getBlockX(), "y-" + location.getBlockY(),
												"z-" + location.getBlockZ());
										W.pos2.put(player, location);
									}
								}
							}
						}
					}
				}
			}
		}

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock() != null) {
				if (event.getClickedBlock().getType().equals(Material.OAK_SIGN)
						|| event.getClickedBlock().getType().equals(Material.OAK_WALL_SIGN)) {
					if (SignsHandler.isSign(new LocationSerializable(event.getClickedBlock().getLocation()))) {
						Sign sign = (Sign) event.getClickedBlock().getState();
						sign.getLine(1);

						if (sign.getLine(1).equals(MessageM.replaceAll(W.config.getFile().getStringList(ConfigC.sign_LEAVE.location).get(1)))) {
							if (PermissionsM.hasPerm(player, Permissions.joinsign, true)) {
								ArenaHandler.playerLeaveArena(player, true, true);
							}
						} else if (sign.getLine(1).equals(MessageM.replaceAll(W.config.getFile().getStringList(ConfigC.sign_SHOP.location).get(1)))) {
							if (PermissionsM.hasPerm(player, Permissions.shop, true)) {
								InventoryHandler.openShop(player);
							}
						} else {
							for (Arena arena : W.arenaList) {
								if (sign.getLines()[1].contains(arena.arenaName)) {
									if (PermissionsM.hasPerm(player, Permissions.joinsign, true)) {
										ArenaHandler.playerJoinArena(player, arena.arenaName);
									}
								}
							}
						}
					}
				}
			}
		}

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK
				|| event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (block != null && block.getType() != Material.AIR) {
				if (block.getType().equals(Material.ENCHANTING_TABLE)
						|| block.getType().equals(Material.CRAFTING_TABLE)
						|| block.getType().equals(Material.FURNACE)
						|| block.getType().equals(Material.CHEST)
						|| block.getType().equals(Material.ANVIL)
						|| block.getType().equals(Material.ENDER_CHEST)
						|| block.getType().equals(Material.JUKEBOX)) {
					for (Arena arena : W.arenaList) {
						if (arena.playersInArena.contains(player)) {
							event.setCancelled(true);
						}
					}
				}
			}
		}

		if (event.getAction() == Action.LEFT_CLICK_BLOCK
				|| event.getAction() == Action.LEFT_CLICK_BLOCK) {
			for (Arena arena : W.arenaList) {
				if (arena.seekers.contains(player)) {
					for (Player pl : arena.playersInArena) {
						if (W.hiddenLoc.get(pl) != null) {
							Block pLoc = event.getClickedBlock();
							if (pLoc != null) {
								Block moveLocBlock = W.hiddenLoc.get(pl).getBlock();
								if (moveLocBlock.getX() == pLoc.getX()
										&& moveLocBlock.getY() == pLoc.getY()
										&& moveLocBlock.getZ() == pLoc.getZ()) {
									W.moveLoc.put(pl, new Location(pl.getWorld(), 0, 0, 0));
									pl.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
									SolidBlockHandler.makePlayerUnsolid(pl);
								}
							}
						}
					}
				}
			}
		}

		for (Arena arena : W.arenaList) {
			if (arena.playersInArena.contains(player) && (arena.gameState.equals(ArenaState.WAITING)
					|| arena.gameState.equals(ArenaState.STARTING))) {
				event.setCancelled(true);
				ItemStack item = player.getInventory().getItemInMainHand();
				if (item.getType() != Material.AIR) {
					if (item.getItemMeta() != null) {
						if (item.getItemMeta().getDisplayName().equals(
								MessageM.replaceAll((String) W.config.get(ConfigC.shop_blockChooserv1Name))
						)) {
							Inventory blockChooser = Bukkit.createInventory(
									null,
									36,
									MessageM.replaceAll("\u00A7r" + W.config.get(ConfigC.shop_blockChooserv1Name))
							);
							if (arena.disguiseBlocks != null) {
								for (int i = arena.disguiseBlocks.size(); i > 0; i = i - 1) {
									blockChooser.setItem(i - 1, arena.disguiseBlocks.get(i - 1));
								}
							}

							player.openInventory(blockChooser);
						}

						if (item.getItemMeta()
								.getDisplayName()
								.equals(MessageM.replaceAll((String) W.config
										.get(ConfigC.shop_BlockHuntPassv2Name)))) {
							Inventory BlockHuntPass = Bukkit.createInventory(
									null,
									9,
									MessageM.replaceAll("\u00A7r" + W.config.get(ConfigC.shop_BlockHuntPassv2Name))
							);
							ItemStack BlockHuntPassSEEKER = new ItemStack(Material.WHITE_WOOL, 1);
							ItemMeta BlockHuntPassIM = BlockHuntPassSEEKER.getItemMeta();
							if (BlockHuntPassIM != null) {
								BlockHuntPassIM.setDisplayName(MessageM.replaceAll("&eSEEKER"));
								BlockHuntPassSEEKER.setItemMeta(BlockHuntPassIM);
							}
							Damageable damageable = (Damageable) BlockHuntPassSEEKER.getItemMeta();
							damageable.setDamage(11);
							BlockHuntPassSEEKER.setItemMeta((ItemMeta) damageable);

							BlockHuntPass.setItem(1, BlockHuntPassSEEKER);

							ItemStack BlockHuntPassHIDER = new ItemStack(Material.WHITE_WOOL, 1);
							ItemMeta BlockHuntPassHIDERIM = BlockHuntPassHIDER.getItemMeta();
							if (BlockHuntPassHIDERIM != null) {
								BlockHuntPassHIDERIM.setDisplayName(MessageM.replaceAll("&eHIDER"));
								BlockHuntPassHIDER.setItemMeta(BlockHuntPassHIDERIM);
							}

							Damageable damageableHIDER = (Damageable) BlockHuntPassHIDER.getItemMeta();
							damageable.setDamage(14);
							BlockHuntPassHIDER.setItemMeta((ItemMeta) damageableHIDER);

							BlockHuntPass.setItem(7, BlockHuntPassHIDER);

							player.openInventory(BlockHuntPass);
						}
					}
				}
			}
		}

	}
}
