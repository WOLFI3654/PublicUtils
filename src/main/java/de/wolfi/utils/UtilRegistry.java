package de.wolfi.utils;

import org.bukkit.plugin.java.JavaPlugin;

public class UtilRegistry {

	protected static Class<? extends JavaPlugin> clazz;

	public static JavaPlugin getPlugin() {
		return JavaPlugin.getPlugin(UtilRegistry.clazz);
	}

	public static void registerUtils(Class<? extends JavaPlugin> plugin) {
		UtilRegistry.clazz = plugin;
	}

}
