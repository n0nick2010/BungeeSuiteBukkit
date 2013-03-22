package com.mcdimensions.BungeeSuiteBukkit.listeners;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.mcdimensions.BungeeSuiteBukkit.BungeeSuiteBukkit;
import com.mcdimensions.BungeeSuiteBukkit.Utilities.Utilities;
import com.mcdimensions.BungeeSuiteBukkit.signs.BungeeSign;

public class SignListener implements Listener {
	BungeeSuiteBukkit plugin;
	Utilities utils;

	public SignListener(BungeeSuiteBukkit bungeeSuiteBukkit) {
		plugin = bungeeSuiteBukkit;
		utils = plugin.utils;
	}

	@EventHandler
	public void InfoSignPlace(SignChangeEvent event) throws SQLException {
		if (!event.getPlayer().isOp())
			return;
		String line1 = event.getLine(0);
		String type = event.getLine(1);
		String targetServer = event.getLine(2);
		if (!line1.equalsIgnoreCase("bs") || targetServer == null)
			return;
		if (!(type.equalsIgnoreCase("Portal")
				|| type.equalsIgnoreCase("PlayerList") || type
					.equalsIgnoreCase("MOTD")))
			return;
		Sign sign = (Sign) event.getBlock().getState();
		event.setCancelled(true);
		if (!plugin.utils.serverExists(targetServer)) {
			plugin.SignHandler.serverDoesNotExistSign(sign, targetServer);
			event.getPlayer().sendMessage(
					ChatColor.RED + "Server does not exist");
			return;
		}
		if (type.equalsIgnoreCase("Portal")) {
			BungeeSign bs = new BungeeSign("Portal", sign, targetServer);
			plugin.signs.get(targetServer).add(bs);
			plugin.SignHandler.setLines(bs);
			plugin.utils.createSign(bs);
			event.getPlayer().sendMessage(
					ChatColor.DARK_GREEN + "BungeeSign Created!");
		}
		if (type.equalsIgnoreCase("PlayerList")) {
			BungeeSign bs = new BungeeSign("PlayerList", sign, targetServer);
			plugin.signs.get(targetServer).add(bs);
			plugin.SignHandler.setLines(bs);
			plugin.utils.createSign(bs);
			event.getPlayer().sendMessage(
					ChatColor.DARK_GREEN + "BungeeSign Created!");
		}
		if (type.equalsIgnoreCase("MOTD")) {
			BungeeSign bs = new BungeeSign("MOTD", sign, targetServer);
			plugin.signs.get(targetServer).add(bs);
			plugin.SignHandler.setLines(bs);
			plugin.utils.createSign(bs);
			event.getPlayer().sendMessage(
					ChatColor.DARK_GREEN + "BungeeSign Created!");
		}
	}

	@EventHandler
	public void signDestroy(BlockBreakEvent event) throws SQLException {
		
		BlockState block = event.getBlock().getState();
		if (!(block instanceof Sign))
			return;
		Sign sign = (Sign) block;
		String targetServer = plugin.SignHandler.signExists(sign);
		if (targetServer == null)
			return;
		if(!event.getPlayer().isOp()){
			event.setCancelled(true);
			return;
		}
		for (BungeeSign data : plugin.signs.get(targetServer)) {
			if (data.getSign().equals(sign)) {
				plugin.signs.get(targetServer).remove(data);
				plugin.SignHandler.deleteSign(sign);
				plugin.AllSigns.remove(sign);
				break;
			}
		}
	}

	@EventHandler
	public void signInteract(PlayerInteractEvent event) throws SQLException {
		if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;
		if (!(event.getClickedBlock().getState() instanceof Sign))
			return;
		Sign sign = (Sign) event.getClickedBlock().getState();
		String type = plugin.SignHandler.signExists(sign);
		if (type == null)
			return;
		BungeeSign bs = plugin.SignHandler.getSign(sign, type);
		plugin.SignHandler.playerClick(event.getPlayer(), bs);
	}

	@EventHandler
	public void playerConnect(PlayerLoginEvent event) throws SQLException {
		utils.updatePlayerCount(1);
		return;
	}

	@EventHandler
	public void playerDisconnect(PlayerQuitEvent event) throws SQLException {
		utils.updatePlayerCount(-1);
		return;
	}

	@EventHandler
	public void ChunkUnload(ChunkUnloadEvent event) {
		for (BungeeSign data : plugin.AllSigns) {
			if (data.getSign().getChunk().equals(event.getChunk())) {
				event.setCancelled(true);
			}
		}
	}
	

}
