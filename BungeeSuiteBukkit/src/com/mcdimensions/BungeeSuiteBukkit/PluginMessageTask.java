package com.mcdimensions.BungeeSuiteBukkit;

import java.io.ByteArrayOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PluginMessageTask extends BukkitRunnable {
    
    private final BungeeSuiteBukkit plugin;
    private final Player player;
    private ByteArrayOutputStream bytes;
    
    public PluginMessageTask(BungeeSuiteBukkit plugin, Player player, ByteArrayOutputStream bytes) {
        this.plugin = plugin;
        this.player = player;
        this.bytes = bytes;
    }

    public void run() {
        Bukkit.getServer().sendPluginMessage(this.plugin, "BungeeSuite", this.bytes.toByteArray());
    }

}