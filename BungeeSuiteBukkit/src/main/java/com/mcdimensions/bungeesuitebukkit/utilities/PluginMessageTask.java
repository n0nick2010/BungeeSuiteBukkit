package com.mcdimensions.bungeesuitebukkit.utilities;

import java.io.ByteArrayOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mcdimensions.bungeesuitebukkit.BungeeSuiteBukkit;

public class PluginMessageTask extends BukkitRunnable {
    
    private final BungeeSuiteBukkit plugin;
    private final ByteArrayOutputStream bytes;
    
    public PluginMessageTask(BungeeSuiteBukkit plugin, Player player, ByteArrayOutputStream bytes) {
        this.plugin = plugin;
        this.bytes = bytes;
    }

    public void run() {
        Bukkit.getServer().sendPluginMessage(plugin, BungeeSuiteBukkit.OUTGOING_PLUGIN_CHANNEL, bytes.toByteArray());
    }

}