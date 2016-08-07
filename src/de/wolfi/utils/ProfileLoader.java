package de.wolfi.utils;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class ProfileLoader {
	private final String name;
	private final String skinOwner;
	private final String uuid;

	public ProfileLoader(String uuid, String name) {
		this(uuid, name, name);
	}

	public ProfileLoader(String uuid, String name, String skinOwner) {
		this.uuid = uuid == null ? null : uuid.replaceAll("-", ""); // We add
																	// these
																	// later
		String displayName = ChatColor.translateAlternateColorCodes('&', name);
		this.name = ChatColor.stripColor(displayName);
		this.skinOwner = skinOwner;
	}

	private void addProperties(GameProfile profile) {
		String uuid = this.getUUID(this.skinOwner);
		try {
			// Get the name from SwordPVP
			URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
			URLConnection uc = url.openConnection();
			uc.setUseCaches(false);
			uc.setDefaultUseCaches(false);
			uc.addRequestProperty("User-Agent", "Mozilla/5.0");
			uc.addRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
			uc.addRequestProperty("Pragma", "no-cache");

			// Parse it
			Scanner s = new Scanner(uc.getInputStream(), "UTF-8");
			String json = s.useDelimiter("\\A").next();
			s.close();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(json);
			JSONArray properties = (JSONArray) ((JSONObject) obj).get("properties");
			for (int i = 0; i < properties.size(); i++) {
				try {
					JSONObject property = (JSONObject) properties.get(i);
					String name = (String) property.get("name");
					String value = (String) property.get("value");
					String signature = property.containsKey("signature") ? (String) property.get("signature") : null;
					if (signature != null) {
						profile.getProperties().put(name, new Property(name, value, signature));
					} else {
						profile.getProperties().put(name, new Property(value, name));
					}
				} catch (Exception e) {
					Bukkit.getLogger().log(Level.WARNING, "Failed to apply auth property", e);
				}
			}
		} catch (Exception e) {
			; // Failed to load skin
		}
	}

	@SuppressWarnings("deprecation")
	private String getUUID(String name) {
		return Bukkit.getOfflinePlayer(name).getUniqueId().toString().replaceAll("-", "");
	}

	public GameProfile loadProfile() {
		UUID id = this.uuid == null ? this.parseUUID(this.getUUID(this.name)) : this.parseUUID(this.uuid);
		GameProfile profile = new GameProfile(id, this.name);
		this.addProperties(profile);
		return profile;
	}

	private UUID parseUUID(String uuidStr) {
		// Split uuid in to 5 components
		String[] uuidComponents = new String[] { uuidStr.substring(0, 8), uuidStr.substring(8, 12),
				uuidStr.substring(12, 16), uuidStr.substring(16, 20), uuidStr.substring(20, uuidStr.length()) };

		// Combine components with a dash
		StringBuilder builder = new StringBuilder();
		for (String component : uuidComponents) {
			builder.append(component).append('-');
		}

		// Correct uuid length, remove last dash
		builder.setLength(builder.length() - 1);
		return UUID.fromString(builder.toString());
	}
}
