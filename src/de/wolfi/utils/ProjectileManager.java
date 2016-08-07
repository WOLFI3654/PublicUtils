package de.wolfi.utils;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class ProjectileManager {

	public static Vector createVector(Location target, Location fix) {

		double xVec = (target.getX() - fix.getX()) * 0.13;
		double yVec = (target.getY() - fix.getY()) * 0.009;
		double zVec = (target.getZ() - fix.getZ()) * 0.13;

		return new Vector(xVec, yVec, zVec);

	}

	private ArrayList<ArrayList<Projectile>> projectiles;

	public ProjectileManager() {
		this.projectiles = new ArrayList<>();
	}

	public int createProjectiles(Location fixLoc) {
		ArrayList<Projectile> projectile = new ArrayList<>();
		double degrees = fixLoc.getYaw();

		if (degrees > 315 || degrees < 45 || degrees < 225 && degrees > 135) {
			projectile.add(new Projectile(Material.GOLD_SPADE, fixLoc.clone().add(0, 1.0, 0)));
			projectile.add(new Projectile(Material.GOLD_SWORD, fixLoc.clone().add(1, 1.0, 0)));
			projectile.add(new Projectile(Material.GOLD_PICKAXE, fixLoc.clone().add(-1, 1.0, 0)));
			projectile.add(new Projectile(Material.GOLD_SWORD, fixLoc.clone().add(2, 1.0, 0)));
			projectile.add(new Projectile(Material.GOLD_PICKAXE, fixLoc.clone().add(-2, 1.0, 0)));
			projectile.add(new Projectile(Material.GOLD_SPADE, fixLoc.clone().add(-3, 1.0, 0)));
			projectile.add(new Projectile(Material.GOLD_SWORD, fixLoc.clone().add(3, 1.0, 0)));
			projectile.add(new Projectile(Material.GOLD_HOE, fixLoc.clone().add(0, 2.0, 0)));
			projectile.add(new Projectile(Material.GOLD_AXE, fixLoc.clone().add(1, 2.0, 0)));
			projectile.add(new Projectile(Material.COOKIE, fixLoc.clone().add(-1, 2.0, 0)));
			projectile.add(new Projectile(Material.GOLD_HOE, fixLoc.clone().add(2, 2.0, 0)));
			projectile.add(new Projectile(Material.GOLD_AXE, fixLoc.clone().add(-2, 2.0, 0)));
			projectile.add(new Projectile(Material.GOLD_SPADE, fixLoc.clone().add(0, 3.0, 0)));
			projectile.add(new Projectile(Material.GOLD_SWORD, fixLoc.clone().add(1, 3.0, 0)));
			projectile.add(new Projectile(Material.GOLD_PICKAXE, fixLoc.clone().add(-1, 3.0, 0)));

			projectile.add(new Projectile(Material.GOLD_INGOT, fixLoc.clone().add(0, 4.0, 0)));

		} else {
			projectile.add(new Projectile(Material.GOLD_SPADE, fixLoc.clone().add(0, 1.0, 0)));
			projectile.add(new Projectile(Material.GOLD_SWORD, fixLoc.clone().add(0, 1.0, 1)));
			projectile.add(new Projectile(Material.GOLD_PICKAXE, fixLoc.clone().add(0, 1.0, -1)));
			projectile.add(new Projectile(Material.GOLD_SPADE, fixLoc.clone().add(0, 1.0, 2)));
			projectile.add(new Projectile(Material.GOLD_SWORD, fixLoc.clone().add(0, 1.0, -2)));
			projectile.add(new Projectile(Material.GOLD_HOE, fixLoc.clone().add(0, 1.0, 3)));
			projectile.add(new Projectile(Material.GOLD_AXE, fixLoc.clone().add(0, 1.0, -3)));
			projectile.add(new Projectile(Material.COOKIE, fixLoc.clone().add(0, 2.0, 0)));
			projectile.add(new Projectile(Material.GOLD_HOE, fixLoc.clone().add(0, 2.0, -1)));
			projectile.add(new Projectile(Material.GOLD_AXE, fixLoc.clone().add(0, 2.0, 1)));
			projectile.add(new Projectile(Material.GOLD_SPADE, fixLoc.clone().add(0, 2.0, -2)));
			projectile.add(new Projectile(Material.GOLD_SWORD, fixLoc.clone().add(0, 2.0, 2)));
			projectile.add(new Projectile(Material.GOLD_PICKAXE, fixLoc.clone().add(0, 3.0, 0)));
			projectile.add(new Projectile(Material.GOLD_HOE, fixLoc.clone().add(0, 3.0, -1)));
			projectile.add(new Projectile(Material.GOLD_AXE, fixLoc.clone().add(0, 3.0, 1)));

			projectile.add(new Projectile(Material.GOLD_INGOT, fixLoc.clone().add(0, 4.0, 0)));

		}

		this.projectiles.add(projectile);
		return this.projectiles.indexOf(projectile);
	}

	public void shoot(int index, Location target) {

		for (Projectile projectile : this.projectiles.get(index)) {
			projectile.shoot(ProjectileManager.createVector(target, projectile.getLocation()));
		}

		this.projectiles.remove(index);
	}

}
