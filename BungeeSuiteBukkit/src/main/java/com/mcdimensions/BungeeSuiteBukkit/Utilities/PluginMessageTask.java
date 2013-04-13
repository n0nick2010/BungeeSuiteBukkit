package com.mcdimensions.BungeeSuiteBukkit.Utilities;

import java.io.ByteArrayOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mcdimensions.BungeeSuiteBukkit.BungeeSuiteBukkit;

public class PluginMessageTask extends BukkitRunnable {
    
    private final BungeeSuiteBukkit plugin;
    private ByteArrayOutputStream bytes;
    
    public PluginMessageTask(BungeeSuiteBukkit plugin, Player player, ByteArrayOutputStream bytes) {
        this.plugin = plugin;
        this.bytes = bytes;
    }

    public void run() {
        Bukkit.getServer().sendPluginMessage(this.plugin, "BungeeSuite", this.bytes.toByteArray());
    }

}