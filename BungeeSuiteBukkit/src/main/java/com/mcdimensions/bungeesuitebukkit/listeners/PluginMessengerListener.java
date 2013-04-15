package com.mcdimensions.bungeesuitebukkit.listeners;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.mcdimensions.bungeesuitebukkit.BungeeSuiteBukkit;
import com.mcdimensions.bungeesuitebukkit.portals.FillType;
import com.mcdimensions.bungeesuitebukkit.portals.Region;
import com.mcdimensions.bungeesuitebukkit.warps.WarpLocation;

public class PluginMessengerListener implements PluginMessageListener {
	
	BungeeSuiteBukkit plugin;

	public PluginMessengerListener(BungeeSuiteBukkit bungeeSuiteBukkit) {
		plugin = bungeeSuiteBukkit;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player sender, byte[] message) {
		if (!channel.equalsIgnoreCase(BungeeSuiteBukkit.INCOMING_PLUGIN_CHANNEL))
			return;
		
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

		String chan = null;

		try {
			chan = in.readUTF();
		} catch (IOException e5) {
			e5.printStackTrace();
		}

		if (chan.equalsIgnoreCase("CreatePortal")) {
			Player player = null;
			String name = null;
			String type = null;
			String dest = null;

			try {
				player = Bukkit.getPlayer(in.readUTF());
				name = in.readUTF();
				type = in.readUTF();
				dest = in.readUTF();
			} catch (IOException e4) {
				e4.printStackTrace();
			}
			
			if (!(type.equalsIgnoreCase("warp") || type.equalsIgnoreCase("server"))) {
				player.sendMessage(ChatColor.RED + "invalid type use server or warp");
				return;
			}
			
			try {
				if (type.equalsIgnoreCase("warp") && !plugin.utils.warpExists(dest)) {
					player.sendMessage(ChatColor.RED + "That warp doesnt exist!");
					return;
				}
				
				if (type.equalsIgnoreCase("server") && !plugin.utils.serverExists(dest)) {
					player.sendMessage(ChatColor.RED + "That server doesnt exist!");
					return;
				}
				
				if (plugin.utils.portalExists(name)) {
					player.sendMessage(ChatColor.RED + "That portal already exists!");
					return;
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			FillType ft = FillType.getFillType("WATER");
			Region portalArea = plugin.rsm.getSelection(player.getName());
			if (portalArea == null) {
				player.sendMessage(ChatColor.RED + "ERROR: Make a selection with the wood axe");
				return;
			}
			
			try {
				if (type.equalsIgnoreCase("warp"))
					plugin.utils.createPortal(name, null, dest, portalArea, ft);
				else if (type.equalsIgnoreCase("server"))
					plugin.utils.createPortal(name, dest, null, portalArea, ft);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			player.sendMessage(ChatColor.DARK_GREEN + "Portal " + name + " created!");
			return;
		}
		
		if (chan.equalsIgnoreCase("DeletePortal")) {
			String portalName = null;
			try {
				portalName = in.readUTF();
				plugin.utils.deletePortal(portalName);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (chan.equalsIgnoreCase("SetWarp")) {
			Player player = null;
			String name = null;
			boolean visible = true;

			try {
				player = Bukkit.getPlayer(in.readUTF());
				name = in.readUTF();
				
				if (plugin.utils.warpExists(name)) {
					player.sendMessage(ChatColor.RED + "Warp already exists");
					return;
				}
			
				visible = in.readBoolean();
				plugin.utils.createWarp(name, player.getLocation(), visible);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			player.sendMessage(ChatColor.DARK_GREEN + "Warp " + name + " created!");
			return;
		}
		
		if (chan.equalsIgnoreCase("warp")) {
			WarpLocation wl = null;
			String warp[] = null;
			String warps = null;
			Player player = null;
			
			try {
				player = Bukkit.getPlayer(in.readUTF());
				warps = in.readUTF();
				warp = warps.split("~");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			wl = new WarpLocation(warp[0], warp[1],
					Double.parseDouble(warp[2]), Double.parseDouble(warp[3]),
					Double.parseDouble(warp[4]), Float.parseFloat(warp[5]),
					Float.parseFloat(warp[6]));
			
			Chunk c = wl.getLocation().getChunk();
			c.load();
			while (!c.isLoaded()) {
				c.load();
			}
			
			player.teleport(wl.getLocation());
			return;
		}
		
		if (chan.equalsIgnoreCase("Teleport")) {
			Player player = null;
			String targetPlayer = null;

			try {
				player = Bukkit.getPlayer(in.readUTF());
				targetPlayer = in.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Player tp = Bukkit.getPlayer(targetPlayer);
			player.teleport(tp);
			return;
		}
	}

}
