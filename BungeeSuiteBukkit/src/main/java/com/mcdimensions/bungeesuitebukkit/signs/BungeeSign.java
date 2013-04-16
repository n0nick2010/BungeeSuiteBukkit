package com.mcdimensions.bungeesuitebukkit.signs;

import org.bukkit.block.Sign;

public class BungeeSign {
	
	private static final int MAX_LINES = 4;
	
	String Type;
	Sign sign;
	String targetServer;
	
	public BungeeSign(String type, Sign sign, String targetServer){
		this.Type = type;
		this.sign = sign;
		this.targetServer = targetServer;
	}
	public void setLines(String[] lines){
		for (int i = 0; i < (lines.length > MAX_LINES ? MAX_LINES : lines.length); i++)
			sign.setLine(i, lines[i]);

		sign.update(true);
	}
	
	public boolean isType(String type){
		if(Type.equalsIgnoreCase(type)){
			return true;
		}else{
			return false;
		}
	}
	public String getType(){
		return Type;
	}
	public String getServer(){
		return targetServer;
	}
	public Sign getSign(){
		return sign;
	}
}
