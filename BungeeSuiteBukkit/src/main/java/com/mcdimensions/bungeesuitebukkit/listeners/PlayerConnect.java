package com.mcdimensions.bungeesuitebukkit.listeners;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mcdimensions.bungeesuitebukkit.BungeeSuiteBukkit;
import com.mcdimensions.bungeesuitebukkit.utilities.PluginMessageTask;

public class PlayerConnect implements Listener {
	BungeeSuiteBukkit plugin;
	
	public PlayerConnect(BungeeSuiteBukkit bungeeSuiteBukkit) {
		plugin = bungeeSuiteBukkit;
	}

	@EventHandler
	public void login(PlayerJoinEvent event){
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		Player player = event.getPlayer();
		try{
			out.writeUTF("JoinEvent");
			out.writeUTF(player.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		new PluginMessageTask(this.plugin, player, b).runTaskLater(this.plugin, 5);
	}

}
