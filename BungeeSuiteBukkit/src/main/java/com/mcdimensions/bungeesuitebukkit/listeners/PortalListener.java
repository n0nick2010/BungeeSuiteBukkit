package com.mcdimensions.bungeesuitebukkit.listeners;

import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.mcdimensions.bungeesuitebukkit.BungeeSuiteBukkit;
import com.mcdimensions.bungeesuitebukkit.portals.Portal;
import com.mcdimensions.bungeesuitebukkit.utilities.Utilities;

public class PortalListener implements Listener {

	BungeeSuiteBukkit plugin;
	Utilities utils;

	public PortalListener(BungeeSuiteBukkit bungeeSuiteBukkit) {
		plugin = bungeeSuiteBukkit;
		utils = plugin.utils;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent e) throws IOException {
		Player p = e.getPlayer();
		Location f = e.getFrom();
		Location t = e.getTo();
		
		if (f.getBlockX() != t.getBlockX() || f.getBlockZ() != t.getBlockZ() || f.getBlockY() != t.getBlockY()) {
			Portal portal = null;
			// NOTE 't.getY() + 0.5' makes sure it fetches the correct block id when walking on half-slabs
			int movingThrough = t.getWorld().getBlockTypeIdAt(t.getBlockX(), (int) (t.getY() + 0.5), t.getBlockZ());

			for (Portal portal2 : plugin.portals.values()) {
				if (portal2.isActive() && portal2.getFillType().isAType(movingThrough) && portal2.isIn(t)) {
					portal = portal2;
					break;
				}
			}
			
			if (portal != null) {
				
				if (portal.isIn(e.getFrom()))
					return;

				if (portal.hasWarp())
					utils.warp(portal.getWarp(), e.getPlayer());
				else
					utils.TeleportPlayerServer(portal.getToServer(), p);

			}
		}
	}

}
