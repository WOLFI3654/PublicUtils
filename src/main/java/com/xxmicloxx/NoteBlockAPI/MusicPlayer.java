package com.xxmicloxx.NoteBlockAPI;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.wolfi.utils.Priority;
import de.wolfi.utils.UtilRegistry;



public class MusicPlayer {
	
	private static MusicPlayer instance = new MusicPlayer();
	
	public static MusicPlayer getInstance() {
		return instance;
	}
	
	public MusicPlayer() {
		players = new HashMap<String, MusicData>();
	}
	
	private HashMap<String, MusicData> players;
	
	public void addPlayer(Player p, SongPlayer sp, Priority prior) {
		if(sp == null) { return; }
		
		if(getMusicData(p) != null) {
			if(getMusicData(p).getPriority().getState() > prior.getState()) {  if(sp.getPlayerList().contains(p.getName())) { sp.removePlayer(p); } return; }
			removePlayer(p, true); 
		}
		
		if(!sp.getPlayerList().contains(p.getName())) {
			sp.addPlayer(p);
		}
		MusicData md = new MusicData(sp, prior);
		players.put(p.getName(), md);
	}
	
	public void removePlayer(Player p, boolean forcestop) {
		if(getMusicData(p) == null) { return; }
		
		getSongPlayer(p).removePlayer(p);
		players.remove(p.getName());
		
		if(!forcestop) {
			MusicPlayerRemoveEvent mpr = new MusicPlayerRemoveEvent(p);
			Bukkit.getPluginManager().callEvent(mpr);
		}
		
	}
	
	public void removePlayer(Player p) {
		removePlayer(p, false);
	}
	
	public SongPlayer getSongPlayer(Player p) {
		return players.get(p.getName()).getSongPlayer();
	}
	
	public MusicData getMusicData(Player p) {
		return players.get(p.getName());
	}
	
	public SongPlayer generateSongPlayer(String path, String file) {
		Song s = NBSDecoder.parse(new File(UtilRegistry.getPlugin().getDataFolder() + path, file));
		SongPlayer sp = new RadioSongPlayer(s);
		sp.setAutoDestroy(false);
		return sp;
	}

	public void stop() {
		for(String n : players.keySet()) {
			Player p = Bukkit.getPlayer(n);
			removePlayer(p, true);
		}
	}
	
}
