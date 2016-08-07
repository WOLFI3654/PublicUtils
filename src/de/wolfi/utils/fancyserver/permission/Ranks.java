package de.wolfi.utils.fancyserver.permission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;

import de.wolfi.utils.fancyserver.ConfigManager;
import de.wolfi.utils.fancyserver.PermissionManager;
public class Ranks implements Serializable {

	public static ArrayList<Ranks> ranks = new ArrayList<>();
	private static final long serialVersionUID = 3471085678541509169L;

	private static PermissionManager permissionManager;
	private static ConfigManager cfg;
	
	public static Ranks getRankByName(String name) {
		for (Ranks r : Ranks.ranks) {
			if (r.getName().equalsIgnoreCase(name)) {
				return r;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static void load(ConfigManager cfg, PermissionManager permissionManager) {
		Ranks.cfg = cfg;
		Ranks.permissionManager = permissionManager;
		Object o = cfg.get("Ranks");
		if (o instanceof ArrayList<?>) {
			Ranks.ranks = (ArrayList<Ranks>) o;
		}
	}

	public static void save() {
		cfg.set("Ranks", Ranks.ranks);
	}

	private char colorChar;
	private ArrayList<String> commands = new ArrayList<>();

	private String name;
	private ArrayList<UUID> players = new ArrayList<>();

	public Ranks(String name, char colorChar) {
		this.name = name;
		this.colorChar = colorChar;
		if (Ranks.ranks.size() == 0) {
			Ranks.cfg.set("defaultrank", name);
		}
		Ranks.ranks.add(this);

	}

	public void addCommand(String name) {
		if (!this.commands.contains(name))
			this.commands.add(name);
	}

	public void addPlayer(UUID uuid) {
		this.players.add(uuid);
	}

	public boolean containsCommand(String name) {
		return this.commands.contains(name);
	}

	public char getColorChar() {
		return this.colorChar;
	}
	
	protected void setColorChar(char c){
		this.colorChar = c;
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<String> getPlayersName() {
		ArrayList<String> s = new ArrayList<>();
		for (UUID u : this.players) {
			s.add(Bukkit.getOfflinePlayer(u).getName());
		}
		return s;
	}

	public ArrayList<UUID> getPlayersUUID() {
		return this.players;
	}
	
	public ArrayList<String> copyOfCommands(){
		return new ArrayList<>(commands);
	}

	public boolean isDefault() {
		return permissionManager.getDefaultRank() == this;
	}

	public boolean isPlayer(String name) {
		return this.getPlayersName().contains(name);
	}

	public boolean isPlayer(UUID uuid) {
		return this.getPlayersUUID().contains(uuid);
	}

	public void removeCommand(String name) {
		if (this.commands.contains(name))
			this.commands.remove(name);
	}

	public void removePlayer(UUID uuid) {
		this.players.remove(uuid);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "§7§l[§" + this.colorChar +"§l"+ this.name + "§7§l]§" + this.colorChar;
	}
}
