package org.EncryptSL.blockhunt.Listeners;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.libraryaddict.disguise.DisguiseAPI;
import org.EncryptSL.blockhunt.Arena;
import org.EncryptSL.blockhunt.Arena.ArenaState;
import org.EncryptSL.blockhunt.ArenaHandler;
import org.EncryptSL.blockhunt.ConfigC;
import org.EncryptSL.blockhunt.W;
import org.EncryptSL.blockhunt.Managers.MessageM;

public class OnEntityDamageByEntityEvent implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		Player player = null;
		if (event.getEntity() instanceof Player) {
			player = (Player) event.getEntity();
		}

		Player damager = null;
		if (event.getDamager() instanceof Player) {
			damager = (Player) event.getDamager();
		}

		if (player != null) {
			for (Arena arena : W.arenaList) {
				if (arena.playersInArena.contains(player)) {
					if ((arena.gameState == ArenaState.WAITING) || (arena.gameState == ArenaState.STARTING)) {
						event.setCancelled(true);
					} else {
						if (arena.seekers.contains(player) && arena.seekers.contains(event.getDamager())) {
							event.setCancelled(true);
						} else if (arena.playersInArena.contains(player)
								&& arena.playersInArena.contains(event.getDamager())
								&& !arena.seekers.contains(event.getDamager()) && !arena.seekers.contains(player)) {
							event.setCancelled(true);
						} else {
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);

							if (event.getDamage() >= player.getHealth()) {
								AttributeInstance attributeInstance = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
								player.setHealth(attributeInstance != null ? attributeInstance.getDefaultValue() : 20.0);
								event.setCancelled(true);

								DisguiseAPI.undisguiseToAll(player);
								W.pBlock.remove(player);

								if (!arena.seekers.contains(player)) {
									if (damager != null && W.shop.getFile().get(damager.getName() + ".tokens") == null) {
										W.shop.getFile().set(damager.getName() + ".tokens", 0);
										W.shop.save();
									}
									int damagerTokens = 0;
									if (damager != null) {
										damagerTokens = W.shop.getFile().getInt(damager.getName() + ".tokens");
									}
									if (damager != null) {
										W.shop.getFile().set(damager.getName() + ".tokens",
												damagerTokens + arena.killTokens);
									}
									W.shop.save();

									MessageM.sendFMessage(damager, ConfigC.normal_addedToken,
											"amount-" + arena.killTokens);

									if (W.shop.getFile().get(player.getName() + ".tokens") == null) {
										W.shop.getFile().set(player.getName() + ".tokens", 0);
										W.shop.save();
									}
									int playerTokens = W.shop.getFile().getInt(player.getName() + ".tokens");
									float addingTokens = (arena.hidersTokenWin
											- (((float) arena.timer / (float) arena.gameTime) * arena.hidersTokenWin));
									W.shop.getFile().set(player.getName() + ".tokens",
											playerTokens + (int) addingTokens);
									W.shop.save();

									MessageM.sendFMessage(player, ConfigC.normal_addedToken,
											"amount-" + (int) addingTokens);

									arena.seekers.add(player);
									ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameHiderDied,
											"playername-" + player.getName(),
											"left-" + (arena.playersInArena.size() - arena.seekers.size()));
								} else {
									ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameSeekerDied,
											"playername-" + player.getName(), "secs-" + arena.waitingTimeSeeker);
								}

								player.getInventory().clear();
								player.updateInventory();

								if (arena.seekers.size() >= arena.playersInArena.size()) {
									ArenaHandler.seekersWin(arena);
								} else {
									DisguiseAPI.undisguiseToAll(player);
									W.seekertime.put(player, arena.waitingTimeSeeker);
									player.teleport(arena.seekersWarp);
									player.setGameMode(GameMode.SURVIVAL);
									player.setWalkSpeed(0.25F);
								}
							}
						}
					}
				}
			}
		}
	}
}
