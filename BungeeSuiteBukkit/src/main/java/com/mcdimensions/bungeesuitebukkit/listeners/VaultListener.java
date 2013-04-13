package com.mcdimensions.bungeesuitebukkit.listeners;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mcdimensions.bungeesuitebukkit.BungeeSuiteBukkit;
import com.mcdimensions.bungeesuitebukkit.utilities.PluginMessageTask;

public class VaultListener implements Listener {
	BungeeSuiteBukkit plugin;

	public VaultListener(BungeeSuiteBukkit bungeeSuiteBukkit) {
		this.plugin = bungeeSuiteBukkit;
	}

	@EventHandler
	public void login(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String prefix = "";
		String suffix = "";
		Chat chat=BungeeSuiteBukkit.CHAT;
		String group =chat.getPrimaryGroup(player);
		if(chat.getPlayerPrefix(player)!=null){
			prefix=chat.getPlayerPrefix(player);
		}else if(chat.getGroupPrefix(player.getWorld(), group)!=null){
			prefix =chat.getGroupPrefix(player.getWorld(),group);
		}
		if(chat.getPlayerSuffix(player)!=null){
			suffix = chat.getPlayerSuffix(player);
		}else if(chat.getGroupSuffix(player.getWorld(), group)!=null){
			suffix = chat.getGroupSuffix(player.getWorld(), group);
		}
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("PrefixSuffix");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.writeUTF(player.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.writeUTF(prefix);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			out.writeUTF(suffix);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new PluginMessageTask(this.plugin, event.getPlayer(), b).runTaskLater(this.plugin, 5);
	}

}
