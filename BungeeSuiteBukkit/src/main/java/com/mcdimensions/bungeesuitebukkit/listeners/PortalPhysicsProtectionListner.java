package com.mcdimensions.bungeesuitebukkit.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import com.mcdimensions.bungeesuitebukkit.BungeeSuiteBukkit;
import com.mcdimensions.bungeesuitebukkit.portals.Portal;

public class PortalPhysicsProtectionListner implements Listener {
	
	BungeeSuiteBukkit plugin;
	
	private final Location cacheLoc = new Location(null, 0.0, 0.0, 0.0);

	public PortalPhysicsProtectionListner(BungeeSuiteBukkit bungeeSuiteBukkit) {
		plugin = bungeeSuiteBukkit;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockPhysics(BlockPhysicsEvent e) {
		for (Portal p : plugin.portals.values()) {
			if (p.isIn(e.getBlock().getLocation(cacheLoc))) {
				e.setCancelled(true);
				break;
			}
		}
	}
	
}
