package de.wolfi.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Laser {

	private Method a;
	protected Object dataWatcher;

	protected int entityId;

	private Location location;

	public Laser(Location location) {
		this.entityId = new Random().nextInt(Integer.MAX_VALUE);
		Class<?> watcherClass = Reflection.getNMSClass("DataWatcher");
		Constructor<?> watcherConstructor = Reflection.getConstructor(watcherClass, Reflection.getNMSClass("Entity"));
		this.dataWatcher = Reflection.newInstance(watcherConstructor, (Object) null);
		byte data = (byte) this.calcData(0, 0, false); // onFire
		data = (byte) this.calcData(data, 1, false); // Crouched
		data = (byte) this.calcData(data, 3, false); // Sprinting
		data = (byte) this.calcData(data, 4, false); // Eating/Drinking/Block
		data = (byte) this.calcData(data, 5, true); // Invisible
		this.a = Reflection.getMethod(watcherClass, "a", int.class, Object.class);
		Reflection.invokeMethod(this.a, this.dataWatcher, 0, data);
		Reflection.invokeMethod(this.a, this.dataWatcher, 6, 20F);

		int type = this.calcType(0, 2, false); // Is Elderly
		type = this.calcType(type, 4, false); // Is retracting spikes
		Reflection.invokeMethod(this.a, this.dataWatcher, 16, type);

		this.location = location;
	}

	protected int calcData(int data, int id, boolean flag) {
		if (flag) {
			return Integer.valueOf(data | 1 << id);
		} else {
			return Integer.valueOf(data & ~(1 << id));
		}
	}

	protected int calcType(int type, int id, boolean flag) {
		if (flag) {
			return Integer.valueOf(type | id);
		} else {
			return Integer.valueOf(type & ~id);
		}
	}

	public void despawn() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			this.despawn(p);
		}
	}

	public void despawn(Player show) {

		Class<?> packetClass = Reflection.getNMSClass("PacketPlayOutEntityDestroy");
		Constructor<?> constructor = Reflection.getUnsafeConstructor(packetClass, 1);
		Object packet = Reflection.newInstance(constructor, new int[] { this.entityId });

		if (show == null) {
			this.sendPacket(packet);
		} else {
			this.sendPacket(show, packet);
		}
	}

	private void sendPacket(Object packet) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			this.sendPacket(player, packet);
		}
	}

	private void sendPacket(Player player, Object packet) {
		Method getHandle = Reflection.getMethod(player.getClass(), "getHandle");
		Object handle = Reflection.invokeMethod(getHandle, player);
		Field playerConnection = Reflection.getField(handle.getClass(), "playerConnection");
		Object connection = Reflection.invokeField(playerConnection, handle);
		Method sendPacket = Reflection.getUnsafeMethod(connection.getClass(), "sendPacket", 1);
		Reflection.invokeMethod(sendPacket, connection, packet);

	}

	protected void set(Object instance, String name, Object value) throws Exception {
		Field field = instance.getClass().getDeclaredField(name);
		field.setAccessible(true);
		field.set(instance, value);
	}

	public void setTarget(LivingEntity target) {
		try {
			this.spawn(null);
			Class<?> packetClass = Reflection.getNMSClass("PacketPlayOutEntityMetadata");
			Constructor<?> constructor = Reflection.getConstructor(packetClass);
			Object packet = Reflection.newInstance(constructor);
			this.set(packet, "a", this.entityId);
			Method getHandle = Reflection.getMethod(target.getClass(), "getHandle");
			Object handle = Reflection.invokeMethod(getHandle, target);
			Method getId = Reflection.getMethod(handle.getClass(), "getId");
			Object id = Reflection.invokeMethod(getId, handle);
			Reflection.invokeMethod(this.a, this.dataWatcher, 17, id);

			Method b = Reflection.getMethod(this.dataWatcher.getClass(), "b");
			this.set(packet, "b", Reflection.invokeMethod(b, this.dataWatcher));
			this.sendPacket(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setTarget(Player show, LivingEntity target) {
		try {
			this.spawn(show);
			Class<?> packetClass = Reflection.getNMSClass("PacketPlayOutEntityMetadata");
			Constructor<?> constructor = Reflection.getConstructor(packetClass);
			Object packet = Reflection.newInstance(constructor);
			this.set(packet, "a", this.entityId);

			Method getHandle = Reflection.getMethod(target.getClass(), "getHandle");
			Object handle = Reflection.invokeMethod(getHandle, target);
			Method getId = Reflection.getMethod(handle.getClass(), "getId");
			Object id = Reflection.invokeMethod(getId, handle);
			Reflection.invokeMethod(this.a, this.dataWatcher, 17, id);
			Method b = Reflection.getMethod(this.dataWatcher.getClass(), "b");
			this.set(packet, "b", Reflection.invokeMethod(b, this.dataWatcher));
			this.sendPacket(show, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void spawn(Player show) {
		try {
			Class<?> packetClass = Reflection.getNMSClass("PacketPlayOutSpawnEntityLiving");
			Constructor<?> packetConstructor = Reflection.getConstructor(packetClass);
			Object packet = Reflection.newInstance(packetConstructor);
			this.set(packet, "a", this.entityId);
			this.set(packet, "b", 68);
			this.set(packet, "c", this.toFixedPointNumber(this.location.getX()));
			this.set(packet, "d", this.toFixedPointNumber(this.location.getY()));
			this.set(packet, "e", this.toFixedPointNumber(this.location.getZ()));
			this.set(packet, "f", (int) this.toPackedByte(this.location.getYaw()));
			this.set(packet, "g", (int) this.toPackedByte(this.location.getPitch()));
			this.set(packet, "h", (int) this.toPackedByte(this.location.getPitch()));
			this.set(packet, "i", (byte) 0);
			this.set(packet, "j", (byte) 0);
			this.set(packet, "k", (byte) 0);
			this.set(packet, "l", this.dataWatcher);
			if (show == null) {
				this.sendPacket(packet);
			} else {
				this.sendPacket(show, packet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected int toFixedPointNumber(Double d) {
		return (int) (d * 32D);
	}

	protected byte toPackedByte(float f) {
		return (byte) (int) (f * 256.0F / 360.0F);
	}
}