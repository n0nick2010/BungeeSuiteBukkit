package com.mcdimensions.bungeesuitebukkit.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import com.mcdimensions.bungeesuitebukkit.BungeeSuiteBukkit;
import com.mcdimensions.bungeesuitebukkit.portals.Portal;

public class PortalLiquidListener implements Listener {

	protected BungeeSuiteBukkit plugin;
	
	private final Location cacheLoc = new Location(null, 0.0, 0.0, 0.0);

	public PortalLiquidListener(BungeeSuiteBukkit bungeeSuiteBukkit) {
		plugin = bungeeSuiteBukkit;
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockFromTo(BlockFromToEvent e) {
		for (Portal p : plugin.portals.values()) {
			if (p.getFillType().isAType(e.getBlock().getTypeId()) && p.isIn(e.getBlock().getLocation(cacheLoc))) {
				e.setCancelled(true);
				break;
			}
		}
	}
	
}
