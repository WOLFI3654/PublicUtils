package de.wolfi.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import io.netty.channel.Channel;
//import javassist.ClassPool;
//import javassist.CtClass;
//import javassist.CtMethod;
//import javassist.CtNewConstructor;
//import javassist.CtNewMethod;

public class PlayerListInjection {

	private static Class<?> clazz;
	// private static Class<?> playerConnection;
	private static Method join;
	private static Method leave;

	private static Object list;

	private static Object server;

	static {
		PlayerListInjection.list = Reflection
				.invokeMethod(Reflection.getMethod(Bukkit.getServer().getClass(), "getHandle"), Bukkit.getServer());
		PlayerListInjection.clazz = PlayerListInjection.list.getClass().getSuperclass();

		PlayerListInjection.server = Reflection
				.invokeMethod(Reflection.getMethod(Bukkit.getServer().getClass(), "getServer"), Bukkit.getServer());
		PlayerListInjection.join = Reflection.getUnsafeMethod(PlayerListInjection.clazz, "onPlayerJoin", 2);
		PlayerListInjection.leave = Reflection.getUnsafeMethod(PlayerListInjection.clazz, "disconnect", 1);

		// try{
		// CtClass origClazz =
		// ClassPool.getDefault().get(Reflection.getNMSClass("PlayerConnection").getName());
		// CtClass subClass =
		// ClassPool.getDefault().makeClass("de.wolfi.tmp.classes.PlayerConnection"+
		// "New", origClazz);
		// CtMethod m = CtNewMethod.make(
		// "public void
		// sendPacket("+Reflection.getNMSClass("Packet").getName()+" p){}",
		// subClass );
		// subClass.addMethod(m);
		// subClass.addConstructor(CtNewConstructor.make(
		// "public PlayerConnectionNew("+server.getClass().getName()+"
		// s,"+Reflection.getNMSClass("NetworkManager").getName()+"
		// n,"+Reflection.getNMSClass("EntityPlayer").getName()+" p){"
		// + "super($$);"
		// + "}", subClass));
		// playerConnection = subClass.getClass();
		//
		// }catch(Exception e){
		// e.printStackTrace();
		// }

	}

	public static void broadcastPacket(Object packet) {
		for (Player p : Bukkit.getOnlinePlayers())
			PlayerListInjection.sendPacket(p, packet);
	}

	/**
	 * create fake player
	 * 
	 * @param uuid
	 * @param name
	 * @param w
	 * @return
	 */
	public static Object createEntityPlayer(UUID uuid, String name, World w) {
		GameProfile profile = new GameProfile(uuid, name);
		Object worldServer = PlayerListInjection.worldServer(w);

		Object player = Reflection.newInstance(
				Reflection.getUnsafeConstructor(Reflection.getNMSClass("EntityPlayer"), 4), PlayerListInjection.server,
				worldServer, profile, PlayerListInjection.interactManager(worldServer));
		// ((EntityPlayer)player).playerConnection = new
		// PlayerConnection((MinecraftServer)server, new
		// NetworkManager(EnumProtocolDirection.CLIENTBOUND),
		// (EntityPlayer)player);
		try {
			Reflection.getField(player.getClass(), "playerConnection").set(player, PlayerListInjection
					.playerConnection(PlayerListInjection.server, PlayerListInjection.networkManager(), player));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return player;

	}
	// /**
	// * [WIP] Create Safe Entity HOOK
	// * @param uuid
	// * @param name
	// * @param w
	// * @return
	// */
	// public static Object createSecureEntityPlayer(UUID uuid, String
	// name,World w){
	// GameProfile profile = new GameProfile(uuid, name);
	// Object worldServer = worldServer(w);
	//
	// Object player =
	// Reflection.newInstance(Reflection.getUnsafeConstructor(Reflection.getNMSClass("EntityPlayer"),
	// 4),server,worldServer,profile,interactManager(worldServer));
	// //((EntityPlayer)player).playerConnection = new
	// PlayerConnection((MinecraftServer)server, new
	// NetworkManager(EnumProtocolDirection.CLIENTBOUND), (EntityPlayer)player);
	// try{
	// Reflection.getField(player.getClass(),"playerConnection").set(player,securePlayerConnection(server,networkManager(),player));
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// return player;
	//
	// }

	/**
	 * Entity Human to Bukkit player
	 * 
	 * @param human
	 * @return
	 */
	public static Player entityHumanToPlayer(Object human) {

		return (Player) Reflection.invokeMethod(Reflection.getMethod(human.getClass(), "getBukkitEntity"), human);
	}

	public static Channel getChannel(Object networkManager) {
		return (Channel) Reflection.invokeField(Reflection.getField(networkManager.getClass(), "channel"),
				networkManager);
	}

	public static Channel getChannel(Player player) {
		return PlayerListInjection
				.getChannel(PlayerListInjection.networkManager(PlayerListInjection.getPlayerConnection(player)));
	}

	public static Object getPlayerConnection(Object human) {
		return Reflection.invokeField(Reflection.getField(human.getClass(), "playerConnection"), human);
	}

	public static boolean setPlayerConnection(Player player, Object con){
		return setPlayerConnection(PlayerListInjection.playerToEntityHuman(player), con);
	}
	
	private static boolean setPlayerConnection(Object playerToEntityHuman, Object con) {
		Field f = Reflection.getField(playerToEntityHuman.getClass(), "playerConnection");
		f.setAccessible(true);
		try {
			f.set(playerToEntityHuman, con);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Object getPlayerConnection(Player player) {
		return PlayerListInjection.getPlayerConnection(PlayerListInjection.playerToEntityHuman(player));
	}

	private static Object interactManager(Object worldServer) {
		return Reflection.newInstance(
				Reflection.getUnsafeConstructor(Reflection.getNMSClass("PlayerInteractManager"), 1), worldServer);
	}

	/**
	 * Let Fakeplayer join
	 * 
	 * @param EntityPlayer
	 * @param message
	 */
	public static void join(Object EntityPlayer, String message) {
		Reflection.invokeMethod(PlayerListInjection.join, PlayerListInjection.list, EntityPlayer, message);
	}

	/**
	 * Let Fakeplayer leave
	 * 
	 * @param EntityPlayer
	 */
	public static void leave(Object EntityPlayer) {
		Reflection.invokeMethod(PlayerListInjection.leave, PlayerListInjection.list, EntityPlayer);
	}

	private static Object networkManager() {
		Class<?> c = Reflection.getNMSClass("EnumProtocolDirection");

		Object side = Reflection.invokeMethod(Reflection.getMethod(c, "valueOf", String.class), null, "CLIENTBOUND");
		Object manager = Reflection.newInstance(Reflection.getConstructor(Reflection.getNMSClass("NetworkManager"), c),
				side);
		try {
			Reflection.getField(manager.getClass(), "channel").set(manager, new NullChannel(null));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return manager;
	}

	// private static Object securePlayerConnection(Object server, Object
	// network, Object player){
	//
	// return
	// Reflection.newInstance(Reflection.getConstructor(playerConnection,server.getClass().getSuperclass(),network.getClass(),player.getClass()),
	// server,network,player);
	//
	// }

	public static Object networkManager(Object playerConnection) {
		return Reflection.invokeField(Reflection.getField(playerConnection.getClass(), "networkManager"),
				playerConnection);
	}

	private static Object playerConnection(Object server, Object network, Object player) {

		return Reflection.newInstance(Reflection.getConstructor(Reflection.getNMSClass("PlayerConnection"),
				server.getClass().getSuperclass(), network.getClass(), player.getClass()), server, network, player);

	}

	/**
	 * Bukkit Player to entity Human
	 * 
	 * @param player
	 * @return
	 */
	public static Object playerToEntityHuman(Player player) {
		return Reflection.invokeMethod(Reflection.getMethod(player.getClass(), "getHandle"), player);
	}

	public static void sendPacket(Player player, Object packet) {
		Object con = PlayerListInjection.getPlayerConnection(player);
		Reflection.invokeMethod(Reflection.getMethod(con.getClass(), "sendPacket", Reflection.getNMSClass("Packet")),
				con, packet);
	}

	public static void setChannel(Object human, Channel channel) {
		PlayerListInjection.setChannelFromNetworkManager(
				PlayerListInjection.networkManager(PlayerListInjection.getPlayerConnection(human)), channel);
	}

	public static void setChannel(Player player, Channel channel) {
		PlayerListInjection.setChannel(PlayerListInjection.playerToEntityHuman(player), channel);
	}

	public static void setChannelFromNetworkManager(Object networkManager, Channel channel) {
		try {
			Reflection.getField(networkManager.getClass(), "channel").set(networkManager, channel);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * set skin from player
	 * 
	 * @param player
	 * @param to
	 */
	public static void setSkin(Player player, GameProfile to) {
		PlayerListInjection.setSkin(player, to.getName());
		// GameProfile pp = (GameProfile)
		// Reflection.invokeMethod(Reflection.getMethod(player.getClass(),
		// "getProfile"), player);
		//
		// Collection<Property> props = to.getProperties().get("textures");
		// pp.getProperties().removeAll("textures");
		// pp.getProperties().putAll("textures", props);
	}

	/**
	 * Set skin from player
	 * 
	 * @param player
	 * @param from
	 */
	public static void setSkin(Player player, Player from) {
		PlayerListInjection.setSkin(player,
				(GameProfile) Reflection.invokeMethod(Reflection.getMethod(from.getClass(), "getProfile"), from));
	}

	/**
	 * set skin from player
	 * 
	 * @param player
	 * @param to
	 */
	public static void setSkin(Player player, String to) {
		GameProfile profile = (GameProfile) Reflection
				.invokeMethod(Reflection.getMethod(player.getClass(), "getProfile"), player);
		ProfileLoader loader = new ProfileLoader(player.getUniqueId().toString(), player.getName(), to);
		GameProfile loaded = loader.loadProfile();
		profile.getProperties().clear();
		profile.getProperties().putAll(loaded.getProperties());
		// try {
		// // Get the name from SwordPVP
		// URL url = new
		// URL("https://sessionserver.mojang.com/session/minecraft/profile/" +
		// to.toString().replaceAll("-", "") + "?unsigned=false");
		// URLConnection uc = url.openConnection();
		// uc.setUseCaches(false);
		// uc.setDefaultUseCaches(false);
		// uc.addRequestProperty("User-Agent", "Mozilla/5.0");
		// uc.addRequestProperty("Cache-Control", "no-cache, no-store,
		// must-revalidate");
		// uc.addRequestProperty("Pragma", "no-cache");
		//
		// // Parse it
		// Scanner ss = new Scanner(uc.getInputStream(), "UTF-8");
		//
		//
		//
		// String json = ss.useDelimiter("\\A").next();
		// ss.close();
		// JSONParser parser = new JSONParser();
		// Object obj = parser.parse(json);
		// JSONArray properties = (JSONArray) ((JSONObject)
		// obj).get("properties");
		// for (int i = 0; i < properties.size(); i++) {
		// try {
		// JSONObject property = (JSONObject) properties.get(i);
		// String name = (String) property.get("name");
		// String value = (String) property.get("value");
		// String signature = property.containsKey("signature") ? (String)
		// property.get("signature") : null;
		// if (signature != null) {
		// profile.getProperties().put(name, new Property(name, value,
		// signature));
		// } else {
		// profile.getProperties().put(name, new Property(value, name));
		// }
		// } catch (Exception e) {
		// Bukkit.getLogger().log(Level.WARNING, "Failed to apply auth
		// property", e);
		// }
		// }
		// } catch (Exception e) {
		// ; // Failed to load skin
		// }
	}

	private static Object worldServer(World w) {
		return Reflection.invokeMethod(Reflection.getMethod(w.getClass(), "getHandle"), w);
	}

}
