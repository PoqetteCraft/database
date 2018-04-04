package com.github.pocketkid2.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
			if (label.equalsIgnoreCase("connect")) {
				connect(sender);
			} else if (label.equalsIgnoreCase("disconnect")) {
				disconnect(sender);
			} else if (label.equalsIgnoreCase("reconnect")) {
				reconnect(sender);
			} else {
				// Show status/info to user
				PluginDescriptionFile pdf = plugin.getDescription();
				sender.sendMessage(pdf.getFullName());
				sender.sendMessage("Author: " + String.join(", ", pdf.getAuthors()));
				sender.sendMessage("Current database status: " + (plugin.isOnline() ? (ChatColor.GREEN + "online") : (ChatColor.RED + "offline")));
				return false;
			}
		} else if (args.length == 1) {
			// Read argument
			if (args[0].equalsIgnoreCase("connect") || args[0].equalsIgnoreCase("con")) {
				connect(sender);
			} else if (args[0].equalsIgnoreCase("disconnect") || args[0].equalsIgnoreCase("discon")) {
				disconnect(sender);
			} else if (args[0].equalsIgnoreCase("reconnect") || args[0].equalsIgnoreCase("recon")) {
				reconnect(sender);
			} else if (args[0].equalsIgnoreCase("plugins") || args[0].equalsIgnoreCase("pl")) {
				plugins(sender);
			} else {
				sender.sendMessage(ChatColor.RED + "Unknown argument '" + args[0] + "'");
				return false;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Too many arguments!");
			return false;
		}
		return true;
	}

	private void reconnect(CommandSender sender) {
		// Check current state
		if (!plugin.isOnline()) {
			sender.sendMessage("The database is already offline!");
		} else {
			// Attempt reconnection
			Database.disconnect();
			Database.connect();
			// Read off results
			if (plugin.isOnline()) {
				sender.sendMessage("Reconnection successful!");
			} else {
				sender.sendMessage("Reconnection unsuccessful!");
			}
		}
	}

	private void disconnect(CommandSender sender) {
		// Check current state
		if (!plugin.isOnline()) {
			sender.sendMessage("The database is already offline!");
		} else {
			// Disconnect
			Database.disconnect();
			sender.sendMessage("Disconnected!");
		}
	}

	private void connect(CommandSender sender) {
		// Check current state
		if (plugin.isOnline()) {
			sender.sendMessage("The database is already online!");
		} else {
			// Attempt connection
			Database.connect();
			// And display the results
			if (plugin.isOnline()) {
				sender.sendMessage("Connection successful!");
			} else {
				sender.sendMessage("Connection unsuccessful!");
			}
		}
	}

	private void plugins(CommandSender sender) {
		// Display list of currently registered plugins
		StringBuilder sb = new StringBuilder();
		Set<DatabasePlugin> plugins = Database.getRegisteredPlugins();
		sb.append(String.format("Database Plugins (%d): ", plugins.size()));
		List<String> names = new ArrayList<String>();
		for (DatabasePlugin p : plugins) {
			names.add((p.isOnline() ? (ChatColor.GREEN) : (ChatColor.RED)) + p.getName());
		}
		sb.append(String.join(ChatColor.RESET + ", ", names));
		sender.sendMessage(sb.toString());
	}

}
