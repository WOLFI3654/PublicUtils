package de.wolfi.utils.fancyserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import de.wolfi.utils.SerializeableMethod;
import de.wolfi.utils.ShortConsoleLogFormatter;
import de.wolfi.utils.fancyserver.ban.Ban;
import de.wolfi.utils.fancyserver.ban.BanCommand;
import de.wolfi.utils.fancyserver.ban.BanListener;


public class BanManager {

	public static final Logger logger = Logger.getLogger("BanLogger");
	
	static{
		
		try {
			FileHandler fl = new FileHandler("BanManager/"+Calendar.getInstance().getTime().toString()+".log");
			BanManager.logger.addHandler(fl);
			fl.setFormatter(new ShortConsoleLogFormatter());
			BanManager.logger.setUseParentHandlers(false);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Listener banListener;
	private HashMap<String,Ban> ipBans = new HashMap<>();

	private HashMap<UUID,Ban> uuidBans = new HashMap<>();

	private final Class<? extends JavaPlugin> main;
	private final PluginSetuper setup;
	private final String getterName;
	
	public BanManager(PluginSetuper setup, Class<? extends JavaPlugin> main, String getterName) {
		this.main = main;
		this.setup = setup;
		this.getterName = getterName;
	}

	public void addBan(UUID uuid, Ban ban) {
		if (!this.isBanned(uuid)) {
			this.uuidBans.put(uuid,ban);

			OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
			if (p.isOnline()) {
				p.getPlayer().kickPlayer(ban.getMessage());
			}
		}
	}

	public void addIPBan(String ip,Ban ban) {
		if (!this.isBanned(ip))
			this.ipBans.put(ip,ban);
		for(Player p : Bukkit.getOnlinePlayers()){
			if((p.getAddress().toString()+":").split(":")[0].equals(ip)){
				p.kickPlayer(ban.getMessage());
			}
		}
	}

	public boolean isBanned(String ip) {
		return this.ipBans.containsKey(ip);
	}

	public boolean isBanned(UUID uuid) {
		return this.uuidBans.containsKey(uuid);
	}
	
	public Ban getIpBan(String ip){
		return ipBans.get(ip);
	}
	
	public Ban getUuidBan(UUID uuid){
		return uuidBans.get(uuid);
	}
	
	public Ban getUuidBanByName(String name){
		for (UUID u : this.uuidBans.keySet()) {
			OfflinePlayer p = Bukkit.getOfflinePlayer(u);
			if (p.getName().equals(name)) {
				
				return getUuidBan(u);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void load() {
		Bukkit.getPluginManager().registerEvents(this.banListener = new BanListener(this),
				JavaPlugin.getPlugin(main));
		
		File file = new File("bans.bin");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		try {
			ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
			this.ipBans = (HashMap<String,Ban>) stream.readObject();
			this.uuidBans = (HashMap<UUID,Ban>) stream.readObject();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setup.registerCommand(new BanCommand(new SerializeableMethod(main, getterName)));
	}

	public void removeBan(String name) {
		
		for (UUID u : new HashMap<>(this.uuidBans).keySet()) {
			OfflinePlayer p = Bukkit.getOfflinePlayer(u);
			if(p == null){this.uuidBans.remove(u); Bukkit.broadcastMessage(u.toString()); continue;}
			if(p.getName() == null){this.uuidBans.remove(u); Bukkit.broadcastMessage(u.toString()); continue;}
			if (p.getName().equals(name)) {
				this.removeBan(u);
				return;
			}
		}
	}

	public void removeBan(UUID uuid) {
		if (this.isBanned(uuid))
			this.uuidBans.remove(uuid);
	}

	public void removeIPBan(String ip) {
		if (this.isBanned(ip))
			this.ipBans.remove(ip);
	}

	public void save() {
		HandlerList.unregisterAll(this.banListener);
		try {
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("bans.bin"));
			stream.writeObject(this.ipBans);
			stream.flush();
			stream.writeObject(this.uuidBans);
			stream.flush();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
