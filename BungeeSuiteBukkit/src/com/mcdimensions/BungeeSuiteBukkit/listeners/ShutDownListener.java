package com.mcdimensions.BungeeSuiteBukkit.listeners;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerKickEvent;

import com.mcdimensions.BungeeSuiteBukkit.BungeeSuiteBukkit;

public class ShutDownListener implements Listener {
	BungeeSuiteBukkit plugin;

	public ShutDownListener(BungeeSuiteBukkit bungeeSuiteBukkit) {
	plugin = bungeeSuiteBukkit;
	}

	
	@EventHandler
	public void shutdownEvent(PlayerKickEvent event){
		Bukkit.getLogger().info("TEST " +event.getReason());
	}
}
