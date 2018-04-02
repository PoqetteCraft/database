package com.github.pocketkid2.database;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import net.md_5.bungee.api.ChatColor;

public class DatabaseCommand implements CommandExecutor {

	private DatabasePlugin plugin;

	public DatabaseCommand(DatabasePlugin pl) {
		plugin = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			// Show status/info to user
			PluginDescriptionFile pdf = plugin.getDescription();
			sender.sendMessage(pdf.getFullName());
			sender.sendMessage("By " + String.join(", ", pdf.getAuthors()));
			sender.sendMessage("Current database status: " + (plugin.isActive() ? (ChatColor.GREEN + "online") : (ChatColor.RED + "offline")));
			return false;
		} else if (args.length == 1) {
			// Read argument
			if (args[0].equalsIgnoreCase("connect")) {
				// Check current state
				if (plugin.isActive()) {
					sender.sendMessage("The database is already online!");
				} else {
					// Attempt connection
					Database.connect();
					// And display the results
					if (plugin.isActive()) {
						sender.sendMessage("Connection successful!");
					} else {
						sender.sendMessage("Connection unsuccessful!");
					}
				}
			} else if (args[0].equalsIgnoreCase("disconnect")) {
				// Check current state
				if (!plugin.isActive()) {
					sender.sendMessage("The database is already offline!");
				} else {
					// Disconnect
					Database.disconnect();
					sender.sendMessage("Disconnected!");
				}
			} else if (args[0].equalsIgnoreCase("reconnect")) {
				// Check current state
				if (!plugin.isActive()) {
					sender.sendMessage("The database is offline! Try /" + label + " connect");
				} else {
					// Attempt reconnection
					Database.disconnect();
					Database.connect();
					// Read off results
					if (plugin.isActive()) {
						sender.sendMessage("Reconnection successful!");
					} else {
						sender.sendMessage("Reconnection unsuccessful!");
					}
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

}