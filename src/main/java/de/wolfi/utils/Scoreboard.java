package de.wolfi.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public class Scoreboard {

	private final String name;
	private final Objective obj;
	private final org.bukkit.scoreboard.Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	public Scoreboard(String name, DisplaySlot slot){
		this.name = name;
		this.obj = scoreboard.registerNewObjective("values", "dummy");
		obj.setDisplaySlot(slot);
		obj.setDisplayName(this.name);
	}
	
	public void setName(String name){
		obj.setDisplayName(name);
	}
	
	public int getScore(String name){
		return obj.getScore(name).getScore();
	}
	
	public void addPlayer(Player player){
		player.setScoreboard(scoreboard);
	}
	public void removePlayer(Player player){
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}
	
	public void setScore(String name,int score){
		obj.getScore(name).setScore(score);
	}
	
	public void addScore(String name, int score){
		obj.getScore(name).setScore(getScore(name)+score);
	}
	
	public void removeScore(String name){
		scoreboard.resetScores(name);
	}
	
	public HashMap<Integer, String> ranks(){
		HashMap<Integer,String> values = new HashMap<>();
		for(String entry : scoreboard.getEntries()){
			int count = scoreboard.getEntries().size();
			for(String other : scoreboard.getEntries()){
				if(obj.getScore(entry).getScore() > obj.getScore(other).getScore()){
					count --;
				}
			}
			values.put(count, entry);
		}
		return values;

	}
	
	public String nameByRank(int rank){
		return ranks().get(new Integer(rank));
	}

	
	
	
}
