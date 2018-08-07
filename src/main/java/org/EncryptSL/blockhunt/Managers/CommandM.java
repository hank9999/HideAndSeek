package org.EncryptSL.blockhunt.Managers;

import java.util.List;

import org.EncryptSL.blockhunt.ConfigC;
import org.EncryptSL.blockhunt.PermissionsC.Permissions;
import org.EncryptSL.blockhunt.W;
import org.EncryptSL.blockhunt.Commands.DefaultCMD;

public class CommandM {
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

	public String name;
	public String label;
	public String args;
	public String argsalias;
	public Permissions permission;
	public ConfigC help;
	public boolean enabled;
	public List<String> mainTABlist;
	public DefaultCMD CMD;
	public String usage;

	public CommandM (String name, String label, String args, String argsalias,
			Permissions permission, ConfigC help, Boolean enabled,
			List<String> mainTABlist, DefaultCMD CMD, String usage) {
		this.name = name;
		this.label = label;
		this.args = args;
		this.argsalias = argsalias;
		this.permission = permission;
		this.help = help;
		this.enabled = enabled;
		this.mainTABlist = mainTABlist;
		this.CMD = CMD;
		this.usage = usage;

		W.commands.add(this);
	}
}
