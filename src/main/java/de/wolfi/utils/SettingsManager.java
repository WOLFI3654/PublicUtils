package de.wolfi.utils;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SettingsManager {
	private File file;
		private FileConfiguration config;
		
		private SettingsManager(String fileName) {
			if (!UtilRegistry.getPlugin().getDataFolder().exists()) {
				UtilRegistry.getPlugin().getDataFolder().mkdir();
			}
			
			file = new File(UtilRegistry.getPlugin().getDataFolder(), fileName + ".yml");
			
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			config = YamlConfiguration.loadConfiguration(file);
		}
		
		@SuppressWarnings("unchecked")
		public <T> T get(String path) {
			return (T) config.get(path);
		}
		
		public Set<String> getKeys() {
			return config.getKeys(false);
		}
		
		public Map<String, Object> getValues() {
			return config.getValues(false);
		}
		
		public void set(String path, Object value) {
			config.set(path, value);
			save();
		}
		
		public boolean contains(String path) {
			return config.contains(path);
		}
		
		public ConfigurationSection createSection(String path) {
			ConfigurationSection section = config.createSection(path);
			save();
			return section;
		}
		
		
		public void save() {
			try {
				config.save(file);
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		private static final SettingsManager virtualEnemy = new SettingsManager("virtualEnemy");
		
		public static SettingsManager getVirtualEnemy() {
			// TODO Auto-generated method stub
			return virtualEnemy;
		}
		
}

