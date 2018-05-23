package de.wolfi.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;


public class LocationManager {
	
	public ArrayList<Entity> getNearbyEntities(Location l, double radius) {
		World w = l.getWorld();
		
		List<Entity> en = w.getEntities();
		ArrayList<Entity> enFinal = new ArrayList<Entity>();
		
		for(Entity entity : en) {
			if(l.getWorld().getName().equals(entity.getLocation().getWorld().getName()) && l.distance(entity.getLocation()) <= radius) {
				enFinal.add(entity);
			}
		}
		
		return enFinal;
	}
	
	public boolean areBlockLocationsIdentical(Location a, Location b) {
		return a.getBlockX() == b.getBlockX() && a.getBlockY() == b.getBlockY() && a.getBlockZ() == b.getBlockZ() && a.getBlock().getWorld().getName().equals(b.getWorld().getName());
	}
	
	public boolean areWorldsSame(Location a, Location b) {
		return a.getWorld().getName().equals(b.getWorld().getName());
	}
	
	public Location getBlockMiddle(Location l) {
		int x = l.getBlockX();
		int z = l.getBlockZ();
		
		Location middle = new Location(
				l.getWorld(),
				x + (x > 0 ? 0.5 : -0.5),
				l.getY(),
				z + (z > 0 ? 0.5 : -0.5)
			);
		
		middle.setPitch(l.getPitch());
		middle.setYaw(l.getYaw());
		
		return middle;
	}
	
	public Location loadLocation(SettingsManager sm, String get) {
		 try{
			 Location l = new Location(Bukkit.getWorld((String) sm.get(get + "WORLD")),(int) sm.get(get + "X"),(int) sm.get(get + "Y"), (int)sm.get(get + "Z"));
			 return l;
		 } catch (Exception ex) {
			 ex.printStackTrace();
			 return null;
		 }
	 }
	 
	 public boolean saveLocation(SettingsManager sm, String set, Location l) {
		 try{
			 sm.set(set + "WORLD", l.getWorld().getName());
			 sm.set(set + "Y", l.getBlockY());
			 sm.set(set + "X", l.getBlockX());
			 sm.set(set + "Z", l.getBlockZ());
			 sm.save();
			 return true;
		 } catch (Exception ex) {
			 ex.printStackTrace();
			 return false;
		 }
	 }
	 
	 public void saveLocation(Location location, ConfigurationSection section) {
			section.set("world", location.getWorld().getName());
			section.set("x", location.getX());
			section.set("y", location.getY());
			section.set("z", location.getZ());
			section.set("pitch", location.getPitch());
			section.set("yaw", location.getYaw());
	}
		
	 public Location loadLocation(ConfigurationSection section) {
			return new Location(
					Bukkit.getServer().getWorld(section.getString("world")),
					section.getDouble("x"),
					section.getDouble("y"),
					section.getDouble("z"),
					(float) section.getDouble("pitch"),
					(float) section.getDouble("yaw")
			);
	 }
	 
	 public Location getBoundsMiddle(CuboidSelection bounds){
			Location max = bounds.getMaximumPoint();
			Location min = bounds.getMinimumPoint();
			
			double x = (max.getX() + min.getX()) / 2;
			double z = (max.getZ() + min.getZ()) / 2;
			double y = (max.getY() + min.getY()) / 2;
			
			return new Location(max.getWorld(), x, y, z);
	}
}
