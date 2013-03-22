package com.mcdimensions.BungeeSuiteBukkit.listeners;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import com.mcdimensions.BungeeSuiteBukkit.BungeeSuiteBukkit;
import com.mcdimensions.BungeeSuiteBukkit.PluginMessageTask;

public class VaultListener implements Listener {
	BungeeSuiteBukkit plugin;

	public VaultListener(BungeeSuiteBukkit bungeeSuiteBukkit) {
		this.plugin = bungeeSuiteBukkit;
	}

	@EventHandler
	public void login(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String prefix = "";
		String suffix = "";
		if (plugin.groupFixes) {
			String group = plugin.chat.getPrimaryGroup(player);
			prefix = plugin.chat.getGroupPrefix(player.getWorld(), group);
			suffix = plugin.chat.getGroupSuffix(player.getWorld(), group);
		} else {
			prefix = plugin.chat.getPlayerPrefix(player);
			suffix = plugin.chat.getPlayerSuffix(player);
		}
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("PrefixSuffix");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.writeUTF(player.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.writeUTF(prefix);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			out.writeUTF(suffix);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BukkitTask task = new PluginMessageTask(this.plugin, event.getPlayer(), b).runTaskLater(this.plugin, 1);
	}

}
