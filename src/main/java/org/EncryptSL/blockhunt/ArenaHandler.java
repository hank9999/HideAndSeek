package org.EncryptSL.blockhunt;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import me.libraryaddict.disguise.DisguiseAPI;
import org.EncryptSL.blockhunt.Arena.ArenaState;
import org.EncryptSL.blockhunt.PermissionsC.Permissions;
import org.EncryptSL.blockhunt.Managers.MessageM;
import org.EncryptSL.blockhunt.Managers.PermissionsM;
import org.EncryptSL.blockhunt.Serializables.LocationSerializable;

@SuppressWarnings("deprecation")
public class ArenaHandler {
	public static void hidersWin(Arena arena) {
		StringBuilder hidersLeft = new StringBuilder();
		
		for (Player player : arena.playersInArena) {
			if (!arena.seekers.contains(player)) {
				hidersLeft.append(player.getName()).append(", ");
			}
		}
		
		hidersLeft = new StringBuilder(hidersLeft.substring(0, hidersLeft.length() - 2));
		
		ArenaHandler.sendFMessage(arena, ConfigC.normal_winHiders, "names-" + hidersLeft);
		for (Player player : arena.playersInArena) {
			if (arena.seekers.contains(player)) {
				if (arena.hidersWinCommands != null) {
					for (String command : arena.hidersWinCommands) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								command.replaceAll("%player%", player.getName()));
					}
					if (W.config.getFile().getBoolean("vaultSupport")) {
						if (BlockHunt.econ != null) {
							if (arena.seekers.contains(player)) {
								BlockHunt.econ.depositPlayer(player.getName(), arena.hidersTokenWin);
								MessageM.sendFMessage(player, ConfigC.normal_addedVaultBalance,
										"amount-" + arena.hidersTokenWin);
							}
						}
					} else {
						if (W.shop.getFile().get(player.getName() + ".tokens") == null) {
							W.shop.getFile().set(player.getName() + ".tokens", 0);
							W.shop.save();
						}
						int playerTokens = W.shop.getFile().getInt(player.getName() + ".tokens");
						W.shop.getFile().set(player.getName() + ".tokens", playerTokens + arena.hidersTokenWin);
						W.shop.save();

						MessageM.sendFMessage(player, ConfigC.normal_addedToken, "amount-" + arena.hidersTokenWin);
					}
				}
			}
		}

		arena.seekers.clear();

		for (Player player : arena.playersInArena) {
			ArenaHandler.playerLeaveArena(player, false, false);
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		}

		arena.gameState = ArenaState.WAITING;
		arena.timer = 0;
		arena.playersInArena.clear();
	}

	public static void loadArenas() {
		W.arenaList.clear();
		for (String arenaName : W.arenas.getFile().getKeys(false)) {
			W.arenaList.add((Arena) W.arenas.getFile().get(arenaName));
		}

		for (Arena arena : W.arenaList) {
			ScoreboardHandler.createScoreboard(arena);
		}
	}

	public static void playerJoinArena(Player player, String arenaname) {
		boolean found = false;
		boolean alreadyJoined = false;
		for (Arena arena : W.arenaList) {
			if (arena.playersInArena != null) {
				if (arena.playersInArena.contains(player)) {
					alreadyJoined = true;
				}
			}
		}

		if (!alreadyJoined) {
			for (Arena arena : W.arenaList) {
				if (arena.arenaName.equalsIgnoreCase(arenaname)) {
					found = true;
					if (arena.disguiseBlocks.isEmpty()) {
						MessageM.sendFMessage(player, ConfigC.error_joinNoBlocksSet);
					} else {
						boolean inventoryempty = true;
						for (ItemStack invitem : player.getInventory()) {
							if (invitem != null) {
								if (invitem.getType() != Material.AIR) {
									inventoryempty = false;
								}
							}
						}

						for (ItemStack invitem : player.getInventory().getArmorContents()) {
							if ((invitem != null) && (invitem.getType() != Material.AIR)) {
								inventoryempty = false;
							}
						}

						if ((Boolean) W.config.get(ConfigC.requireInventoryClearOnJoin) && !inventoryempty) {
							MessageM.sendFMessage(player, ConfigC.error_joinInventoryNotEmpty);
							return;
						}

						LocationSerializable zero = new LocationSerializable(
								Bukkit.getWorld(player.getWorld().getName()), 0, 0, 0, 0, 0);
						if ((arena.lobbyWarp != null) && (arena.hidersWarp != null) && (arena.seekersWarp != null)
								&& (arena.spawnWarp != null)) {
							if (!arena.lobbyWarp.equals(zero) && !arena.hidersWarp.equals(zero)
									&& !arena.seekersWarp.equals(zero) && !arena.spawnWarp.equals(zero)) {
								if ((arena.gameState == ArenaState.WAITING)
										|| (arena.gameState == ArenaState.STARTING)) {
									if (arena.playersInArena.size() >= arena.maxPlayers) {
										if (!PermissionsM.hasPerm(player, Permissions.joinfull, false)) {
											MessageM.sendFMessage(player, ConfigC.error_joinFull);
											return;
										}
									}
									arena.playersInArena.add(player);

									PlayerArenaData pad = new PlayerArenaData(player.getLocation(),
											player.getGameMode(), player.getInventory().getContents(),
											player.getInventory().getArmorContents(), player.getExp(),
											player.getLevel(), player.getHealth(), player.getFoodLevel(),
											player.getActivePotionEffects(), player.getAllowFlight());

									W.pData.put(player, pad);

									player.teleport(arena.lobbyWarp);
									player.setGameMode(GameMode.SURVIVAL);
									for (PotionEffect pe : player.getActivePotionEffects()) {
										player.removePotionEffect(pe.getType());
									}
									player.setFoodLevel(20);
									player.setHealth(player.getMaxHealth());
									player.setLevel(arena.timer);
									player.setExp(0);
									player.getInventory().clear();
									player.getInventory().setHelmet(new ItemStack(Material.AIR));
									player.getInventory().setChestplate(new ItemStack(Material.AIR));
									player.getInventory().setLeggings(new ItemStack(Material.AIR));
									player.getInventory().setBoots(new ItemStack(Material.AIR));
									player.setFlying(false);
									player.setAllowFlight(false);
									player.setWalkSpeed(0.25F);
									
									// Fix for client not showing players after
									// they join
									for (Player otherplayer : arena.playersInArena) {
										if (otherplayer.canSee(player)) {
											otherplayer.showPlayer(player);
										}
										
										if (player.canSee(otherplayer)) {
											player.showPlayer(otherplayer);
										}
									}

									if ((Boolean) W.config.get(ConfigC.shop_blockChooserv1Enabled)) {
										if ((W.shop.getFile().get(player.getName() + ".blockchooser") != null)
												|| PermissionsM.hasPerm(player, Permissions.shopblockchooser, false)) {

											Material m = Material.getMaterial((String) W.config.get(ConfigC.shop_blockChooserv1IDname));

											if (m == null) {
												m = Material.STONE;
											}

											ItemStack shopBlockChooser = new ItemStack(m, 1);
											ItemMeta shopBlockChooser_IM = shopBlockChooser.getItemMeta();

											if (shopBlockChooser_IM != null) {
												shopBlockChooser_IM.setDisplayName(MessageM.replaceAll((String) W.config.get(ConfigC.shop_blockChooserv1Name)));
											}

											List<String> lores = W.config.getFile().getStringList(ConfigC.shop_blockChooserv1Description.location);
											List<String> lores2 = new ArrayList<>();
											for (String lore : lores) {
												lores2.add(MessageM.replaceAll(lore));
											}

											if (shopBlockChooser_IM != null) {
												shopBlockChooser_IM.setLore(lores2);
											}

											shopBlockChooser.setItemMeta(shopBlockChooser_IM);

											player.getInventory().addItem(shopBlockChooser);
										}
									}

									if ((Boolean) W.config.get(ConfigC.shop_BlockHuntPassv2Enabled)) {
										if (W.shop.getFile().getInt(player.getName() + ".blockhuntpass") != 0) {

											Material m = Material.getMaterial((String) W.config.get(ConfigC.shop_BlockHuntPassv2IDName));

											if (m == null) {
												m = Material.STONE;
											}

											ItemStack shopBlockHuntPass = new ItemStack(m, 1);
											ItemMeta shopBlockHuntPass_IM = shopBlockHuntPass.getItemMeta();

											if (shopBlockHuntPass_IM != null) {
												shopBlockHuntPass_IM.setDisplayName(MessageM.replaceAll((String) W.config.get(ConfigC.shop_BlockHuntPassv2Name)));
											}

											List<String> lores = W.config.getFile().getStringList(ConfigC.shop_BlockHuntPassv2Description.location);
											List<String> lores2 = new ArrayList<>();
											for (String lore : lores) {
												lores2.add(MessageM.replaceAll(lore));
											}

											if (shopBlockHuntPass_IM != null) {
												shopBlockHuntPass_IM.setLore(lores2);
											}

											shopBlockHuntPass.setItemMeta(shopBlockHuntPass_IM);
											shopBlockHuntPass.setAmount(
													W.shop.getFile().getInt(player.getName() + ".blockhuntpass"));

											player.getInventory().addItem(shopBlockHuntPass);
										}
									}
									player.updateInventory();

									DisguiseAPI.undisguiseToAll(player);

									ArenaHandler.sendFMessage(
											arena,
											ConfigC.normal_joinJoinedArena,
											"playername-" + player.getName(),
											"1-" + arena.playersInArena.size(),
											"2-" + arena.maxPlayers
									);
									if (arena.playersInArena.size() < arena.minPlayers) {
										ArenaHandler.sendFMessage(arena, ConfigC.warning_lobbyNeedAtleast, "1-" + arena.minPlayers);
									}
								} else {
									MessageM.sendFMessage(player, ConfigC.error_joinArenaIngame);
								}
							} else {
								MessageM.sendFMessage(player, ConfigC.error_joinWarpsNotSet);
							}
						} else {
							MessageM.sendFMessage(player, ConfigC.error_joinWarpsNotSet);
						}
					}
				}
			}
		} else {
			MessageM.sendFMessage(player, ConfigC.error_joinAlreadyJoined);
			return;
		}

		if (!found) {
			MessageM.sendFMessage(player, ConfigC.error_noArena, "name-" + arenaname);
		}

		SignsHandler.updateSigns();
	}

	public static void playerLeaveArena(Player player, boolean message, boolean cleanup) {
		Arena arena = null;
		for (Arena arena2 : W.arenaList) {
			if (arena2.playersInArena != null) {
				if (arena2.playersInArena.contains(player)) {
					arena = arena2;
				}
			}
		}

		if (arena != null) {
			if (cleanup) {
				arena.playersInArena.remove(player);
				arena.seekers.remove(player);

				if ((arena.playersInArena.size() < arena.minPlayers) && arena.gameState.equals(ArenaState.STARTING)) {
					arena.gameState = ArenaState.WAITING;
					arena.timer = 0;

					ArenaHandler.sendFMessage(arena, ConfigC.warning_lobbyNeedAtleast, "1-" + arena.minPlayers);
				}
				if ((arena.playersInArena.size() <= 2) && (arena.gameState == ArenaState.INGAME)) {
					if (arena.seekers.size() >= arena.playersInArena.size()) {
						ArenaHandler.seekersWin(arena);
					} else {
						ArenaHandler.hidersWin(arena);
					}
				}

				if (arena.seekers.size() >= arena.playersInArena.size()) {
					ArenaHandler.seekersWin(arena);
				}

				if ((arena.seekers.size() == 0) && (arena.gameState == ArenaState.INGAME)) {
					Player seeker = arena.playersInArena.get(W.random.nextInt(arena.playersInArena.size()));
					ArenaHandler.sendFMessage(arena, ConfigC.warning_ingameNEWSeekerChoosen,
							"seeker-" + seeker.getName());
					ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameSeekerChoosen, "seeker-" + seeker.getName());
					DisguiseAPI.undisguiseToAll(seeker);
					for (Player pl : Bukkit.getOnlinePlayers()) {
						pl.showPlayer(seeker);
					}
					seeker.getInventory().clear();
					arena.seekers.add(seeker);
					seeker.teleport(arena.seekersWarp);
					W.seekertime.put(seeker, arena.waitingTimeSeeker);
					seeker.setWalkSpeed(0.25F);
					
					// Fix for client not showing players after
					// they join
					for (Player otherplayer : arena.playersInArena) {
						if (otherplayer.canSee(player)) {
							otherplayer.showPlayer(player);
						}
						
						if (player.canSee(otherplayer)) {
							player.showPlayer(otherplayer);
						}
					}
				}
			}

			PlayerArenaData pad = new PlayerArenaData(null, null, null, null, null, null, null, null, null, false);

			if (W.pData.get(player) != null) {
				pad = W.pData.get(player);
			}

			player.getInventory().clear();
			player.getInventory().setContents(pad.pInventory);
			player.getInventory().setArmorContents(pad.pArmor);
			player.updateInventory();
			player.setExp(pad.pEXP);
			player.setLevel(pad.pEXPL);
			player.setHealth(pad.pHealth);
			player.setFoodLevel(pad.pFood);
			player.addPotionEffects(pad.pPotionEffects);
			player.teleport(arena.spawnWarp);
			player.setGameMode(pad.pGameMode);
			player.setAllowFlight(pad.pFlying);
			if (player.getAllowFlight()) {
				player.setFlying(true);
			}
			player.setWalkSpeed(0.2F);

			W.pData.remove(player);

			for (Player pl : Bukkit.getOnlinePlayers()) {
				pl.showPlayer(player);
				if (W.hiddenLoc.get(player) != null) {
					if (W.hiddenLocWater.get(player) != null) {
						Block pBlock = W.hiddenLoc.get(player).getBlock();
						if (W.hiddenLocWater.get(player)) {
							pl.sendBlockChange(pBlock.getLocation(), Material.WATER, (byte) 0);
						} else {
							pl.sendBlockChange(pBlock.getLocation(), Material.AIR, (byte) 0);
						}
					}
				}

				DisguiseAPI.undisguiseToAll(player);
			}

			ScoreboardHandler.removeScoreboard(player);

			MessageM.sendFMessage(player, ConfigC.normal_leaveYouLeft);
			if (message) {
				ArenaHandler.sendFMessage(arena, ConfigC.normal_leaveLeftArena, "playername-" + player.getName(),
						"1-" + arena.playersInArena.size(), "2-" + arena.maxPlayers);
			}
		} else {
			if (message) {
				MessageM.sendFMessage(player, ConfigC.error_leaveNotInArena);
			}
			return;
		}

		SignsHandler.updateSigns();
	}

	public static void seekersWin(Arena arena) {
		ArenaHandler.sendFMessage(arena, ConfigC.normal_winSeekers);
		for (Player player : arena.playersInArena) {
			if (arena.seekersWinCommands != null) {
				for (String command : arena.seekersWinCommands) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", player.getName()));
				}
				if (W.config.getFile().getBoolean("vaultSupport")) {
					if (BlockHunt.econ != null) {
						BlockHunt.econ.depositPlayer(player.getName(), arena.seekersTokenWin);
						MessageM.sendFMessage(player, ConfigC.normal_addedVaultBalance,
								"amount-" + arena.seekersTokenWin);
					}
				} else {
					if (W.shop.getFile().get(player.getName() + ".tokens") == null) {
						W.shop.getFile().set(player.getName() + ".tokens", 0);
						W.shop.save();
					}
					int playerTokens = W.shop.getFile().getInt(player.getName() + ".tokens");
					W.shop.getFile().set(player.getName() + ".tokens", playerTokens + arena.seekersTokenWin);
					W.shop.save();

					MessageM.sendFMessage(player, ConfigC.normal_addedToken, "amount-" + arena.seekersTokenWin);
				}
			}
		}

		arena.seekers.clear();

		for (Player player : arena.playersInArena) {
			ArenaHandler.playerLeaveArena(player, false, false);
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		}

		arena.gameState = ArenaState.WAITING;
		arena.timer = 0;
		arena.playersInArena.clear();
	}

	public static void sendFMessage(Arena arena, ConfigC location, String... vars) {
		for (Player player : arena.playersInArena) {

			Object locationObject = location.config.getFile().get(location.location);
			String locationString = "";

			if (locationObject != null) {
				locationString = locationObject.toString();
			}

			String pMessage = locationString.replaceAll("%player%", player.getName());
			player.sendMessage(MessageM.replaceAll(pMessage, vars));
		}
	}

	public static void sendMessage(Arena arena, String message, String... vars) {
		for (Player player : arena.playersInArena) {
			String pMessage = message.replaceAll("%player%", player.getName());
			player.sendMessage(MessageM.replaceAll(pMessage, vars));
		}
	}

	public static void stopArena(Arena arena) {
		ArenaHandler.sendFMessage(arena, ConfigC.warning_arenaStopped);

		arena.seekers.clear();

		for (Player player : arena.playersInArena) {
			ArenaHandler.playerLeaveArena(player, false, false);
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		}

		arena.gameState = ArenaState.WAITING;
		arena.timer = 0;
		arena.playersInArena.clear();
	}
}
