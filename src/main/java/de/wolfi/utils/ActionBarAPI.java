package de.wolfi.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ActionBarAPI {

	public static String nmsver;

	static {

		ActionBarAPI.nmsver = Bukkit.getServer().getClass().getPackage().getName();
		ActionBarAPI.nmsver = ActionBarAPI.nmsver.substring(ActionBarAPI.nmsver.lastIndexOf(".") + 1);
	}

	public static void sendActionBar(Player player, String message) {

		try {
		    Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
		    Object p = c1.cast(player);
		    Object ppoc;
		    Class<?> c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
		    Class<?> c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
		    if ((nmsver.equalsIgnoreCase("v1_8_R1") || !nmsver.startsWith("v1_8_")) && !nmsver.startsWith("v1_9_")) {
			Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
			Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
			Method m3 = c2.getDeclaredMethod("a", String.class);
			Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
			ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(cbc, (byte) 2);
		    } else {
			Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
			Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
			Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
			ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
		    }
		    Method m1 = c1.getDeclaredMethod("getHandle");
		    Object h = m1.invoke(p);
		    Field f1 = h.getClass().getDeclaredField("playerConnection");
		    Object pc = f1.get(h);
		    Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
		    m5.invoke(pc, ppoc);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}

	public static void sendActionBar(final Player player, final String message, long duration) {
		ActionBarAPI.sendActionBar(player, message);

		if (duration >= 0) {
			// Sends empty message at the end of the duration. Allows messages
			// shorter than 3 seconds, ensures precision.
			new BukkitRunnable() {
				@Override
				public void run() {
					ActionBarAPI.sendActionBar(player, "");
				}
			}.runTaskLater(UtilRegistry.getPlugin(), duration + 1);
		}

		// Re-sends the messages every 3 seconds so it doesn't go away from the
		// player's screen.
		BukkitTask timer = Bukkit.getScheduler().runTaskTimer(UtilRegistry.getPlugin(), new Runnable() {
			@Override
			public void run() {
				ActionBarAPI.sendActionBar(player, message);
			}
		}, 55, 55);
		Bukkit.getScheduler().runTaskLater(UtilRegistry.getPlugin(), new Runnable() {
			@Override
			public void run() {
				timer.cancel();
			}
		}, duration);
	}

	public static void sendActionBarLater(Player player, String message, long cooldown) {
		Bukkit.getScheduler().runTaskLater(UtilRegistry.getPlugin(), () -> {
			ActionBarAPI.sendActionBar(player, message);
		}, cooldown);
	}

	public static void sendActionBarToAllPlayers(String message) {
		ActionBarAPI.sendActionBarToAllPlayers(message, -1);
	}

	public static void sendActionBarToAllPlayers(String message, long duration) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			ActionBarAPI.sendActionBar(p, message, duration);
		}
	}
}