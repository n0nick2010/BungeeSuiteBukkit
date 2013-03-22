package com.mcdimensions.BungeeSuiteBukkit;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

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
				BukkitTask task = new PluginMessageTask(this.plugin, event.getPlayer(), b).runTaskLater(this.plugin, 5);
	}

}
