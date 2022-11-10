package org.EncryptSL.blockhunt.Commands;

import org.EncryptSL.blockhunt.Arena;
import org.EncryptSL.blockhunt.BlockHunt;
import org.EncryptSL.blockhunt.ConfigC;
import org.EncryptSL.blockhunt.W;
import org.EncryptSL.blockhunt.Managers.MessageM;
import org.EncryptSL.blockhunt.Serializables.LocationSerializable;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDremove extends DefaultCMD {

	@Override
	public boolean exectue(Player player, Command cmd, String label,
			String[] args) {
		if (player != null) {
			if (args.length <= 1) {
				MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments,
						"syntax-" + BlockHunt.CMDremove.usage);
			} else {
				for (Arena arena : W.arenaList) {
					if (args[1].equalsIgnoreCase(arena.arenaName)) {
						MessageM.sendFMessage(player,
								ConfigC.normal_removeRemovedArena, "name-"
										+ args[1]);
						W.arenas.getFile().set(args[1], null);
						for (String sign : W.signs.getFile().getKeys(false)) {
							Object o = W.signs.getFile().get(sign + ".arenaName");
							if (o != null && o.toString().equalsIgnoreCase(args[1])) {
								Object ls = W.signs.getFile().get(sign + ".location");
								if (ls != null) {
									LocationSerializable signLoc = new LocationSerializable((Location) ls);
									signLoc.getBlock().setType(Material.AIR);
									World w = signLoc.getWorld();
									if (w != null) {
										w.playEffect(signLoc, Effect.MOBSPAWNER_FLAMES, 0);
										w.playSound(signLoc, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
									}
									W.signs.getFile().set(sign, null);
								}
							}
						}

						W.arenas.save();
						W.signs.load();

						W.arenaList.remove(arena);
						return true;
					}
				}

				MessageM.sendFMessage(player, ConfigC.error_noArena, "name-"
						+ args[1]);
			}
		} else {
			MessageM.sendFMessage(player, ConfigC.error_onlyIngame);
		}
		return true;
	}
}
