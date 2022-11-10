package org.EncryptSL.blockhunt;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import org.EncryptSL.blockhunt.Managers.MessageM;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SolidBlockHandler {
	public static void makePlayerUnsolid(Player player) {
		ItemStack block = player.getInventory().getItem(8);
		Block pBlock = player.getLocation().getBlock();

		if (W.hiddenLoc.get(player) != null) {
			pBlock = W.hiddenLoc.get(player).getBlock();
		}

		if (block != null) {
			block.setAmount(5);
		}
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (!pl.equals(player)) {
				if (W.hiddenLocWater.get(player) != null) {
					if (W.hiddenLocWater.get(player)) {
						BlockData blockData = Material.WATER.createBlockData(String.valueOf(0));
						pl.sendBlockChange(pBlock.getLocation(), blockData);
					} else {
						BlockData blockData = Material.AIR.createBlockData(String.valueOf(0));
						pl.sendBlockChange(pBlock.getLocation(), blockData);
					}
				} else {
					BlockData blockData = Material.AIR.createBlockData(String.valueOf(0));
					pl.sendBlockChange(pBlock.getLocation(), blockData);
				}

				W.hiddenLocWater.remove(player);
			}
		}

		player.playSound(player.getLocation(), Sound.ENTITY_BAT_HURT, 1, 1);
		if (block != null) {
			block.removeEnchantment(Enchantment.DURABILITY);
		}

		for (Player playerShow : Bukkit.getOnlinePlayers()) {
			playerShow.showPlayer(BlockHunt.plugin, player);
		}

		MiscDisguise disguise = new MiscDisguise(DisguiseType.FALLING_BLOCK);
		DisguiseAPI.disguiseToAll(player, disguise);

		MessageM.sendFMessage(player, ConfigC.normal_ingameNoMoreSolid);
	}
}
