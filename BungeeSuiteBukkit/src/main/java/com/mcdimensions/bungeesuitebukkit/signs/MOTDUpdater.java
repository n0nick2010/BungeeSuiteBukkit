package com.mcdimensions.bungeesuitebukkit.signs;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.mcdimensions.bungeesuitebukkit.BungeeSuiteBukkit;

public class MOTDUpdater extends BukkitRunnable{
	BungeeSuiteBukkit plugin;
	private static final String COLOUR_CHAR = "\u00A7";
	
	public MOTDUpdater(BungeeSuiteBukkit bungeeSuiteBukkit) {
		this.plugin = bungeeSuiteBukkit;
	}

	public void run(){
			String motd = null;
			try (Socket socket = new Socket()) {

				socket.setSoTimeout(1500);

				socket.connect(
						new InetSocketAddress("localhost", Bukkit.getPort()), 1500);

				try (OutputStream outputStream = socket.getOutputStream();
						DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
						InputStream inputStream = socket.getInputStream();
						InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
								Charset.forName("UTF-16BE"))) {
					dataOutputStream.write(new byte[] { (byte) 0xFE, (byte) 0x01 });
	
					int packetId = inputStream.read();
	
					if (packetId == -1) {
						throw new IOException("Premature end of stream.");
					}
	
					if (packetId != 0xFF) {
						throw new IOException("Invalid packet ID (" + packetId + ").");
					}
	
					int length = inputStreamReader.read();
	
					if (length == -1) {
						throw new IOException("Premature end of stream.");
					}
	
					if (length == 0) {
						throw new IOException("Invalid string length.");
					}
	
					char[] chars = new char[length];
	
					if (inputStreamReader.read(chars, 0, length) != length) {
						throw new IOException("Premature end of stream.");
					}
	
					String string = new String(chars);
	
					if (string.startsWith(COLOUR_CHAR)) {
						String[] data = string.split("\0");
						motd=data[3];
					} else {
						String[] data = string.split(COLOUR_CHAR);
	
						motd = data[0];
					}
				}
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				plugin.utils.setMOTD();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			plugin.setMOTD(motd);
		}
}
