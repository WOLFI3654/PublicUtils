package de.wolfi.utils;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class ParticleAPI {

	public static class Particle {
		Enum<?> en;

		private Particle(Enum<?> p) {
			this.en = p;
		}

		private Particle(String name) {
			this.en = ParticleAPI.getWitch(name);
		}

		public void play(Location l, float offsetX, float offsetY, float offsetZ, float speed, int count) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getWorld().equals(l.getWorld())) {
					try {
						this.play(p, l, offsetX, offsetY, offsetZ, speed, count);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		public void play(Player player, Location l, float offsetX, float offsetY, float offsetZ,
				float speed, int count) throws Exception {

			Object cp = player.getClass().getMethod("getHandle").invoke(player);
			Object pc = cp.getClass().getField("playerConnection").get(cp);
			Class<?> packet = Reflection.getNMSClass("PacketPlayOutWorldParticles");
			Constructor<?> pac = null;
			for (Constructor<?> c : packet.getConstructors()) {
				if (c.getParameterTypes().length > 1) {
					pac = c;
				}
			}

			pc.getClass().getMethod("sendPacket", Reflection.getNMSClass("Packet")).invoke(pc,
					pac.newInstance(en, true, (float) l.getX(), (float) l.getY(), (float) l.getZ(), offsetX, offsetY,
							offsetZ, speed, count, new int[0]));

			// PacketPlayOutWorldParticles packet = new
			// PacketPlayOutWorldParticles(type, true, (float)l.getX(),
			// (float)l.getY(), (float)l.getZ(), offsetX, offsetY, offsetZ,
			// speed, count, 0);
			// p.getHandle().playerConnection.sendPacket(packet);
		}
	}

	private static HashMap<String, Particle> byName = new HashMap<>();

	@SuppressWarnings("unchecked")
	private static Class<? extends Enum<?>> clazz = (Class<? extends Enum<?>>) Reflection.getNMSClass("EnumParticle");

	static {
		for (Enum<?> e : ParticleAPI.clazz.getEnumConstants()) {
			ParticleAPI.registerParticle(e.toString(), new Particle(e));
		}
	}

	public static Particle getParticle(String name) {
		return ParticleAPI.byName.get(name);
	}

	private static Enum<?> getWitch(String name) {
		try {
			for (Enum<?> e : ParticleAPI.clazz.getEnumConstants()) {
				if (e.name().equals(name))
					return e;
			}

		} catch (IllegalArgumentException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param name
	 *            Unique name
	 * @param p
	 *            particle. Override play Method for Custom feelings <3
	 * @return successfully add.
	 */

	public static boolean registerParticle(String name, Particle p) {
		if (!ParticleAPI.byName.containsKey(name)) {
			ParticleAPI.byName.put(name, p);
			return true;
		}
		return false;
	}

}
