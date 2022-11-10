package org.EncryptSL.blockhunt.Listeners;

import org.EncryptSL.blockhunt.*;
import org.EncryptSL.blockhunt.Arena.ArenaType;
import org.EncryptSL.blockhunt.Managers.MessageM;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class OnInventoryClickEvent implements Listener {

    public static void updownButton(Player player, ItemStack item, Arena arena, ArenaType at, int option, int max,
                                    int min, int add, int remove) {
        if (item.getItemMeta() == null) {
            return;
        }
        if (item.getItemMeta().getDisplayName()
                .contains((String) W.messages.get(ConfigC.button_add2))) {
            if (option < max) {
                switch (at) {
                    case maxPlayers:
                        arena.maxPlayers = option + add;
                        break;
                    case minPlayers:
                        arena.minPlayers = option + add;
                        break;
                    case amountSeekersOnStart:
                        arena.amountSeekersOnStart = option + add;
                        break;
                    case timeInLobbyUntilStart:
                        arena.timeInLobbyUntilStart = option + add;
                        break;
                    case waitingTimeSeeker:
                        arena.waitingTimeSeeker = option + add;
                        break;
                    case gameTime:
                        arena.gameTime = option + add;
                        break;
                    case timeUntilHidersSword:
                        arena.timeUntilHidersSword = option + add;
                        break;
                    case hidersTokenWin:
                        arena.hidersTokenWin = option + add;
                        break;
                    case seekersTokenWin:
                        arena.seekersTokenWin = option + add;
                        break;
                    case killTokens:
                        arena.killTokens = option + add;
                        break;
                }
            } else {
                MessageM.sendFMessage(player, ConfigC.error_setTooHighNumber, "max-" + max);
            }
        } else if (item.getItemMeta().getDisplayName().contains((String) W.messages.get(ConfigC.button_remove2))) {
            if (option > min) {
                switch (at) {
                    case maxPlayers:
                        arena.maxPlayers = option - remove;
                        break;
                    case minPlayers:
                        arena.minPlayers = option - remove;
                        break;
                    case amountSeekersOnStart:
                        arena.amountSeekersOnStart = option - remove;
                        break;
                    case timeInLobbyUntilStart:
                        arena.timeInLobbyUntilStart = option - remove;
                        break;
                    case waitingTimeSeeker:
                        arena.waitingTimeSeeker = option - remove;
                        break;
                    case gameTime:
                        arena.gameTime = option - remove;
                        break;
                    case timeUntilHidersSword:
                        arena.timeUntilHidersSword = option - remove;
                        break;
                    case hidersTokenWin:
                        arena.hidersTokenWin = option - remove;
                        break;
                    case seekersTokenWin:
                        arena.seekersTokenWin = option - remove;
                        break;
                    case killTokens:
                        arena.killTokens = option - remove;
                        break;
                }
            } else {
                MessageM.sendFMessage(player, ConfigC.error_setTooLowNumber, "min-" + min);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        for (Arena arena : W.arenaList) {
            if (arena.playersInArena.contains(player) && !arena.seekers.contains(player)) {
                if (event.getSlot() == 8 || event.getSlot() == 39) {
                    event.setCancelled(true);
                }
            }
        }
        Inventory inv = event.getInventory();
        if (inv.getType().equals(InventoryType.CHEST)) {
            if (event.getView().getTitle().contains("DisguiseBlocks")) {
                if (event.getCurrentItem() != null) {
                    if (!event.getCurrentItem().getType().isBlock()) {
                        if (!event.getCurrentItem().getType().equals(Material.FLOWER_POT)) {
                            event.setCancelled(true);
                            MessageM.sendFMessage(player, ConfigC.error_setNotABlock);
                        }
                    }
                }
            } else if (event.getView().getTitle().startsWith("\u00A7r")) {
                if (event.getView().getTitle().equals(MessageM.replaceAll("\u00A7r" + W.config.get(ConfigC.shop_title)))) {
                    event.setCancelled(true);
                    ItemStack item = event.getCurrentItem();
                    if (W.shop.getFile().get(player.getName() + ".tokens") == null) {
                        W.shop.getFile().set(player.getName() + ".tokens", 0);
                        W.shop.save();
                    }
                    int playerTokens = W.shop.getFile().getInt(player.getName() + ".tokens");
                    if (item == null)
                        return;
                    if (item.getType().equals(Material.AIR))
                        return;
                    if (item.getItemMeta() == null) {
                        return;
                    }
                    if (item.getItemMeta().getDisplayName().equals(
                            MessageM.replaceAll(W.config.get(ConfigC.shop_blockChooserv1Name).toString())
                    )) {
                        if (W.config.getFile().getBoolean("vaultSupport")) {
                            if (BlockHunt.econ != null) {
                                int vaultBalance = (int) BlockHunt.econ.getBalance(player);
                                if (vaultBalance >= W.config.getFile().getInt("blockChooserPrice")) {
                                    W.shop.getFile().set(player.getName() + ".blockchooser", true);
                                    BlockHunt.econ.depositPlayer(player, W.config.getFile().getInt("blockChooserPrice"));
                                    W.shop.save();
                                    MessageM.sendFMessage(player,
                                            ConfigC.normal_shopBoughtItem,
                                            "itemname-" + W.config.get(ConfigC.shop_blockChooserv1Name)
                                    );
                                } else {
                                    MessageM.sendFMessage(player, ConfigC.error_shopNeedMoreMoney);
                                }
                            }
                        } else {
                            if (playerTokens >= (Integer) W.config.get(ConfigC.shop_blockChooserv1Price)) {
                                W.shop.getFile().set(player.getName() + ".blockchooser", true);
                                W.shop.getFile().set(
                                        player.getName() + ".tokens",
                                        playerTokens - W.config.getFile().getInt("blockChooserPrice")
                                );
                                W.shop.save();
                                MessageM.sendFMessage(
                                        player,
                                        ConfigC.normal_shopBoughtItem,
                                        "itemname-" + W.config.get(ConfigC.shop_blockChooserv1Name)
                                );
                            } else {
                                MessageM.sendFMessage(player, ConfigC.error_shopNeedMoreTokens);
                            }
                        }
                    } else if (item.getItemMeta().getDisplayName().equals(
                            MessageM.replaceAll(W.config.get(ConfigC.shop_BlockHuntPassv2Name).toString())
                    )) {
                        if (W.config.getFile().getBoolean("vaultSupport")) {
                            if (BlockHunt.econ != null) {
                                int vaultBalance = (int) BlockHunt.econ.getBalance(player);
                                if (vaultBalance >= W.config.getFile().getInt("seekerHiderPrice")) {
                                    if (W.shop.getFile().get(player.getName() + ".blockhuntpass") == null) {
                                        W.shop.getFile().set(player.getName() + ".blockhuntpass", 0);
                                        W.shop.save();
                                    }

                                    Object o = W.shop.getFile().get(player.getName() + ".blockhuntpass");

                                    W.shop.getFile().set(
                                            player.getName() + ".blockhuntpass",
                                            (Integer) (o != null ? o : 0) + 1
                                    );
                                    BlockHunt.econ.depositPlayer(
                                            player,
                                            W.config.getFile().getInt("seekerHiderPrice")
                                    );
                                    W.shop.save();
                                    MessageM.sendFMessage(
                                            player,
                                            ConfigC.normal_shopBoughtItem,
                                            "itemname-" + W.config.get(ConfigC.shop_BlockHuntPassv2Name)
                                    );
                                } else {
                                    MessageM.sendFMessage(player, ConfigC.error_shopNeedMoreMoney);
                                }
                            }
                        } else {
                            if (playerTokens >= (Integer) W.config
                                    .get(ConfigC.shop_BlockHuntPassv2Price)) {
                                if (W.shop.getFile().get(
                                        player.getName() + ".blockhuntpass") == null) {
                                    W.shop.getFile().set(player.getName() + ".blockhuntpass", 0);
                                    W.shop.save();
                                }

                                Object o = W.shop.getFile().get(player.getName() + ".blockhuntpass");

                                W.shop.getFile().set(
                                        player.getName() + ".blockhuntpass",
                                        (Integer) (o != null ? o : 0)  + 1
                                );
                                W.shop.getFile().set(
                                        player.getName() + ".tokens",
                                        playerTokens - W.config.getFile().getInt("seekerHiderPrice")
                                );
                                W.shop.save();
                                MessageM.sendFMessage(
                                        player,
                                        ConfigC.normal_shopBoughtItem,
                                        "itemname-" + W.config.get(ConfigC.shop_BlockHuntPassv2Name)
                                );
                            } else {
                                MessageM.sendFMessage(player, ConfigC.error_shopNeedMoreTokens);
                            }
                        }
                    }

                    InventoryHandler.openShop(player);
                } else if (event.getView().getTitle().contains(
                        MessageM.replaceAll((String) W.config.get(ConfigC.shop_blockChooserv1Name)))) {
                    event.setCancelled(true);
                    if (event.getCurrentItem() == null) {
                        return;
                    }
                    if (event.getCurrentItem().getType() != Material.AIR) {
                        if (event.getCurrentItem().getType().isBlock()) {
                            W.choosenBlock.put(player, event.getCurrentItem());
                            MessageM.sendFMessage(
                                    player,
                                    ConfigC.normal_shopChoosenBlock,
                                    "block-"
                                            + event.getCurrentItem().getType()
                                            .toString()
                                            .replaceAll("_", "")
                                            .replaceAll("BLOCK", "")
                                            .toLowerCase());
                        } else {
                            MessageM.sendFMessage(player, ConfigC.error_setNotABlock);
                        }
                    }
                } else if (event.getView().getTitle().contains(
                        MessageM.replaceAll((String) W.config.get(ConfigC.shop_BlockHuntPassv2Name)))) {
                    event.setCancelled(true);
                    if (event.getCurrentItem() == null) {
                        return;
                    }
                    if (event.getCurrentItem().getType() != Material.AIR) {
                        if (event.getCurrentItem().getType().equals(Material.WHITE_WOOL)
                                && event.getCurrentItem().getItemMeta() != null
                                && ((Damageable) event.getCurrentItem().getItemMeta()).getDamage() == 11) {
                            int i = 0;
                            for (Arena arena : W.arenaList) {
                                if (arena.playersInArena.contains(player)) {
                                    for (Player playerCheck : arena.playersInArena) {
                                        if (W.choosenSeeker.get(playerCheck) != null) {
                                            if (W.choosenSeeker.get(playerCheck)) {
                                                i = i + 1;
                                            }
                                        }
                                    }
                                }

                                if (i >= arena.amountSeekersOnStart) {
                                    MessageM.sendFMessage(player, ConfigC.error_shopMaxSeekersReached);
                                } else {
                                    W.choosenSeeker.put(player, true);
                                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                    player.updateInventory();
                                    MessageM.sendFMessage(player, ConfigC.normal_shopChoosenSeeker);
                                    inv.clear();
                                    if (W.shop.getFile().getInt(player.getName() + ".blockhuntpass") == 1) {
                                        W.shop.getFile().set(player.getName() + ".blockhuntpass", null);
                                        W.shop.save();
                                    } else {
                                        W.shop.getFile().set(
                                                player.getName() + ".blockhuntpass",
                                                W.shop.getFile().getInt(player.getName() + ".blockhuntpass") - 1
                                        );
                                        W.shop.save();
                                    }
                                }
                            }

                        } else if (event.getCurrentItem().getType().equals(Material.WHITE_WOOL)
                                && event.getCurrentItem().getItemMeta() != null
                                && ((Damageable) event.getCurrentItem().getItemMeta()).getDamage() == 14) {
                            int i = 0;
                            for (Arena arena : W.arenaList) {
                                if (arena.playersInArena.contains(player)) {
                                    for (Player playerCheck : arena.playersInArena) {
                                        if (W.choosenSeeker.get(playerCheck) != null) {
                                            if (!W.choosenSeeker.get(playerCheck)) {
                                                i = i + 1;
                                            }
                                        }
                                    }
                                }

                                if (i >= (arena.playersInArena.size() - 1)) {
                                    MessageM.sendFMessage(player, ConfigC.error_shopMaxHidersReached);
                                } else {
                                    W.choosenSeeker.put(player, false);
                                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                    player.updateInventory();
                                    MessageM.sendFMessage(player, ConfigC.normal_shopChoosenHiders);
                                    inv.clear();
                                    if (W.shop.getFile().getInt(player.getName() + ".blockhuntpass") == 1) {
                                        W.shop.getFile().set(player.getName() + ".blockhuntpass", null);
                                        W.shop.save();
                                    } else {
                                        W.shop.getFile().set(
                                                player.getName() + ".blockhuntpass",
                                                W.shop.getFile().getInt(player.getName() + ".blockhuntpass") - 1
                                        );
                                        W.shop.save();
                                    }
                                }
                            }
                        }
                    }
                } else {
                    event.setCancelled(true);
                    ItemStack item = event.getCurrentItem();
                    ItemStack item2 = inv.getItem(0);
                    if (item2 == null) {
                        return;
                    }
                    ItemMeta itemMeta = item2.getItemMeta();
                    if (itemMeta == null) {
                        return;
                    }
                    String arenaName = itemMeta.getDisplayName().replaceAll(MessageM.replaceAll("%NSettings of arena: %A"), "");

                    Arena arena = null;
                    for (Arena arena2 : W.arenaList) {
                        if (arena2.arenaName.equalsIgnoreCase(arenaName)) {
                            arena = arena2;
                        }
                    }
                    if (arena == null) {
                        return;
                    }
                    if (item == null)
                        return;
                    if (item.getType().equals(Material.AIR))
                        return;
                    if (item.getItemMeta() == null) {
                        return;
                    }
                    if (!item.getItemMeta().hasDisplayName())
                        return;
                    if (item.getType().equals(Material.GOLD_NUGGET)) {
                        if (item.getItemMeta().getDisplayName().contains("maxPlayers")) {
                            updownButton(player, item, arena, ArenaType.maxPlayers, arena.maxPlayers,
                                    Bukkit.getMaxPlayers(), 2, 1, 1
                            );
                        } else if (item.getItemMeta().getDisplayName().contains("minPlayers")) {
                            updownButton(
                                    player, item, arena, ArenaType.minPlayers, arena.minPlayers,
                                    Bukkit.getMaxPlayers() - 1, 2, 1, 1
                            );
                        } else if (item.getItemMeta().getDisplayName().contains("amountSeekersOnStart")) {
                            updownButton(player, item, arena, ArenaType.amountSeekersOnStart, arena.amountSeekersOnStart,
                                    arena.maxPlayers - 1, 1, 1, 1
                            );
                        } else if (item.getItemMeta().getDisplayName().contains("timeInLobbyUntilStart")) {
                            updownButton(player, item, arena, ArenaType.timeInLobbyUntilStart,
                                    arena.timeInLobbyUntilStart, 1000, 5, 1, 1
                            );
                        } else if (item.getItemMeta().getDisplayName().contains("waitingTimeSeeker")) {
                            updownButton(player, item, arena, ArenaType.waitingTimeSeeker, arena.waitingTimeSeeker,
                                    1000, 5, 1, 1
                            );
                        } else if (item.getItemMeta().getDisplayName().contains("gameTime")) {
                            updownButton(player, item, arena, ArenaType.gameTime, arena.gameTime, 1000, 5, 1, 1);
                        } else if (item.getItemMeta().getDisplayName().contains("timeUntilHidersSword")) {
                            updownButton(player, item, arena, ArenaType.timeUntilHidersSword, arena.timeUntilHidersSword,
                                    1000, 0, 1, 1
                            );
                        } else if (item.getItemMeta().getDisplayName().contains("hidersTokenWin")) {
                            updownButton(player, item, arena, ArenaType.hidersTokenWin, arena.hidersTokenWin, 1000,
                                    0, 1, 1
                            );
                        } else if (item.getItemMeta().getDisplayName().contains("seekersTokenWin")) {
                            updownButton(player, item, arena, ArenaType.seekersTokenWin, arena.seekersTokenWin,
                                    1000, 0, 1, 1
                            );
                        } else if (item.getItemMeta().getDisplayName().contains("killTokens")) {
                            updownButton(player, item, arena, ArenaType.killTokens, arena.killTokens, 1000,
                                    0, 1, 1
                            );
                        }

                        save(arena);
                        InventoryHandler.openPanel(player, arena.arenaName);

                    } else if (item.getType().equals(Material.BOOK)) {
                        if (item.getItemMeta().getDisplayName().contains("disguiseBlocks")) {
                            InventoryHandler.openDisguiseBlocks(arena, player);
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
