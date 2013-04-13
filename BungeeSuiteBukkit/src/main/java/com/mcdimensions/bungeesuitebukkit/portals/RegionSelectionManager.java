package com.mcdimensions.bungeesuitebukkit.portals;


import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mcdimensions.bungeesuitebukkit.BungeeSuiteBukkit;

public class RegionSelectionManager implements Listener {
	private Material selectionTools = Material.WOOD_AXE;
	private HashMap<String, Region> selections = new HashMap<String, Region>();
	BungeeSuiteBukkit plugin;
	public RegionSelectionManager(BungeeSuiteBukkit bungeeSuiteBukkit) {
		plugin = bungeeSuiteBukkit;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if (e.getItem() != null && e.getItem().getType() == selectionTools && e.getClickedBlock() != null && e.getPlayer().isOp())
		{
			Region v = this.selections.get(e.getPlayer().getName());
			if (v == null)
			{
				v = new Region(null, null);
				this.selections.put(e.getPlayer().getName(), v);
			}
			Location l  = e.getClickedBlock().getLocation();
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				v.setEnd(l);
					e.getPlayer().sendMessage("second_point_set");
			}
			else if (e.getAction() == Action.LEFT_CLICK_BLOCK)
			{
				v.setFirst(l);
					e.getPlayer().sendMessage("first_point_set");
			}
		}
	}

	public Region getSelection(String player)
	{
		Region v = this.selections.get(player);
		if (v != null && v.getFirst() != null && v.getEnd() != null)
			return v;
		return null;
	}
	
	public void removeSelection(String p)
	{
		this.selections.remove(p);
	}

}