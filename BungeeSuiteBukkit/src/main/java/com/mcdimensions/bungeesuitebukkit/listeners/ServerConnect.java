package com.mcdimensions.bungeesuitebukkit.listeners;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mcdimensions.bungeesuitebukkit.BungeeSuiteBukkit;
import com.mcdimensions.bungeesuitebukkit.utilities.PluginMessageTask;

public class ServerConnect implements Listener {
	BungeeSuiteBukkit plugin;
	
	public ServerConnect(BungeeSuiteBukkit bungeeSuiteBukkit) {
		plugin=bungeeSuiteBukkit;
	}

	@EventHandler
	public void login(PlayerJoinEvent event){
		
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try{
			out.writeUTF("JoinEvent");
			
		} catch (IOException e){
			
		}
		try{
			out.writeUTF(event.getPlayer().getName());
		} catch (IOException e){
			
		}
		new PluginMessageTask(this.plugin, event.getPlayer(), b).runTaskLater(this.plugin, 5);
	}

}
