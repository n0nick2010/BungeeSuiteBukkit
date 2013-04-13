package com.mcdimensions.BungeeSuiteBukkit.signs;

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

import com.mcdimensions.BungeeSuiteBukkit.BungeeSuiteBukkit;

public class MOTDUpdater extends BukkitRunnable{
	BungeeSuiteBukkit plugin;
	
	public MOTDUpdater(BungeeSuiteBukkit bungeeSuiteBukkit) {
		this.plugin = bungeeSuiteBukkit;
	}

	public void run(){
			String motd = null;
			try {
				Socket socket = new Socket();
				OutputStream outputStream;
				DataOutputStream dataOutputStream;
				InputStream inputStream;
				InputStreamReader inputStreamReader;

				socket.setSoTimeout(1500);

				socket.connect(
						new InetSocketAddress("localhost", Bukkit.getPort()), 1500);

				outputStream = socket.getOutputStream();
				dataOutputStream = new DataOutputStream(outputStream);

				inputStream = socket.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream,
						Charset.forName("UTF-16BE"));

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

				if (string.startsWith("§")) {
					String[] data = string.split("\0");

//					this.setPingVersion(Integer.parseInt(data[0].substring(1)));
//					this.setProtocolVersion(Integer.parseInt(data[1]));
//					this.setGameVersion(data[2]);
					motd=data[3];
//					this.setPlayersOnline(Integer.parseInt(data[4]));
//					this.setMaxPlayers(Integer.parseInt(data[5]));
				} else {
					String[] data = string.split("§");

					motd = data[0];
//					this.setPlayersOnline(Integer.parseInt(data[1]));
//					this.setMaxPlayers(Integer.parseInt(data[2]));
				}

				dataOutputStream.close();
				outputStream.close();

				inputStreamReader.close();
				inputStream.close();

				socket.close();
			} catch (SocketException exception) {

			} catch (IOException exception) {

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
