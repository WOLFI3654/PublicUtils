package de.wolfi.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class NMSUtils {

	
	public static final Object getNMSBlock(String name){

		return Reflection.invokeMethod(Reflection.getMethod(Reflection.getNMSClass("Block"), "getByName", String.class),null,name);
	}
	
	public static final Object getBlockPosition(Location loc){
		return Reflection.newInstance(Reflection.getConstructor(Reflection.getNMSClass("BlockPosition"), double.class,double.class,double.class), loc.getX(),loc.getY(),loc.getZ());
	}
	
	public static final Object getCraftEntity(Entity e){
		return Reflection.invokeMethod(Reflection.getMethod(e.getClass(), "getHandle"), e);
	}
	
}
