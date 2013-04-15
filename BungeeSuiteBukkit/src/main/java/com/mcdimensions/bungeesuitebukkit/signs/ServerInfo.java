package com.mcdimensions.bungeesuitebukkit.signs;

import java.sql.SQLException;

import com.mcdimensions.bungeesuitebukkit.BungeeSuiteBukkit;

public class ServerInfo {
	String serverName;
	int playersOnline;
	int maxPlayers;
	String MOTD;
	boolean status;
	BungeeSuiteBukkit plugin;
	
	public ServerInfo(BungeeSuiteBukkit bungeeSuite, String serverName, int playersOnline, int maxPlayers, String MOTD, boolean status){
		this.plugin = bungeeSuite;
		this.serverName = serverName;
		this.playersOnline = playersOnline;
		this.maxPlayers = maxPlayers;
		this.MOTD = MOTD;
		this.status = status;
	}
	public boolean updateMOTDInfo() throws SQLException{
		String newMOTD = plugin.utils.getMOTD(serverName);
		if(newMOTD == null){
			return false;
		}
		if(newMOTD.equalsIgnoreCase(MOTD)){
			return false;
		}else{
			MOTD = newMOTD;
			return true;
		}
	}
	public boolean updatePlayerCount() throws SQLException{
		int newPlayersOnline = plugin.utils.getOnlinePlayerCount(serverName);
		if(newPlayersOnline == playersOnline){
			return false;
		}else{
			playersOnline = newPlayersOnline;
			return true;
		}
	}
	public boolean updateStatus() throws SQLException{
		boolean serverStatus =  plugin.utils.ServerStatus(serverName);
		if(serverStatus==status){
			return false;
		}else{
			this.status = serverStatus;
			return true;
		}
	}
}
