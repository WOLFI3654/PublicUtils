package de.wolfi.utils;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;

public class RainBomb {
	private FallingBlock sand;
	@SuppressWarnings("unused")
	private Location spawn;
	private int taskid = 0;

	public RainBomb(Location spawn) {
		this.spawn = spawn;

		this.sand = ((CraftWorld) spawn.getWorld()).spawnFallingBlock(spawn, Material.TNT, (byte) 1);
		this.sand.setCustomName("DONOTDROP");
		this.taskid = Bukkit.getScheduler().runTaskTimer(UtilRegistry.getPlugin(), new Runnable() {
			@Override
			public void run() {
				RainBomb.this.sand.getWorld().spigot().playEffect(RainBomb.this.sand.getLocation(), Effect.FLAME, 0, 0,
						0, 0, 0, 10, 10, 1);
				RainBomb.this.sand.getWorld().playSound(RainBomb.this.sand.getLocation(), Sound.BURP, 1, 1);
				Block bellow = RainBomb.this.sand.getLocation().getBlock().getRelative(BlockFace.DOWN)
						.getRelative(BlockFace.DOWN);
				if (!bellow.isEmpty() || !RainBomb.this.sand.isValid()) {
					World w = RainBomb.this.sand.getWorld();
					Location l = RainBomb.this.sand.getLocation();
					RainBomb.this.sand.remove();

					w.playSound(l, Sound.WITHER_HURT, 1, 1);
					w.createExplosion(l.getX(), l.getY(), l.getZ(), 5, false, false);

					Bukkit.getScheduler().cancelTask(RainBomb.this.taskid);
				}
			}
		}, 5, 1).getTaskId();
	}

	public RainBomb(Location spawn, Entity rider) {
		this(spawn);
		this.sand.setPassenger(rider);
	}

}
