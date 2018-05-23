package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MusicPlayerRemoveEvent extends Event {
	
	
	private Player p;
	
	private static final HandlerList handlers = new HandlerList();
	
	public MusicPlayerRemoveEvent(Player p) {
		this.p = p;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
