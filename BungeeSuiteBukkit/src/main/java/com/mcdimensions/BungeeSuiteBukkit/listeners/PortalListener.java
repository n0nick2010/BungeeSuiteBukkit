package com.mcdimensions.BungeeSuiteBukkit.listeners;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.mcdimensions.BungeeSuiteBukkit.BungeeSuiteBukkit;
import com.mcdimensions.BungeeSuiteBukkit.Portals.Portal;
import com.mcdimensions.BungeeSuiteBukkit.Utilities.Utilities;

public class PortalListener implements Listener {

	BungeeSuiteBukkit plugin;
	Utilities utils;
	public PortalListener(BungeeSuiteBukkit bungeeSuiteBukkit) {
		plugin = bungeeSuiteBukkit;
		utils =plugin.utils;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent e) throws IOException {
		if (e.isCancelled()) { return; }
		Player p = e.getPlayer();
		if (!e.getFrom().getBlock().getLocation().equals(e.getTo().getBlock().getLocation()))
		{
			Portal portal = utils.getPortalByPosition(e.getTo());
			if (portal != null)
			{
				if(portal.hasWarp()){
					utils.warp(portal.getWarp(), e.getPlayer());
				}else{
				utils.TeleportPlayerServer(portal.getToServer(), p);
				return;
				}
				
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockFromTo(BlockFromToEvent e)
	{
			for (Portal p : plugin.getPortals())
			{
				if (p.isIn(e.getBlock().getLocation()) && !p.isIn(e.getToBlock().getLocation()))
				{
					e.setCancelled(true);
					return;
				}
			}
		
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPhysics(BlockPhysicsEvent e)
	{
		for (Portal p : plugin.getPortals())
		{
			if (p.isIn(e.getBlock().getLocation()))
			{
				e.setCancelled(true);
				return;
			}
		}
	}
}
