package de.wolfi.utils.fancyserver.permission;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import de.wolfi.utils.Messages;
import de.wolfi.utils.fancyserver.PermissionManager;
import de.wolfi.utils.fancyserver.PluginSetuper;


public class PermissionListener implements Listener {

	private final PermissionManager permissionManager;
	private final PluginSetuper setup;
	
	public PermissionListener(PermissionManager pm, PluginSetuper set) {
		this.permissionManager = pm;
		this.setup = set;
	}
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setFormat(e.getPlayer().getDisplayName() + " §2>>> §"
				+ permissionManager.getPlayerRank(e.getPlayer().getUniqueId()).getColorChar() + e.getMessage());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onExecute(PlayerCommandPreprocessEvent e) {

		if (!setup.commandExist(e.getMessage().split(" ")[0].replaceFirst("/", ""))) {
			e.getPlayer().sendMessage(Messages.UNKNOWCOMMAND);
			e.setCancelled(true);
			return;
		}
		if (!permissionManager.canExecute((e.getMessage() + " ").split(" ")[0], e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(Messages.NOCOMMANDPERMISSIONS.replaceAll("%cmd%", e.getMessage()));
		}
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if (permissionManager.getPlayerRank(e.getPlayer().getUniqueId()) == null) {
			permissionManager.getDefaultRank().addPlayer(e.getPlayer().getUniqueId());
			Bukkit.broadcastMessage(Messages.Prefix+"§eWillkommen "+e.getPlayer().getName()+" auf MestoMC!");
		}
		permissionManager.updateNames(e.getPlayer());

	}
}
