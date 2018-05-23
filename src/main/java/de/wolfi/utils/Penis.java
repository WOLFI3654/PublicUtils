package de.wolfi.utils;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.FallingBlock;
import org.bukkit.material.Wool;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class Penis {
	private Location loc;

	public Penis(Location loc) {

		this.loc = loc;
		this.spawn();
	}

	public void spawn() {
		this.wool(this.loc.getBlock());
		this.loc.getBlock().setMetadata("penis", new FixedMetadataValue(UtilRegistry.getPlugin(), true));
		this.wool(this.loc.getBlock().getRelative(BlockFace.EAST));
		this.wool(this.loc.getBlock().getRelative(BlockFace.WEST));
		this.wool(this.loc.getBlock().getRelative(BlockFace.UP));
		this.wool(this.loc.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP));
	}

	public void sperma() {
		Block start = this.loc.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP);
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 20; i++) {
					Bukkit.getScheduler().runTask(UtilRegistry.getPlugin(), new Runnable() {
						@Override
						public void run() {
							FallingBlock b = ((CraftWorld) start.getWorld()).spawnFallingBlock(start.getLocation(),
									Material.WEB, (byte) 0);
							b.setCustomName("DONOTDROP");
							b.setVelocity(new Vector(0, 2, 3));
						}
					});

					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}

	public void wool(Block block) {
		block.setType(Material.WOOL);
		Wool w = (Wool) block.getState().getData();
		w.setColor(DyeColor.PINK);
		block.getState().setData(w);

	}

}
