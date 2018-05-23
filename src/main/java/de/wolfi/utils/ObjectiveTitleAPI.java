package de.wolfi.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Minecraft 1.8 (1.9) Title
 * 
 * @version 1.0.5
 * @author Maxim Van de Wynckel
 */
public class ObjectiveTitleAPI {
	private static Class<?> chatBaseComponent;
	private static final Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap<Class<?>, Class<?>>();
	/* Chat serializer */
	private static Class<?> nmsChatSerializer;
	/* Title packet actions ENUM */
	private static Class<?> packetActions;
	/* Title packet */
	private static Class<?> packetTitle;

	private static boolean equalsTypeArray(Class<?>[] a, Class<?>[] o) {
		if (a.length != o.length)
			return false;
		for (int i = 0; i < a.length; i++)
			if (!a[i].equals(o[i]) && !a[i].isAssignableFrom(o[i]))
				return false;
		return true;
	}

	/* Title timings */
	private int fadeInTime = -1;
	private int fadeOutTime = -1;
	private int stayTime = -1;
	/* Subtitle text and color */
	private String subtitle = "";
	private ChatColor subtitleColor = ChatColor.WHITE;
	private boolean ticks = false;

	/* Title text and color */
	private String title = "";

	private ChatColor titleColor = ChatColor.WHITE;

	public ObjectiveTitleAPI() {
		this.loadClasses();
	}

	/**
	 * Copy 1.8 title
	 * 
	 * @param title
	 *            Title
	 */
	public ObjectiveTitleAPI(ObjectiveTitleAPI title) {
		// Copy title
		this.title = title.getTitle();
		this.subtitle = title.getSubtitle();
		this.titleColor = title.getTitleColor();
		this.subtitleColor = title.getSubtitleColor();
		this.fadeInTime = title.getFadeInTime();
		this.fadeOutTime = title.getFadeOutTime();
		this.stayTime = title.getStayTime();
		this.ticks = title.isTicks();
		this.loadClasses();
	}

	/**
	 * Create a new 1.8 title
	 * 
	 * @param title
	 *            Title
	 */
	public ObjectiveTitleAPI(String title) {
		this.title = title;
		this.loadClasses();
	}

	/**
	 * Create a new 1.8 title
	 * 
	 * @param title
	 *            Title text
	 * @param subtitle
	 *            Subtitle text
	 */
	public ObjectiveTitleAPI(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
		this.loadClasses();
	}

	/**
	 * Create a new 1.8 title
	 * 
	 * @param title
	 *            Title text
	 * @param subtitle
	 *            Subtitle text
	 * @param fadeInTime
	 *            Fade in time
	 * @param stayTime
	 *            Stay on screen time
	 * @param fadeOutTime
	 *            Fade out time
	 */
	public ObjectiveTitleAPI(String title, String subtitle, int fadeInTime, int stayTime, int fadeOutTime) {
		this.title = title;
		this.subtitle = subtitle;
		this.fadeInTime = fadeInTime;
		this.stayTime = stayTime;
		this.fadeOutTime = fadeOutTime;
		this.loadClasses();
	}

	/**
	 * Broadcast the title to all players
	 */
	public void broadcast() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			this.send(p);
		}
	}

	private boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
		boolean equal = true;
		if (l1.length != l2.length)
			return false;
		for (int i = 0; i < l1.length; i++)
			if (l1[i] != l2[i]) {
				equal = false;
				break;
			}
		return equal;
	}

	/**
	 * Clear the title
	 * 
	 * @param player
	 *            Player
	 */
	public void clearTitle(Player player) {
		try {
			// Send timings first
			Object handle = this.getHandle(player);
			Object connection = this.getField(handle.getClass(), "playerConnection").get(handle);
			Object[] actions = ObjectiveTitleAPI.packetActions.getEnumConstants();
			Method sendPacket = this.getMethod(connection.getClass(), "sendPacket");
			Object packet = ObjectiveTitleAPI.packetTitle
					.getConstructor(ObjectiveTitleAPI.packetActions, ObjectiveTitleAPI.chatBaseComponent)
					.newInstance(actions[3], null);
			sendPacket.invoke(connection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getFadeInTime() {
		return this.fadeInTime;
	}

	public int getFadeOutTime() {
		return this.fadeOutTime;
	}

	private Field getField(Class<?> clazz, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Object getHandle(Object obj) {
		try {
			return this.getMethod("getHandle", obj.getClass()).invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Method getMethod(Class<?> clazz, String name, Class<?>... args) {
		for (Method m : clazz.getMethods())
			if (m.getName().equals(name) && (args.length == 0 || this.ClassListEqual(args, m.getParameterTypes()))) {
				m.setAccessible(true);
				return m;
			}
		return null;
	}

	private Method getMethod(String name, Class<?> clazz, Class<?>... paramTypes) {
		Class<?>[] t = this.toPrimitiveTypeArray(paramTypes);
		for (Method m : clazz.getMethods()) {
			Class<?>[] types = this.toPrimitiveTypeArray(m.getParameterTypes());
			if (m.getName().equals(name) && ObjectiveTitleAPI.equalsTypeArray(types, t))
				return m;
		}
		return null;
	}

	private Class<?> getNMSClass(String className) {
		String fullName = "net.minecraft.server." + this.getVersion() + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	private Class<?> getPrimitiveType(Class<?> clazz) {
		return ObjectiveTitleAPI.CORRESPONDING_TYPES.containsKey(clazz)
				? ObjectiveTitleAPI.CORRESPONDING_TYPES.get(clazz) : clazz;
	}

	public int getStayTime() {
		return this.stayTime;
	}

	/**
	 * Get subtitle text
	 * 
	 * @return Subtitle text
	 */
	public String getSubtitle() {
		return this.subtitle;
	}

	public ChatColor getSubtitleColor() {
		return this.subtitleColor;
	}

	/**
	 * Get title text
	 * 
	 * @return Title text
	 */
	public String getTitle() {
		return this.title;
	}

	public ChatColor getTitleColor() {
		return this.titleColor;
	}

	private String getVersion() {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String version = name.substring(name.lastIndexOf('.') + 1) + ".";
		return version;
	}

	public boolean isTicks() {
		return this.ticks;
	}

	/**
	 * Load spigot and NMS classes
	 */
	private void loadClasses() {
		if (ObjectiveTitleAPI.packetTitle == null) {
			ObjectiveTitleAPI.packetTitle = this.getNMSClass("PacketPlayOutTitle");
			ObjectiveTitleAPI.packetActions = this.getNMSClass("PacketPlayOutTitle$EnumTitleAction");
			ObjectiveTitleAPI.chatBaseComponent = this.getNMSClass("IChatBaseComponent");
			ObjectiveTitleAPI.nmsChatSerializer = this.getNMSClass("ChatComponentText");
		}
	}

	/**
	 * Reset the title settings
	 * 
	 * @param player
	 *            Player
	 */
	public void resetTitle(Player player) {
		try {
			// Send timings first
			Object handle = this.getHandle(player);
			Object connection = this.getField(handle.getClass(), "playerConnection").get(handle);
			Object[] actions = ObjectiveTitleAPI.packetActions.getEnumConstants();
			Method sendPacket = this.getMethod(connection.getClass(), "sendPacket");
			Object packet = ObjectiveTitleAPI.packetTitle
					.getConstructor(ObjectiveTitleAPI.packetActions, ObjectiveTitleAPI.chatBaseComponent)
					.newInstance(actions[4], null);
			sendPacket.invoke(connection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send the title to a player
	 * 
	 * @param player
	 *            Player
	 */
	public void send(Player player) {
		if (ObjectiveTitleAPI.packetTitle != null) {
			// First reset previous settings
			this.resetTitle(player);
			try {
				// Send timings first
				Object handle = this.getHandle(player);
				Object connection = this.getField(handle.getClass(), "playerConnection").get(handle);
				Object[] actions = ObjectiveTitleAPI.packetActions.getEnumConstants();
				Method sendPacket = this.getMethod(connection.getClass(), "sendPacket");
				Object packet = ObjectiveTitleAPI.packetTitle.getConstructor(ObjectiveTitleAPI.packetActions,
						ObjectiveTitleAPI.chatBaseComponent, Integer.TYPE, Integer.TYPE, Integer.TYPE)
						.newInstance(actions[2], null, this.fadeInTime * (this.ticks ? 1 : 20),
								this.stayTime * (this.ticks ? 1 : 20), this.fadeOutTime * (this.ticks ? 1 : 20));
				// Send if set
				if (this.fadeInTime != -1 && this.fadeOutTime != -1 && this.stayTime != -1)
					sendPacket.invoke(connection, packet);

				// Send title
				Object serialized = ObjectiveTitleAPI.nmsChatSerializer.getConstructor(String.class)
						.newInstance(ChatColor.translateAlternateColorCodes('&', this.title));
				packet = ObjectiveTitleAPI.packetTitle
						.getConstructor(ObjectiveTitleAPI.packetActions, ObjectiveTitleAPI.chatBaseComponent)
						.newInstance(actions[0], serialized);
				sendPacket.invoke(connection, packet);
				if (!(this.subtitle).equals("")) {
					// Send subtitle if present
					serialized = ObjectiveTitleAPI.nmsChatSerializer.getConstructor(String.class)
							.newInstance(ChatColor.translateAlternateColorCodes('&', this.subtitle));
					packet = ObjectiveTitleAPI.packetTitle
							.getConstructor(ObjectiveTitleAPI.packetActions, ObjectiveTitleAPI.chatBaseComponent)
							.newInstance(actions[1], serialized);
					sendPacket.invoke(connection, packet);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Set title fade in time
	 * 
	 * @param time
	 *            Time
	 */
	public void setFadeInTime(int time) {
		this.fadeInTime = time;
	}

	/**
	 * Set title fade out time
	 * 
	 * @param time
	 *            Time
	 */
	public void setFadeOutTime(int time) {
		this.fadeOutTime = time;
	}

	/**
	 * Set title stay time
	 * 
	 * @param time
	 *            Time
	 */
	public void setStayTime(int time) {
		this.stayTime = time;
	}

	/**
	 * Set subtitle text
	 * 
	 * @param subtitle
	 *            Subtitle text
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * Set the subtitle color
	 * 
	 * @param color
	 *            Chat color
	 */
	public void setSubtitleColor(ChatColor color) {
		this.subtitleColor = color;
	}

	/**
	 * Set timings to seconds
	 */
	public void setTimingsToSeconds() {
		this.ticks = false;
	}

	/**
	 * Set timings to ticks
	 */
	public void setTimingsToTicks() {
		this.ticks = true;
	}

	/**
	 * Set title text
	 * 
	 * @param title
	 *            Title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Set the title color
	 * 
	 * @param color
	 *            Chat color
	 */
	public void setTitleColor(ChatColor color) {
		this.titleColor = color;
	}

	private Class<?>[] toPrimitiveTypeArray(Class<?>[] classes) {
		int a = classes != null ? classes.length : 0;
		Class<?>[] types = new Class<?>[a];
		for (int i = 0; i < a; i++)
			types[i] = this.getPrimitiveType(classes[i]);
		return types;
	}

	public void updateSubtitle(Player player) {
		if (ObjectiveTitleAPI.packetTitle != null) {
			try {
				Object handle = this.getHandle(player);
				Object connection = this.getField(handle.getClass(), "playerConnection").get(handle);
				Object[] actions = ObjectiveTitleAPI.packetActions.getEnumConstants();
				Method sendPacket = this.getMethod(connection.getClass(), "sendPacket");
				Object serialized = ObjectiveTitleAPI.nmsChatSerializer.getConstructor(String.class)
						.newInstance(ChatColor.translateAlternateColorCodes('&', this.subtitle));
				Object packet = ObjectiveTitleAPI.packetTitle
						.getConstructor(
								new Class[] { ObjectiveTitleAPI.packetActions, ObjectiveTitleAPI.chatBaseComponent })
						.newInstance(new Object[] { actions[1], serialized });
				sendPacket.invoke(connection, packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void updateTimes(Player player) {
		if (ObjectiveTitleAPI.packetTitle != null) {
			try {
				Object handle = this.getHandle(player);
				Object connection = this.getField(handle.getClass(), "playerConnection").get(handle);
				Object[] actions = ObjectiveTitleAPI.packetActions.getEnumConstants();
				Method sendPacket = this.getMethod(connection.getClass(), "sendPacket");
				Object packet = ObjectiveTitleAPI.packetTitle
						.getConstructor(new Class[] { ObjectiveTitleAPI.packetActions,
								ObjectiveTitleAPI.chatBaseComponent, Integer.TYPE, Integer.TYPE, Integer.TYPE })
						.newInstance(new Object[] { actions[2], null,
								Integer.valueOf(this.fadeInTime * (this.ticks ? 1 : 20)),
								Integer.valueOf(this.stayTime * (this.ticks ? 1 : 20)),
								Integer.valueOf(this.fadeOutTime * (this.ticks ? 1 : 20)) });
				if (this.fadeInTime != -1 && this.fadeOutTime != -1 && this.stayTime != -1) {
					sendPacket.invoke(connection, packet);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void updateTitle(Player player) {
		if (ObjectiveTitleAPI.packetTitle != null) {
			try {
				Object handle = this.getHandle(player);
				Object connection = this.getField(handle.getClass(), "playerConnection").get(handle);
				Object[] actions = ObjectiveTitleAPI.packetActions.getEnumConstants();
				Method sendPacket = this.getMethod(connection.getClass(), "sendPacket");
				Object serialized = ObjectiveTitleAPI.nmsChatSerializer.getConstructor(String.class)
						.newInstance(ChatColor.translateAlternateColorCodes('&', this.title));
				Object packet = ObjectiveTitleAPI.packetTitle
						.getConstructor(
								new Class[] { ObjectiveTitleAPI.packetActions, ObjectiveTitleAPI.chatBaseComponent })
						.newInstance(new Object[] { actions[0], serialized });
				sendPacket.invoke(connection, packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
