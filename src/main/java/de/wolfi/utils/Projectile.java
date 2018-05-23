package de.wolfi.utils;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Projectile {

	private abstract class Runnable implements java.lang.Runnable {

		private Projectile projectile;

		public Runnable(Projectile projectile) {
			this.projectile = projectile;
		}

		public Projectile getProjectile() {
			return this.projectile;
		}

	}

	private ArmorStand holder;
	private Location location;
	private Material material;

	public Projectile(Material material, Location loc) {

		this.location = loc;
		this.material = material;
		this.holder = (ArmorStand) this.location.getWorld().spawnEntity(this.location, EntityType.ARMOR_STAND);
		this.holder.getLocation().setDirection(loc.getDirection());
		this.holder.setVisible(false);
		this.holder.setGravity(false);
		this.holder.setArms(true);
		this.holder.setItemInHand(new ItemStack(material));

	}

	public Location getLocation() {
		return this.location;
	}

	public Material getMaterial() {
		return this.material;
	}

	public void hit(LivingEntity entity) {
		if (entity instanceof Player) {
			if (((Player) entity).getGameMode() == GameMode.CREATIVE
					|| ((Player) entity).getGameMode() == GameMode.SPECTATOR) {
				return;
			}
		}
		if (entity.getHealth() == 0.0) {
			return;
		}
		if (entity.getHealth() < 2) {
			entity.setHealth(0);
			entity.playEffect(EntityEffect.HURT);
			return;
		}
		entity.setHealth(entity.getHealth() - 2);
		entity.playEffect(EntityEffect.HURT);
	}

	public void shoot(final Vector target) {
		Thread t = new Thread(new Runnable(this) {
			@Override
			public void run() {
				Bukkit.getScheduler().runTask(UtilRegistry.getPlugin(), new Runnable(this.getProjectile()) {
					@Override
					public void run() {
						Projectile.this.holder.setGravity(true);
						Projectile.this.holder.setVelocity(target);
					}
				});
				try {
					Thread.sleep(50);
				} catch (InterruptedException e2) {

					e2.printStackTrace();
				}

				while (this.getProjectile().holder.isValid()) {
					if (this.getProjectile().holder.isOnGround()) {
						Bukkit.getScheduler().runTask(UtilRegistry.getPlugin(), new Runnable(this.getProjectile()) {
							@Override
							public void run() {
								this.getProjectile().holder.getWorld()
										.strikeLightning(this.getProjectile().holder.getLocation());
								this.getProjectile().holder.remove();
							}
						});
					}
					try {
						Thread.sleep(50);
						for (Entity e : this.getProjectile().holder.getNearbyEntities(1.0D, 1.0D, 1.0D)) {
							if (e instanceof LivingEntity) {
								if (e.getType() != EntityType.ARMOR_STAND) {
									Projectile.this.hit((LivingEntity) e);
								}
							}
						}

					} catch (InterruptedException e1) {

						e1.printStackTrace();
					}

				}

				Bukkit.getScheduler().runTask(UtilRegistry.getPlugin(), new Runnable(this.getProjectile()) {
					@Override
					public void run() {
						this.getProjectile().holder.getWorld()
								.strikeLightning(this.getProjectile().holder.getLocation());
						this.getProjectile().holder.remove();
					}
				});
			}
		});
		t.start();

	}
}
