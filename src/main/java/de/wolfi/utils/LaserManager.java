package de.wolfi.utils;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

@Deprecated
public class LaserManager {

	private static HashMap<String, Object[]> lasers = new HashMap<>();

	public static void clear() {
		for (String name : LaserManager.lasers.keySet()) {
			LaserManager.removeLaser(name);
		}
	}

	public static Laser createLaser(String name, Location from) {
		if (LaserManager.lasers.containsKey(name))
			LaserManager.removeLaser(name);
		ArmorStand a = (ArmorStand) from.getWorld().spawnEntity(from.clone().add(0, 200, 0), EntityType.ARMOR_STAND);
		a.setVisible(false);
		a.setGravity(false);
		a.setCustomName(name + "-Laser-Marker");
		Laser l = new Laser(from);

		LaserManager.lasers.put(name, new Object[] { l, a });

		return l;
	}

	public static void removeLaser(String name) {
		if (LaserManager.lasers.containsKey(name)) {
			Object[] os = LaserManager.lasers.get(name);
			Laser l = (Laser) os[0];
			ArmorStand a = (ArmorStand) os[1];
			l.despawn();
			a.remove();
			a.setHealth(0);
			LaserManager.lasers.remove(name);
		}
	}

	public static Laser spawnLaser(String name, Location from, Location to) {
		if (LaserManager.lasers.containsKey(name))
			LaserManager.removeLaser(name);
		ArmorStand a = (ArmorStand) to.getWorld().spawnEntity(to, EntityType.ARMOR_STAND);
		a.setVisible(false);
		a.setGravity(false);
		a.setCustomName(name + "-Laser-Marker");
		Laser l = new Laser(from);
		l.setTarget(a);
		LaserManager.lasers.put(name, new Object[] { l, a });

		return l;
	}

}
