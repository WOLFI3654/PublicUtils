package de.wolfi.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SimpleLocationManager {

	public static void teleportPlayer(Player player, String pose){
		player.teleport(fromString(pose));
	}
	
	public static Location fromString(String loc) {
		if(loc == null){
			return new Location(Bukkit.getWorlds().get(0), 0, 3, 0);
		}
		if(loc.isEmpty()){
			return new Location(Bukkit.getWorlds().get(0), 0, 3, 0);
		}
		String[] data = loc.split(";");
		if(data.length != 6){
			
				return new Location(Bukkit.getWorlds().get(0), 0, 3, 0);
			
		}
		World world = Bukkit.getWorld(data[0]);
		double x = Double.valueOf(data[1]);
		double y = Double.valueOf(data[2]);
		double z = Double.valueOf(data[3]);
		float yaw = Float.valueOf(data[4]);
		float pitch = Float.valueOf(data[5]);
		
		return new Location(world, x, y, z, yaw, pitch);
	}
	
	public static String toString(Location loc){
		return loc.getWorld().getName()+";"+loc.getX()+";"+loc.getY()+";"+loc.getZ()+";"+loc.getYaw()+";"+loc.getPitch();		
	}
	
}
