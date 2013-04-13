package com.mcdimensions.BungeeSuiteBukkit.listeners;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mcdimensions.BungeeSuiteBukkit.BungeeSuiteBukkit;
import com.mcdimensions.BungeeSuiteBukkit.Utilities.PluginMessageTask;

public class serverConnect implements Listener {
	BungeeSuiteBukkit plugin;
	
	public serverConnect(BungeeSuiteBukkit bungeeSuiteBukkit) {
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
