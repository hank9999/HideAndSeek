package org.EncryptSL.blockhunt.Commands;

import java.util.ArrayList;
import java.util.List;

import org.EncryptSL.blockhunt.ConfigC;
import org.EncryptSL.blockhunt.W;
import org.EncryptSL.blockhunt.Managers.MessageM;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CMDwand extends DefaultCMD {

	@Override
	public boolean exectue(Player player, Command cmd, String label,
			String[] args) {
		if (player != null) {
			Material m = Material.getMaterial((String) W.config.get(ConfigC.wandIDname));
			if (m == null) {
				m = Material.STONE;
			}
			ItemStack wand = new ItemStack(m);
			ItemMeta im = wand.getItemMeta();
			if (im != null) {
				im.setDisplayName(MessageM.replaceAll((String) W.config.get(ConfigC.wandName)));
				W.config.load();
				List<String> lores = W.config.getFile().getStringList(
						ConfigC.wandDescription.location);
				List<String> lores2 = new ArrayList<>();
				for (String lore : lores) {
					lores2.add(MessageM.replaceAll(lore));
				}

				im.setLore(lores2);
				wand.setItemMeta(im);
				player.getInventory().addItem(wand);
				player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 0);
				MessageM.sendFMessage(
						player,
						ConfigC.normal_wandGaveWand,
						"type-" + wand.getType().toString().replaceAll("_", " ").toLowerCase());
			}
		} else {
			MessageM.sendFMessage(null, ConfigC.error_onlyIngame);
		}
		return true;
	}
}
