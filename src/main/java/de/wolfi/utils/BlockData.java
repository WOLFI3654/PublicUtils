package de.wolfi.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class BlockData {

	private byte data;
	private Location loc;
	private Material m;

	public BlockData(String[] split) {
		this.loc = new Location(Bukkit.getWorld(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]),
				Integer.valueOf(split[3]));
		this.m = Material.valueOf(split[4]);
		this.data = Byte.parseByte(split[5]);
	}

	public byte getData() {
		return this.data;
	}

	public Location getLoc() {
		return this.loc;
	}

	public Material getM() {
		return this.m;
	}

	public void set() {
		Bukkit.getScheduler().runTask(UtilRegistry.getPlugin(), new Runnable() {
			@Override
			@SuppressWarnings("deprecation")
			public void run() {

				Block b = BlockData.this.loc.getBlock();
				for (Entity e : b.getWorld().getNearbyEntities(b.getLocation(), 1, 5, 1)) {
					e.setVelocity(new Vector(0, 2, 0));
				}
				b.setType(BlockData.this.m,false);
				b.setData(BlockData.this.data,false);
				b.getWorld().playSound(b.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);

			}
		});
	}
}