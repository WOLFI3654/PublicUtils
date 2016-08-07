package de.wolfi.utils.fancyserver.ban;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import de.wolfi.utils.fancyserver.BanManager;


public class BanListener implements Listener{


	private final BanManager banManager;
	public BanListener(BanManager manager) {
		this.banManager = manager;
	}
	
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=true)
	public void onPlayerPreLoginEvent(AsyncPlayerPreLoginEvent e){
		Ban ban = null;
		if(banManager.isBanned(e.getUniqueId())) ban = banManager.getUuidBan(e.getUniqueId());
		if(banManager.isBanned((e.getAddress().toString()+":").split(":")[0])) ban = banManager.getIpBan(e.getAddress().toString()); 
		if(ban != null){
			
			e.disallow(Result.KICK_BANNED, ban.getMessage());
		}
	}
}
