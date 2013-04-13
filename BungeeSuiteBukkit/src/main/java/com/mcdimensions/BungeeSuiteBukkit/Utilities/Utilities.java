package com.mcdimensions.BungeeSuiteBukkit.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.mcdimensions.BungeeSuiteBukkit.BungeeSuiteBukkit;
import com.mcdimensions.BungeeSuiteBukkit.Portals.FillType;
import com.mcdimensions.BungeeSuiteBukkit.Portals.Portal;
import com.mcdimensions.BungeeSuiteBukkit.Portals.Region;
import com.mcdimensions.BungeeSuiteBukkit.signs.BungeeSign;
import com.mcdimensions.BungeeSuiteBukkit.signs.ServerInfo;

public class Utilities {
	BungeeSuiteBukkit plugin;
	SQL sql;

	public Utilities(BungeeSuiteBukkit bungeeSuiteBukkit) {
		plugin = bungeeSuiteBukkit;
		sql = plugin.sql;
	}

	public boolean serverExists(String name) throws SQLException {
		sql.initialise();
		boolean check = sql
				.existanceQuery("SELECT ServerName FROM BungeeServers WHERE ServerName COLLATE Latin1_General_CS = '"
						+ name + "'");
		sql.closeConnection();
		return check;
	}

	public boolean portalExists(String name) throws SQLException {
		sql.initialise();
		boolean check = sql
				.existanceQuery("SELECT Name FROM BungeePortals WHERE Name COLLATE Latin1_General_CS = '"
						+ name + "'");
		sql.closeConnection();
		return check;
	}

	public boolean warpExists(String name) throws SQLException {
		sql.initialise();
		boolean check = sql
				.existanceQuery("SELECT Name FROM BungeeWarps WHERE Name = '"
						+ name + "'");
		sql.closeConnection();
		return check;
	}

	public boolean portalLocationExists(String name) throws SQLException {
		sql.initialise();
		boolean check = sql
				.existanceQuery("SELECT Name FROM BungeeWarps WHERE Name COLLATE Latin1_General_CS = '"
						+ name + "'");
		sql.closeConnection();
		return check;
	}

	public String getPlayers(String server) throws SQLException {
		String players = "";
		sql.initialise();
		ResultSet res = sql
				.sqlQuery("SELECT PlayersOnline, MaxPlayers FROM BungeeServers WHERE ServerName = '"
						+ server + "'");
		while (res.next()) {
			players += res.getString("PlayersOnline");
			players += "/";
			players += res.getString("MaxPlayers");
		}
		res.close();
		sql.closeConnection();
		return players;
	}

	public boolean ServerStatus(String server) throws SQLException {
		boolean check = false;
		sql.initialise();
		check = sql
				.existanceQuery("SELECT Online FROM BungeeServers WHERE ServerName = '"
						+ server + "' AND Online =TRUE");
		sql.closeConnection();
		return check;
	}

	public String getMOTD(String server) throws SQLException {
		String motd = null;
		sql.initialise();
		ResultSet res = sql
				.sqlQuery("SELECT MOTD FROM BungeeServers WHERE ServerName = '"
						+ server + "'");
		while (res.next()) {
			motd = res.getString("MOTD");
		}
		res.close();
		sql.closeConnection();
		return motd;
	}

	public void setMOTD() throws SQLException {
		sql.initialise();
		sql.standardQuery("UPDATE BungeeServers SET MOTD='" + plugin.motd
				+ "' WHERE ServerName = '" + Bukkit.getServerName() + "'");
		sql.closeConnection();
	}

	public void setOnline() throws SQLException {
		sql.initialise();
		sql.standardQuery("UPDATE BungeeServers SET Online=TRUE, MaxPlayers = "
				+ Bukkit.getMaxPlayers() + " WHERE ServerName = '"
				+ Bukkit.getServerName() + "'");
		sql.closeConnection();
	}

	public void setOffline() throws SQLException {
		sql.initialise();
		sql.standardQuery("UPDATE BungeeServers SET Online=FALSE WHERE ServerName = '"
				+ Bukkit.getServerName() + "'");
		sql.closeConnection();
	}

	public void updateAllPlayerSigns() {

	}

	public void updateAllMOTDSigns() {

	}

	public void updateAllPortSigns() {

	}

	public String getType(Sign sign) throws SQLException {
		sql.initialise();
		String type = null;
		ResultSet res = sql
				.sqlQuery("SELECT Type FROM BungeeSignLocations WHERE  World ='"
						+ sign.getWorld().getName()
						+ "' AND Server = '"
						+ Bukkit.getServerName()
						+ "' AND X="
						+ sign.getBlock().getX()
						+ " AND Y="
						+ sign.getBlock().getY()
						+ " AND Z="
						+ sign.getBlock().getZ() + "");
		while (res.next()) {
			type = res.getString("Type");
		}
		sql.closeConnection();
		return type;
	}

	public String getTarget(Sign sign) throws SQLException {
		sql.initialise();
		String target = null;
		ResultSet res = sql
				.sqlQuery("Select TargetServer FROM BungeeSignLocations WHERE Server = '"
						+ Bukkit.getServerName()
						+ "' AND World ='"
						+ sign.getWorld().getName()
						+ "' AND X="
						+ sign.getBlock().getX()
						+ " AND Y="
						+ sign.getBlock().getY()
						+ " AND Z="
						+ sign.getBlock().getZ() + "");
		while (res.next()) {
			target = res.getString("TargetServer");
		}
		sql.closeConnection();
		return target;
	}

	public void updatePlayerCount(int i) throws SQLException {
		sql.initialise();
		sql.standardQuery("UPDATE BungeeServers SET PlayersOnline="
				+ (Bukkit.getOnlinePlayers().length + i)
				+ " WHERE ServerName = '" + Bukkit.getServerName() + "'");
		sql.closeConnection();

	}

	public void createPortal(String name, String toServer, Region portalArea,
			FillType fillType) throws SQLException {
		Region region = new Region(portalArea.getFirst(), portalArea.getEnd());
		Portal portal = new Portal(name, toServer, region, fillType);
		sql.initialise();
		int MaxY = portalArea.getEnd().getBlockY();
		int MinY = portalArea.getFirst().getBlockY();
		int MaxX = portalArea.getEnd().getBlockX();
		int MinX = portalArea.getFirst().getBlockX();
		int MaxZ = portalArea.getEnd().getBlockZ();
		int MinZ = portalArea.getFirst().getBlockZ();
		sql.standardQuery("INSERT INTO BungeePortals (Name, Server, ToServer, World, XMax, XMin, YMax, YMin, ZMax, ZMin) VALUES ('"
				+ name
				+ "','"
				+ Bukkit.getServerName()
				+ "', '"
				+ toServer
				+ "','"
				+ portalArea.getEnd().getWorld().getName()
				+ "', "
				+ MaxX
				+ ", "
				+ MinX
				+ ", "
				+ MaxY
				+ ", "
				+ MinY
				+ ", "
				+ MaxZ
				+ ", " + MinZ + " )");
		sql.closeConnection();
		plugin.portals.put(name, portal);
	}

	public void createPortal(String name, String toServer, String toWarp,
			Region portalArea, FillType fillType) throws SQLException {
		if (toServer == null) {
			toServer = getWarpServer(toWarp);
		}
		Region region = new Region(portalArea.getFirst(), portalArea.getEnd());
		Portal portal = new Portal(name, toServer, toWarp, region, fillType);
		sql.initialise();
		int MaxY = portalArea.getEnd().getBlockY();
		int MinY = portalArea.getFirst().getBlockY();
		int MaxX = portalArea.getEnd().getBlockX();
		int MinX = portalArea.getFirst().getBlockX();
		int MaxZ = portalArea.getEnd().getBlockZ();
		int MinZ = portalArea.getFirst().getBlockZ();
		if (toWarp == null) {
			sql.standardQuery("INSERT INTO BungeePortals (Name, Server, ToServer, World, XMax, XMin, YMax, YMin, ZMax, ZMin) VALUES ('"
					+ name
					+ "','"
					+ Bukkit.getServerName()
					+ "', '"
					+ toServer
					+ "','"
					+ portalArea.getEnd().getWorld().getName()
					+ "', "
					+ MaxX
					+ ", "
					+ MinX
					+ ", "
					+ MaxY
					+ ", "
					+ MinY
					+ ", "
					+ MaxZ + ", " + MinZ + " )");
		} else {
			sql.standardQuery("INSERT INTO BungeePortals (Name, Server, ToServer, Warp, World, XMax, XMin, YMax, YMin, ZMax, ZMin) VALUES ('"
					+ name
					+ "','"
					+ Bukkit.getServerName()
					+ "', '"
					+ toServer
					+ "', '"
					+ toWarp
					+ "','"
					+ portalArea.getEnd().getWorld().getName()
					+ "', "
					+ MaxX
					+ ", "
					+ MinX
					+ ", "
					+ MaxY
					+ ", "
					+ MinY
					+ ", "
					+ MaxZ
					+ ", " + MinZ + " )");
		}
		sql.closeConnection();
		plugin.portals.put(name, portal);
	}

	public String getWarpServer(String warp) throws SQLException {
		sql.initialise();
		String server = null;
		ResultSet res = sql
				.sqlQuery("Select Server FROM BungeeWarps WHERE Name = '"
						+ warp + "'");
		while (res.next()) {
			server = res.getString("Server");
		}
		sql.closeConnection();
		return server;
	}

	public void deletePortal(Portal portal) throws SQLException {
		sql.initialise();
		sql.standardQuery("DELETE FROM BungeePortals WHERE Name = '"
				+ portal.getTag() + "'");
		sql.closeConnection();
		plugin.portals.remove(portal);
	}

	public void deletePortal(String portal) throws SQLException {
		sql.initialise();
		sql.standardQuery("DELETE FROM BungeePortals WHERE Name = '" + portal
				+ "'");
		sql.closeConnection();
		plugin.portals.remove(getPortal(portal));
	}

	public Portal getPortal(String name) {
		return plugin.portals.get(name);
	}

	public HashMap<String, Portal> loadPortals() throws SQLException {
		HashMap<String, Portal> portals = new HashMap<String, Portal>();
		sql.initialise();

		ResultSet res = sql
				.sqlQuery("SELECT * FROM BungeePortals WHERE Server = '"
						+ Bukkit.getServerName() + "'");
		while (res.next()) {
			String name = res.getString("Name");
			FillType ft = FillType.getFillType("WATER");
			World world = Bukkit.getWorld(res.getString("World"));
			Location first = new Location(world, res.getDouble("XMax"),
					res.getDouble("YMax"), res.getDouble("ZMax"));
			Location end = new Location(world, res.getInt("XMin"),
					res.getInt("YMin"), res.getInt("ZMin"));
			Region region = new Region(first, end);
			String toServer = res.getString("ToServer");
			String toWarp = res.getString("Warp");
			Portal portal;
			if (toWarp == null) {
				portal = new Portal(name, toServer, region, ft);
			} else {
				portal = new Portal(name, toServer, toWarp, region, ft);
			}
			portals.put(name, portal);
		}
		sql.closeConnection();
		res.close();
		return portals;
	}

	public Portal getPortalByPosition(Location l, double offset) {
		for (Portal p : plugin.getPortals()) {
			if (p.containsLocation(l, offset) && p.isActive())
				return p;
		}
		return null;
	}

	public Portal getPortalByPosition(Location l) {
		return this.getPortalByPosition(l, 0);
	}

	public void TeleportPlayerServer(String server, Player player) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);

		try {
			out.writeUTF("Connect");
			out.writeUTF(server); // Target Server
		} catch (IOException e) {
			// Can never happen
		}
		player.sendPluginMessage(this.plugin, "BungeeCord", b.toByteArray());
	}

	public void createWarp(String name, Location loc, boolean visible)
			throws SQLException {
		sql.initialise();
		sql.standardQuery("INSERT INTO BungeeWarps(Name,Server,World,X,Y,Z,Yaw,Pitch,Visible) VALUES('"
				+ name
				+ "','"
				+ Bukkit.getServerName()
				+ "', '"
				+ loc.getWorld().getName()
				+ "', "
				+ loc.getX()
				+ ", "
				+ loc.getY()
				+ ", "
				+ loc.getZ()
				+ ", "
				+ loc.getYaw()
				+ ", "
				+ loc.getPitch() + ", " + visible + ")");
		sql.closeConnection();
	}

	public void warp(String name, Player player) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);

		out.writeUTF("warp");
		out.writeUTF(name);
		out.writeUTF(player.getName());
		player.sendPluginMessage(plugin, "BungeeSuite", b.toByteArray());
	}

	public void deleteWarp(String name) throws SQLException {
		sql.initialise();
		sql.standardQuery("DELETE FROM BungeeWarps WHERE Name = '" + name + "'");
		sql.closeConnection();
		// plugin.warps.remove(name);
	}

	public HashMap<String, ServerInfo> getServers() throws SQLException {
		HashMap<String, ServerInfo> list = new HashMap<String, ServerInfo>();
		sql.initialise();
		ResultSet res = sql.sqlQuery("SELECT * FROM BungeeServers");
		while (res.next()) {
			String name = res.getString("ServerName");
			list.put(name,
					new ServerInfo(plugin, name, res.getInt("PlayersOnline"),
							res.getInt("MaxPlayers"), res.getString("MOTD"),
							res.getBoolean("Online")));
		}
		res.close();
		sql.closeConnection();
		return list;
	}

	public HashMap<String, String> getSignFormats() throws SQLException {
		HashMap<String, String> list = new HashMap<String, String>();
		sql.initialise();
		ResultSet res = sql.sqlQuery("SELECT * FROM BungeeSignFormats");
		while (res.next()) {
			list.put("ColoredMOTD", res.getBoolean("ColoredMOTD") + "");
			list.put("MOTDOnline", res.getString("MOTDOnline"));
			list.put("MOTDOffline", res.getString("MOTDOffline"));
			list.put("PlayerCountOnline", res.getString("PlayerCountOnline"));
			list.put("PlayerCountOnlineClick",
					res.getString("PlayerCountOnlineClick"));
			list.put("PlayerCountOffline", res.getString("PlayerCountOffline"));
			list.put("PlayerCountOfflineClick",
					res.getString("PlayerCountOfflineClick"));
			list.put("PortalFormatOnline", res.getString("PortalFormatOnline"));
			list.put("PortalFormatOffline",
					res.getString("PortalFormatOffline"));
			list.put("PortalFormatOfflineClick",
					res.getString("PortalFormatOfflineClick"));
		}
		res.close();
		sql.closeConnection();
		return list;
	}

	public int getPlayersOnline(String serverName) throws SQLException {
		sql.initialise();
		ResultSet res = sql
				.sqlQuery("SELECT PlayersOnline FROM BungeeServers WHERE ServerName = '"
						+ serverName + "'");
		int players = 0;
		while (res.next()) {
			players = res.getInt("PlayersOnline");
		}
		sql.closeConnection();
		res.close();

		return players;
	}

	public HashMap<String, ArrayList<BungeeSign>> loadSigns()
			throws SQLException {
		HashMap<String, ArrayList<BungeeSign>> list = new HashMap<String, ArrayList<BungeeSign>>();
		Set<String> servers = plugin.servers.keySet();
		sql.initialise();
			for (String server : servers) {
				ArrayList<BungeeSign> signs = new ArrayList<BungeeSign>();
				ResultSet res = sql
						.sqlQuery("SELECT * FROM BungeeSignLocations WHERE Server = '"
								+ Bukkit.getServerName()
								+ "' AND TargetServer = '"+server+"'");
				while (res.next()) {
					try{
					BungeeSign sign = new BungeeSign(res.getString("Type"),
							(Sign) (new Location(Bukkit.getWorld(res
									.getString("World")), res.getDouble("X"),
									res.getDouble("Y"), res.getDouble("Z")))
									.getBlock().getState(), res.getString("TargetServer"));
					signs.add(sign);
					plugin.AllSigns.add(sign);
					}catch (ClassCastException e){
						sql.standardQuery("DELETE FROM BungeeSignLocations WHERE World = '"+res.getString("World")+"' AND X="+res.getInt("X")+" AND Y="+res.getInt("Y")+" AND Z = "+res.getInt("Z")+"");
					}
				}
				res.close();
				list.put(server, signs);
			}

		sql.closeConnection();
		return list;
	}

	public void createSign(BungeeSign bs) throws SQLException {
		sql.initialise();
		Sign sign = bs.getSign();
		sql.standardQuery("INSERT INTO BungeeSignLocations (Type,Server,TargetServer,World,X,Y,Z) VALUES ('"+bs.getType()+"', '"+Bukkit.getServerName()+"','"+bs.getServer()+"', '"+sign.getWorld().getName()+"',"+sign.getX()+","+sign.getY()+","+sign.getZ()+")");
		sql.closeConnection();
		
	}

}
