package de.wolfi.utils.fancyserver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginSetuper {
	private SimpleCommandMap map;
	public final List<String> allowedPlugins;
	private final Class<? extends JavaPlugin> plugin;
	public PluginSetuper(Class<? extends JavaPlugin> plugin, String... plugins) {
		this.plugin = plugin;
		allowedPlugins = Arrays.asList(plugins);
		Class<?> clazz = Bukkit.getServer().getClass();

		try {
			this.map = (SimpleCommandMap) this.getMethod(clazz, "getCommandMap").invoke(Bukkit.getServer());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void clearCMDS() throws Exception {
		Class<? extends SimpleCommandMap> clazz = this.map.getClass();

		Field f = clazz.getDeclaredField("knownCommands");
		f.setAccessible(true);

		@SuppressWarnings("unchecked")
		Map<String, Command> knownCommands = (Map<String, Command>) f.get(this.map);
		for (Map.Entry<String, Command> entry : knownCommands.entrySet()) {
			entry.getValue().unregister(this.map);
		}
		knownCommands.clear();

	}

	public boolean commandExist(String name) {

		for (Command c : this.map.getCommands()) {
			if (c.getName().equalsIgnoreCase(name) || c.getAliases().contains(name)) {
				return true;
			}
		}
		return false;
	}

	private Method getMethod(Class<?> clazz, String name) {
		for (Method m : clazz.getMethods()) {
			if (m.getName().equalsIgnoreCase(name)) {
				m.setAccessible(true);
				return m;
			}
		}
		return null;
	}

	//

	public void registerCommand(Command cmd) {

		this.map.register("default", cmd);
	}

	public void setName(String name, PluginDescriptionFile desc) {
		try {
			Class<? extends PluginDescriptionFile> clazz = desc.getClass();
			Field f = clazz.getDeclaredField("name");
			f.setAccessible(true);
			f.set(desc, name);
			f = clazz.getDeclaredField("prefix");
			f.setAccessible(true);
			f.set(desc, name);
			f = clazz.getDeclaredField("rawName");
			f.setAccessible(true);
			f.set(desc, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setupFancyServer() {
		
		try {

			HandlerList.unregisterAll();
			Bukkit.getScheduler().cancelAllTasks();
			for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
				if (!p.getName().equalsIgnoreCase(JavaPlugin.getPlugin(plugin).getName()) && !allowedPlugins.contains(p.getName()))
					Bukkit.getPluginManager().disablePlugin(p);

			}
			Bukkit.getPluginManager().registerEvents(new Listener() {
				@EventHandler
				public void onEnable(PluginEnableEvent e) {
					if (!e.getPlugin().getName().equalsIgnoreCase(JavaPlugin.getPlugin(plugin).getName()) && !allowedPlugins.contains(e.getPlugin().getName()))
						
						e.getPlugin().getPluginLoader().disablePlugin(e.getPlugin());
				}
			}, JavaPlugin.getPlugin(plugin));
			this.clearCMDS();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
