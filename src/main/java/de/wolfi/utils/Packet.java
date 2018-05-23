package de.wolfi.utils;

import java.lang.reflect.Field;

import org.bukkit.entity.Player;

public class Packet {

	
	private final Object internPacket;
	
	public Packet(String name){
		internPacket = Reflection.newInstance(Reflection.getConstructor(Reflection.getNMSClass(name)));
	}
	
	
	public void set(String name, Object value){
		Field f = Reflection.getField(internPacket.getClass(), name);
		boolean bck = f.isAccessible();
		f.setAccessible(true);
		try {
			f.set(internPacket, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		f.setAccessible(bck);
	}
	
	
	public void send(Player p){
		PlayerListInjection.sendPacket(p, internPacket);
	}


	public void broadcast() {
		PlayerListInjection.broadcastPacket(internPacket);
		
	}
}
