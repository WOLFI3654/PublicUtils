package de.wolfi.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class TitlesAPI {

	public static void clearTitle(Player player) {
		TitlesAPI.sendTitle(player, 0, 0, 0, "", "");
	}

	public static void sendFullTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title,
			String subtitle) {
		TitlesAPI.sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
	}

	public static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", Reflection.getNMSClass("Packet"))
					.invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendSubtitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String message) {
		TitlesAPI.sendTitle(player, fadeIn, stay, fadeOut, null, message);
	}

	public static void sendTabTitle(Player player, String header, String footer) {
		if (header == null)
			header = "";

		if (footer == null)
			footer = "";

		header = header.replaceAll("%player%", player.getDisplayName());
		footer = footer.replaceAll("%player%", player.getDisplayName());

		try {
			Object tabHeader = Reflection.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
					.getMethod("a", String.class).invoke(null, "{\"text\":\"" + header + "\"}");
			Object tabFooter = Reflection.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
					.getMethod("a", String.class).invoke(null, "{\"text\":\"" + footer + "\"}");
			Constructor<?> titleConstructor = Reflection.getNMSClass("PacketPlayOutPlayerListHeaderFooter")
					.getConstructor(Reflection.getNMSClass("IChatBaseComponent"));
			Object packet = titleConstructor.newInstance(tabHeader);
			Field field = packet.getClass().getDeclaredField("b");
			field.setAccessible(true);
			field.set(packet, tabFooter);
			PlayerListInjection.sendPacket(player, packet);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String message) {
		TitlesAPI.sendTitle(player, fadeIn, stay, fadeOut, message, null);
	}

	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title,
			String subtitle) {

		try {
			Object e;
			Object chatTitle;
			Object chatSubtitle;
			Constructor<?> subtitleConstructor;
			Object titlePacket;
			Object subtitlePacket;

			if (title != null) {
				title = ChatColor.translateAlternateColorCodes('&', title);
				title = title.replaceAll("%player%", player.getDisplayName());
				// Times packets
				e = Reflection.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES")
						.get((Object) null);
				chatTitle = Reflection.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class })
						.invoke((Object) null, new Object[] { "{\"text\":\"" + title + "\"}" });
				subtitleConstructor = Reflection.getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] {
						Reflection.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
						Reflection.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
				titlePacket = subtitleConstructor.newInstance(new Object[] { e, chatTitle, fadeIn, stay, fadeOut });
				TitlesAPI.sendPacket(player, titlePacket);

				e = Reflection.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE")
						.get((Object) null);
				chatTitle = Reflection.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class })
						.invoke((Object) null, new Object[] { "{\"text\":\"" + title + "\"}" });
				subtitleConstructor = Reflection.getNMSClass("PacketPlayOutTitle").getConstructor(
						new Class[] { Reflection.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
								Reflection.getNMSClass("IChatBaseComponent") });
				titlePacket = subtitleConstructor.newInstance(new Object[] { e, chatTitle });
				TitlesAPI.sendPacket(player, titlePacket);
			}

			if (subtitle != null) {
				subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
				subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
				// Times packets
				e = Reflection.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES")
						.get((Object) null);
				chatSubtitle = Reflection.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class })
						.invoke((Object) null, new Object[] { "{\"text\":\"" + title + "\"}" });
				subtitleConstructor = Reflection.getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] {
						Reflection.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
						Reflection.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
				subtitlePacket = subtitleConstructor
						.newInstance(new Object[] { e, chatSubtitle, fadeIn, stay, fadeOut });
				TitlesAPI.sendPacket(player, subtitlePacket);

				e = Reflection.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE")
						.get((Object) null);
				chatSubtitle = Reflection.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class })
						.invoke((Object) null, new Object[] { "{\"text\":\"" + subtitle + "\"}" });
				subtitleConstructor = Reflection.getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] {
						Reflection.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
						Reflection.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
				subtitlePacket = subtitleConstructor
						.newInstance(new Object[] { e, chatSubtitle, fadeIn, stay, fadeOut });
				TitlesAPI.sendPacket(player, subtitlePacket);
			}
		} catch (Exception var11) {
			var11.printStackTrace();
		}
	}

}
