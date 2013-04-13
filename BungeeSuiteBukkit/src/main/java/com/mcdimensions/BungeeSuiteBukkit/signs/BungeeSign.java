package com.mcdimensions.BungeeSuiteBukkit.signs;

import org.bukkit.block.Sign;

public class BungeeSign {
	String Type;
	Sign sign;
	String targetServer;
	
	public BungeeSign(String type, Sign sign, String targetServer){
		this.Type = type;
		this.sign = sign;
		this.targetServer = targetServer;
	}
	public void setLines(String one, String two,String three,String four){
		sign.setLine(0, one);
		sign.setLine(1, two);
		sign.setLine(2, three);
		sign.setLine(3, four);
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
