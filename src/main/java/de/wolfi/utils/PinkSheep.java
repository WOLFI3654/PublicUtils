package de.wolfi.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PinkSheep implements Listener {

	private PinkSheep i;
	private Sheep sheep;

	public PinkSheep(LivingEntity target, int id) {
		this.i = this;
		Bukkit.getPluginManager().registerEvents(this, UtilRegistry.getPlugin());
		this.sheep = (Sheep) target.getWorld().spawnEntity(target.getLocation().clone().add(0, 500, 0),
				EntityType.SHEEP);
		this.sheep.setColor(DyeColor.PINK);
		this.sheep.teleport(target);
		this.sheep.setFireTicks(20 * 10);
		this.sheep.setPassenger(target);
		this.sheep.setCustomName(ChatColor.LIGHT_PURPLE + "Pink Sheep " + id);

		Bukkit.getScheduler().runTaskLater(UtilRegistry.getPlugin(), new Runnable() {
			@Override
			public void run() {
				HandlerList.unregisterAll(PinkSheep.this.i);

				PinkSheep.this.sheep.getWorld().strikeLightningEffect(PinkSheep.this.sheep.getLocation());
				PinkSheep.this.sheep.remove();

			}
		}, 20 * 60);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity().getCustomName() != null) {
			if (e.getEntity().getCustomName().contains("Pink Sheep")) {
				e.setCancelled(true);
			}
		}
	}
}
