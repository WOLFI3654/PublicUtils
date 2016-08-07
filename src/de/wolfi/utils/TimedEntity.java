package de.wolfi.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class TimedEntity {
	Entity en;
	private Location loc;
	private ArrayList<Runable<TimedEntity>> tasks;
	private int time;

	public TimedEntity(EntityType type, Location loc, int time) {
		this.loc = loc;
		this.time = time;
		this.tasks = new ArrayList<>();
		this.en = loc.getWorld().spawnEntity(loc, type);
		if (time > 0)
			Bukkit.getScheduler().runTaskLater(UtilRegistry.getPlugin(), new Runnable() {
				@Override
				public void run() {
					if (TimedEntity.this.en.isValid()) {
						TimedEntity.this.en.eject();
						TimedEntity.this.en.remove();
						for (Runable<TimedEntity> run : TimedEntity.this.tasks) {
							run.run();
						}
					}
				}
			}, time);
	}

	private Entity a() {
		return this.en;
	}

	public TimedEntity addEndScheduler(Runable<TimedEntity> task) {
		task.a(this);
		this.tasks.add(task);
		return this;
	}

	public TimedEntity angry(boolean angry) {
		if (this.en instanceof Wolf) {
			((Wolf) this.en).setAngry(angry);
		}
		return this;
	}

	public TimedEntity dye(DyeColor color) {
		if (this.en instanceof Colorable) {
			((Colorable) this.en).setColor(color);
		}
		return this;
	}

	public Location getLoc() {
		return this.loc;
	}

	public int getTime() {
		return this.time;
	}

	public TimedEntity helmet(ItemStack item) {
		if (this.en instanceof Creature) {
			((Creature) this.en).getEquipment().setHelmet(item);
		}
		return this;
	}

	public TimedEntity name(String name) {
		this.en.setCustomName(name);
		this.en.setCustomNameVisible(true);
		return this;
	}

	public TimedEntity nbt(String entry, int value) {
		CraftEntity nmsEnt = (CraftEntity) this.en;
		NBTTagCompound tag = nmsEnt.getHandle().getNBTTag();

		if (tag == null) {
			tag = new NBTTagCompound();
		}

		nmsEnt.getHandle().c(tag);

		tag.setInt(entry, value);
		nmsEnt.getHandle().f(tag);
		return this;
	}

	public TimedEntity nbt(String entry, String value) {
		CraftEntity nmsEnt = (CraftEntity) this.en;
		NBTTagCompound tag = nmsEnt.getHandle().getNBTTag();

		if (tag == null) {
			tag = new NBTTagCompound();
		}

		nmsEnt.getHandle().c(tag);
		tag.setString(entry, value);
		nmsEnt.getHandle().f(tag);
		return this;
	}

	public List<Entity> nearby(double fix) {
		return this.en.getNearbyEntities(fix, fix, fix);
	}

	public TimedEntity passenger(Entity pass) {
		this.en.setPassenger(pass);
		return this;
	}

	public TimedEntity passenger(TimedEntity pass) {
		this.en.setPassenger(pass.a());
		return this;
	}

	public TimedEntity potion(PotionEffect effect) {
		if (this.en instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) this.en;
			entity.addPotionEffect(effect);
		}
		return this;
	}

	public TimedEntity silent(boolean silent) {
		this.nbt("Silent", silent ? 1 : 0);
		return this;
	}

	public TimedEntity target(Entity target) {

		if (this.en instanceof Creature && target instanceof LivingEntity) {
			((Creature) this.en).setTarget((LivingEntity) target);
		}
		return this;
	}

	public TimedEntity vector(Vector v) {
		this.en.setVelocity(v);
		return this;
	}

}
