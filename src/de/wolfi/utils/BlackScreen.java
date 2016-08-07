package de.wolfi.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BlackScreen {


    private static Class<?> packetClass;

    private static Object packetObject;

    private static Map<UUID, Integer> ticksLeft;

    private static String VERSION;

    static {
	BlackScreen.setupUtil();
	BlackScreen.ticksLeft = new HashMap<UUID, Integer>();
	String path = Bukkit.getServer().getClass().getPackage().getName();
	BlackScreen.VERSION = path.substring(path.lastIndexOf(".") + 1, path.length());
	try {
	    BlackScreen.packetClass = Class.forName("net.minecraft.server." + BlackScreen.VERSION + ".Packet");
	    Class<?> packetGameStateClass = Class
		    .forName("net.minecraft.server." + BlackScreen.VERSION + ".PacketPlayOutGameStateChange");
	    BlackScreen.packetObject = packetGameStateClass.getConstructor(new Class[] { int.class, float.class })
		    .newInstance(new Object[] { 4, 0 });
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private static void sendPacket(Player p) {
	try {
	    Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
	    Field playerConnectionField = nmsPlayer.getClass().getField("playerConnection");
	    Object pConnection = playerConnectionField.get(nmsPlayer);
	    Method sendPacket = pConnection.getClass().getMethod("sendPacket", new Class[] { BlackScreen.packetClass });
	    sendPacket.invoke(pConnection, new Object[] { BlackScreen.packetObject });
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    /**
     * 68. * 69. * @param p The target player 70. * @param seconds The duration
     * 71.
     */
    public static void setBlack(Player p, int seconds) {
	BlackScreen.ticksLeft.put(p.getUniqueId(), seconds * 20);
	BlackScreen.sendPacket(p); // Send initial packet
    }

    /**
     * 21. * Call the method in onEnable()! 22. * @param instance An instance of
     * your main class 23.
     */
    public static void setupUtil() {
	BlackScreen.setupUtil(UtilRegistry.getPlugin(), 1);
    }

    /**
     * 29. * Call the method in onEnable()! 30. * @param instance An instance of
     * your main class 31. * @param repeatingTicks Interval (in ticks) for the
     * scheduler 32.
     */
    public static void setupUtil(Plugin instance, int repeatingTicks) {
	Bukkit.getScheduler().runTaskTimer(instance, new Runnable() {

	    @Override
	    public void run() {
		for (UUID uuid : BlackScreen.ticksLeft.keySet()) {
		    Player p = Bukkit.getPlayer(uuid);
		    if (p == null) {
			BlackScreen.ticksLeft.remove(uuid);
		    } else {
			if (BlackScreen.ticksLeft.get(uuid) > 0) {
			    BlackScreen.ticksLeft.put(uuid, BlackScreen.ticksLeft.get(uuid) - repeatingTicks);
			    BlackScreen.sendPacket(p);
			} else {
			    BlackScreen.ticksLeft.remove(uuid);
			    p.closeInventory();
			}
		    }
		}
	    }
	}, 0L, repeatingTicks);
    }
}
