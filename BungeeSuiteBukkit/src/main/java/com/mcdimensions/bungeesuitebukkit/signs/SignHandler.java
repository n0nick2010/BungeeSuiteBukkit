package com.mcdimensions.bungeesuitebukkit.signs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mcdimensions.bungeesuitebukkit.BungeeSuiteBukkit;



public class SignHandler extends BukkitRunnable {
	BungeeSuiteBukkit plugin;

	public SignHandler(BungeeSuiteBukkit bungeeSuiteBukkit) throws SQLException {
		this.plugin = bungeeSuiteBukkit;
		plugin.SignHandler = this;
		updateAll();
	}

	private void updateAll() throws SQLException {
		for (ServerInfo data : plugin.servers.values()) {
			for (BungeeSign sign : plugin.signs.get(data.serverName)) {
				if (sign.isType("MOTD")) {
					setLines(sign);
				}
			}

			for (BungeeSign sign : plugin.signs.get(data.serverName)) {
				if (sign.isType("PlayerList")) {
					setLines(sign);
				}
			}
			for (BungeeSign sign : plugin.signs.get(data.serverName)) {
				setStatus(sign, data.status);
			}
		}

	}

	public void run() {
		try {
			updateServerInfos();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addServer(ServerInfo server) {
		plugin.servers.put(server.serverName, server);
	}

	public void removeServer(String server) {
		plugin.servers.remove(server);
	}

	public ServerInfo getServerInfo(String name) {
		return plugin.servers.get(name);
	}

	public boolean serverInfoExists(String name) {
		return plugin.servers.containsKey(name);
	}

	public void updateServerList() throws SQLException {
		plugin.servers = plugin.utils.getServers();
	}

	public void updateSignFormats() throws SQLException {
		plugin.signFormats = plugin.utils.getSignFormats();
	}

	public void updateServerInfos() throws SQLException {
		for (ServerInfo data : plugin.servers.values()) {
			if (data.updateMOTDInfo()) {
				for (BungeeSign sign : plugin.signs.get(data.serverName)) {
					if (sign.isType("MOTD")) {
						setLines(sign);
					}
				}
			}
			if (data.updatePlayerCount()) {
				for (BungeeSign sign : plugin.signs.get(data.serverName)) {
					if (sign.isType("PlayerList")) {
						setLines(sign);
					}
				}
			}
			if (data.updateStatus()) {
				for (BungeeSign sign : plugin.signs.get(data.serverName)) {
					setStatus(sign, data.status);
				}
			}
		}

	}

	public void setLines(BungeeSign sign) throws SQLException {
		String type = sign.getType();
		if (type.equalsIgnoreCase("MOTD")) {
			String Format = "";
			if (plugin.utils.ServerStatus(sign.getServer())) {
				Format = plugin.signFormats.get("MOTDOnline");
			} else {
				Format = plugin.signFormats.get("MOTDOffline");
			}
			if (plugin.signFormats.get("ColoredMOTD").equalsIgnoreCase("true")) {
				Format = ColorString(Format);
			} else {
				Format = ChatColor.stripColor(Format);
			}
			Format = replaceVariables(Format, sign);
			String lines[] = lineSplit(Format);
			sign.setLines(lines[0], lines[1], lines[2], lines[3]);
		} else if (type.equalsIgnoreCase("playerList")) {
			String format = "";
			if (plugin.utils.ServerStatus(sign.getServer())) {
				format = plugin.signFormats.get("PlayerCountOnline");
			} else {
				format = plugin.signFormats.get("PlayerCountOffline");
			}
			format = ColorString(format);
			format = replaceVariables(format, sign);
			String lines[] = format.split(",");
			sign.setLines(lines[0], lines[1], lines[2], lines[3]);
		} else if (type.equalsIgnoreCase("Portal")) {
			String format = "";
			if (plugin.utils.ServerStatus(sign.getServer())) {
				format = plugin.signFormats.get("PortalFormatOnline");
			} else {
				format = plugin.signFormats.get("PortalFormatOffline");
			}
			format = ColorString(format);
			format = replaceVariables(format, sign);
			String lines[] = format.split(",");
			sign.setLines(lines[0], lines[1], lines[2], lines[3]);
		}
	}

	public void setStatus(BungeeSign sign, boolean status) {
		String format = "";
		String type = sign.getType();
		if (status) {
			if (type.equalsIgnoreCase("MOTD")) {
				format = plugin.signFormats.get("MOTDOnline");
			} else if (type.equalsIgnoreCase("playerList")) {
				format = plugin.signFormats.get("PlayerCountOnline");
			} else if (type.equalsIgnoreCase("Portal")) {
				format = plugin.signFormats.get("PortalFormatOnline");
			}
		} else {
			if (type.equalsIgnoreCase("MOTD")) {
				format = plugin.signFormats.get("MOTDOffline");
			} else if (type.equalsIgnoreCase("playerList")) {
				format = plugin.signFormats.get("PlayerCountOffline");
			} else if (type.equalsIgnoreCase("Portal")) {
				format = plugin.signFormats.get("PortalFormatOffline");
			}
		}
		if (!sign.Type.equalsIgnoreCase("MOTD")
				|| plugin.signFormats.get("ColoredMOTD").equalsIgnoreCase(
						"true")) {
			format = ColorString(format);
		} else {
			format = ChatColor.stripColor(format);
		}
		format = replaceVariables(format, sign);
		String lines[];
		if (type.equalsIgnoreCase("MOTD")) {
			lines = lineSplit(format);
		} else {
			lines = format.split(",");
		}
		sign.setLines(lines[0], lines[1], lines[2], lines[3]);

	}

	private String[] lineSplit(String format) {
		String lines[] = { "", "", "", "" };
		String words[] = format.split(" ");
		int count = 0;
		for (String data : words) {
			if ((lines[count].length() + data.length() <= 15)
					|| lines[count].length() == 0) {
				if (lines[count].length() == 0) {
					lines[count] += data;
				} else {
					lines[count] += " " + data;
				}
			} else if (count <= 2) {
				count++;
				lines[count] += data;
			}
		}
		return lines;
	}

	public String replaceVariables(String string, BungeeSign sign) {
		String output = "";
		ServerInfo server = plugin.servers.get(sign.targetServer);
		output = string.replace("%server", server.serverName);
		output = output.replace("%motd", server.MOTD);
		output = output.replace("%players", server.playersOnline + "/"
				+ server.maxPlayers);
		return output;
	}

	public String ColorString(String string) {
		String output = "";
		output = string.replace("&0", ChatColor.BLACK.toString());
		output = output.replace("&1", ChatColor.DARK_BLUE.toString());
		output = output.replace("&2", ChatColor.DARK_GREEN.toString());
		output = output.replace("&3", ChatColor.DARK_AQUA.toString());
		output = output.replace("&4", ChatColor.DARK_RED.toString());
		output = output.replace("&5", ChatColor.DARK_PURPLE.toString());
		output = output.replace("&6", ChatColor.GOLD.toString());
		output = output.replace("&7", ChatColor.GRAY.toString());
		output = output.replace("&8", ChatColor.DARK_GRAY.toString());
		output = output.replace("&9", ChatColor.BLUE.toString());
		output = output.replace("&a", ChatColor.GREEN.toString());
		output = output.replace("&b", ChatColor.AQUA.toString());
		output = output.replace("&c", ChatColor.RED.toString());
		output = output.replace("&d", ChatColor.LIGHT_PURPLE.toString());
		output = output.replace("&e", ChatColor.YELLOW.toString());
		output = output.replace("&f", ChatColor.WHITE.toString());
		output = output.replace("&k", ChatColor.MAGIC.toString());
		output = output.replace("&l", ChatColor.BOLD.toString());
		output = output.replace("&n", ChatColor.UNDERLINE.toString());
		output = output.replace("&o", ChatColor.ITALIC.toString());
		output = output.replace("&m", ChatColor.STRIKETHROUGH.toString());
		return output;
	}

	public void playerClick(Player player, BungeeSign bs) {
		String format = "";
		String type = bs.getType();
		ServerInfo server = plugin.servers.get(bs.targetServer);
		if (server.status) {
			if (type.equalsIgnoreCase("MOTD")) {
				format = plugin.signFormats.get("MOTDOnline");
			} else if (type.equalsIgnoreCase("PlayerList")) {
				format = plugin.signFormats.get("PlayerCountOnlineClick");
			} else if (type.equalsIgnoreCase("Portal")) {
				plugin.utils.TeleportPlayerServer(bs.targetServer, player);
				return;
			}
		} else {
			if (type.equalsIgnoreCase("MOTD")) {
				format = plugin.signFormats.get("MOTDOffline");
			} else if (type.equalsIgnoreCase("PlayerList")) {
				format = plugin.signFormats.get("PlayerCountOfflineClick");
			} else if (type.equalsIgnoreCase("Portal")) {
				format = plugin.signFormats.get("PortalFormatOfflineClick");
				return;
			}
		}
		format = replaceVariables(format, bs);
		format = ColorString(format);
		player.sendMessage(format);
	}

	public BungeeSign getSign(Sign sign, String type) {
		ArrayList<BungeeSign> list = plugin.signs.get(type);
		for (BungeeSign data : list) {
			if (data.getSign().equals(sign)) {
				return data;
			}
		}
		return null;

	}

	public String signExists(Sign sign) throws SQLException { // returns type if
																// true null if
																// false
		Connection connection = plugin.database.getConnection();
		ResultSet res = plugin.database
				.sqlQuery("SELECT TargetServer FROM BungeeSignLocations WHERE World = '"
						+ sign.getWorld().getName()
						+ "' AND Server = '"
						+ Bukkit.getServerName()
						+ "' AND X = "
						+ sign.getX()
						+ " AND Y = "
						+ sign.getY()
						+ " AND Z="
						+ sign.getZ()
						+ "",
						connection);
		String targetServer = null;
		while (res.next()) {
			try {
				targetServer = res.getString("TargetServer");
			} catch (SQLException e) {
				return null;
			}
		}
		res.close();
		connection.close();
		return targetServer;
	}

	public void deleteSign(Sign sign) throws SQLException {
		plugin.database
				.updateQuery("DELETE FROM BungeeSignLocations WHERE World = '"
						+ sign.getWorld().getName()
						+ "' AND Server = '"
						+ Bukkit.getServerName()
						+ "' AND X="
						+ sign.getX()
						+ " AND Y="
						+ sign.getY()
						+ " AND Z="
						+ sign.getZ()
						+ "");

	}

	public void serverDoesNotExistSign(Sign sign, String targetServer) {
		sign.setLine(0, "");
		sign.setLine(1, ChatColor.RED + targetServer);
		sign.setLine(2, ChatColor.RED + "Does not");
		sign.setLine(3, ChatColor.RED + "Exist");
		sign.update();

	}

}
