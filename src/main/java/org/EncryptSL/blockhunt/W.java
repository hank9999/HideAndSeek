package org.EncryptSL.blockhunt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.EncryptSL.blockhunt.Managers.CommandM;
import org.EncryptSL.blockhunt.Managers.ConfigM;
import org.EncryptSL.blockhunt.Serializables.LocationSerializable;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class W {
	/**
	 * Steffion's Engine - Made by Steffion.
	 * 
	 * You're allowed to use this engine for own usage, you're not allowed to
	 * republish the engine. Using this for your own plugin is allowed when a
	 * credit is placed somewhere in the plugin.
	 * 
	 * Thanks for your cooperate!
	 * 
	 * @author Steffion
	 */

	/*
	 * Standard stuff.
	 */
	public static ArrayList<String> newFiles = new ArrayList<>();
	public static ArrayList<CommandM> commands = new ArrayList<>();

	/*
	 * If you want another file to be created. Copy and paste this line.
	 */
	public static ConfigM config = new ConfigM("config");
	public static ConfigM messages = new ConfigM("messages");
	public static ConfigM arenas = new ConfigM("arenas");
	public static ConfigM signs = new ConfigM("signs");
	public static ConfigM shop = new ConfigM("shop");

	/*
	 * Add any variable you need in different classes here:
	 */

	public static HashMap<Player, LocationSerializable> pos1 = new HashMap<>();
	public static HashMap<Player, LocationSerializable> pos2 = new HashMap<>();

	public static ArrayList<Arena> arenaList = new ArrayList<>();
	public static Random random = new Random();
	public static HashMap<Player, Integer> seekertime = new HashMap<>();

	public static HashMap<Player, PlayerArenaData> pData = new HashMap<>();
	public static HashMap<Player, ItemStack> choosenBlock = new HashMap<>();
	public static HashMap<Player, Boolean> choosenSeeker = new HashMap<>();

	public static HashMap<Player, ItemStack> pBlock = new HashMap<>();
	public static HashMap<Player, Location> moveLoc = new HashMap<>();
	public static HashMap<Player, Location> hiddenLoc = new HashMap<>();
	public static HashMap<Player, Boolean> hiddenLocWater = new HashMap<>();
}
