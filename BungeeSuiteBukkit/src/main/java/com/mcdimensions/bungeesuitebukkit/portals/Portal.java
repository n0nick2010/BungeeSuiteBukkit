/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcdimensions.bungeesuitebukkit.portals;

import org.bukkit.Location;

/**
 *
 * @author ANNA
 */
public class Portal {
	private String name;
	private String toServer;
	private String toWarp;
	private Region portal;
	private FillType fillType;
	private boolean active;

	public Portal(String name, String toServer, Region portal, FillType fillType) {
		this.name = name;
		this.toServer = toServer;
		this.portal = portal;
		this.fillType = fillType;
		this.active = true;
		fillBlocks();
	}
	public Portal(String name, String toServer, String toWarp,Region portal, FillType fillType) {
		this.name = name;
		this.toServer = toServer;
		this.toWarp = toWarp;
		this.portal = portal;
		this.fillType = fillType;
		this.active = true;
		fillBlocks();
	}

	public FillType getFillType() {
		return fillType;
	}


	public Region getPortalArea() {
		return portal;
	}

	public String getTag() {
		return name;
	}

	public boolean isActive() {
		return active;
	}

//	public boolean teleportPlayer(Player p)
//	{
//		return true;
//	}

	public boolean containsLocation(Location l, double offset)
	{
			if (portal.isIn(l, offset)){
				return true;
			}
		return false;
	}
	

	public boolean isIn(Location l) {
		return portal.isIn(l);
	}

	public void setActive(boolean active) {
		this.active = active;
		if (active)
		{
			this.fillBlocks();
		}
		else
		{
			this.defillBlocks();
		}
	}

	public void setFillType(FillType fillType) {
		if (this.isActive())
		{
			this.defillBlocks();
			this.fillType = fillType;
			this.fillBlocks();
		}
	}

	public void beforeDelete()
	{
		this.defillBlocks();
	}

	public void defillBlocks()
	{
			this.fillType.defillBlocks(portal.getBlocks());
	}

	public void fillBlocks()
	{
			this.fillType.fillBlocks(portal.getBlocks());
	}

	public void setFromPoints(Region fromPoints) {
		this.portal = fromPoints;
	}
	public String getToServer() {
		return toServer;
	}
	public boolean hasWarp() {
		return toWarp!=null;
	}
	
	public String getWarp() {
		return toWarp;
	}

	public void setToServer(String toServer) {
		this.toServer = toServer;
	}
	public void setToWarp(String toWarp) {
		this.toWarp = toWarp;
	}
}