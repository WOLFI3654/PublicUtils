package de.wolfi.utils.fancyserver.ban;

import java.sql.Date;
import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.wolfi.utils.Messages;
import de.wolfi.utils.SerializeableMethod;
import de.wolfi.utils.TimeUtil;
import de.wolfi.utils.TimingsHandler;
import de.wolfi.utils.fancyserver.BanManager;


public class BanCommand extends Command{

	
	
	
	
	
	private final SerializeableMethod banManagerGet;
	private final BanManager banManager;
	public BanCommand(SerializeableMethod manager) {
		super("banmanager");
		this.banManagerGet = manager;
		this.banManager = (BanManager) this.banManagerGet.execute(null);
		setAliases(Arrays.asList("ban","tmpban","unban","mute","tmpmute","kick","unmute","ipban"));
	}

	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
		switch (arg1) {
		case "ban":
			if(arg2.length >= 1){
				Player p = Bukkit.getPlayer(arg2[0]);
				if(p == null){
					arg0.sendMessage(Messages.USERNOTONLINE.replaceAll("%NAME%", arg2[0]));
					break;
				}
				String reason ="Grundlos";
				if(arg2.length >= 2){
					reason = arg2[1];
					for(int i = 2; i < arg2.length; i++){
						reason +=" "+arg2[i];
					}
				}
				p.damage(50000);
				p.setHealth(0);
				banManager.addBan(p.getUniqueId(),new Ban(Messages.BANNED.replaceAll("%R%", reason).replaceAll("%DATE%", "Unendlich")));
				broadcast(p.getName(), "Gebannt", reason, arg0.getName(), "Ablauf: Permanent");
				arg0.sendMessage(Messages.BAN_FEEDBACK.replaceAll("%P%", p.getName()));
			}else{
				arg0.sendMessage(Messages.BAN_USAGE);
			}
			break;
		case "ipban":
			if(arg2.length >= 1){
				Player p = Bukkit.getPlayer(arg2[0]);
				if(p == null){
					arg0.sendMessage(Messages.USERNOTONLINE.replaceAll("%NAME%", arg2[0]));
					break;
				}
				String reason ="Grundlos";
				if(arg2.length >= 2){
					reason = arg2[1];
					for(int i = 2; i < arg2.length; i++){
						reason +=" "+arg2[i];
					}
				}
				p.damage(50000);
				p.setHealth(0);
				banManager.addIPBan((p.getAddress().toString()+":").split(":")[0],new Ban(Messages.BANNED.replaceAll("%R%", reason).replaceAll("%DATE%", "Unendlich")));
				broadcast(p.getName(), "IPbannt", reason, arg0.getName(), "Ablauf: Permanent");
				arg0.sendMessage(Messages.BAN_FEEDBACK.replaceAll("%P%", p.getName()));
			}else{
				arg0.sendMessage(Messages.BAN_USAGE);
			}
			break;
		case "unban":
			if(arg2.length == 1){
				banManager.removeBan(arg2[0]);
				broadcast(arg2[0], "Entsperrt", "Entsperrung", arg0.getName());
				arg0.sendMessage(Messages.BAN_FEEDBACK_UNBAN.replaceAll("%P%", arg2[0]));
			}else{
				arg0.sendMessage(Messages.BAN_USAGE);
			}
			break;
		case "tmpban":
			if(arg2.length >= 2){
				String player = arg2[0];
				Player p = Bukkit.getPlayerExact(player);
				if (p == null) {
					arg0.sendMessage(Messages.USERNOTONLINE.replaceAll("%NAME%", player));
					break;
				}
				Date d;
				try{
				d = new Date(TimeUtil.fromTime(arg2[1]));
				}catch(Exception e){
					arg0.sendMessage(Messages.INVALID_FORMAT.replaceAll("%F%",arg2[1]).replaceAll("%T%","Date")+e.getMessage());
					break;
				}
				String reason ="Grundlos";
				if(arg2.length >= 3){
					reason = arg2[2];
					for(int i = 3; i < arg2.length; i++){
						reason +=" "+arg2[i];
					}
				}
				
				banManager.addBan(p.getUniqueId(), new Ban(Messages.BANNED.replaceAll("%R%", reason).replaceAll("%DATE%", d.toString())));
				TimingsHandler.addScheduler(new BanSched(d, p.getUniqueId(), banManagerGet));
				broadcast(p.getName(), "TMPbannt", reason, arg0.getName(), "Ablauf: "+d.toString());
				arg0.sendMessage(Messages.BAN_FEEDBACK.replaceAll("%P%", p.getName()));
				
			}else{
				arg0.sendMessage(Messages.BAN_USAGE);
			}
			break;
		case "kick":
			if(arg2.length >= 1){
				Player p = Bukkit.getPlayer(arg2[0]);
				if(p == null){
					arg0.sendMessage(Messages.USERNOTONLINE.replaceAll("%NAME%", arg2[0]));
					break;
				}
				String reason ="Grundlos";
				if(arg2.length >= 2){
					reason = arg2[1];
					for(int i = 2; i < arg2.length; i++){
						reason +=" "+arg2[i];
					}
				}
				
				p.kickPlayer(Messages.KICKED.replaceAll("%R%", reason));
				broadcast(p.getName(), "Gekickt", reason, arg0.getName());
				arg0.sendMessage(Messages.BAN_FEEDBACK.replaceAll("%P%", p.getName()));
			}else{
				arg0.sendMessage(Messages.BAN_USAGE);
			}
			break;
		case "mute":
			
			break;
		case "unmute":
			
			break;
		case "tmpmute":
			
			break;
		default:
			arg0.sendMessage(Messages.BAN_USAGE);
			break;
		}
		return true;
	}
	
	private void broadcast(String kicked,String msg, String reason, String owned,String... info){
		Bukkit.broadcastMessage("§6*****************************");
		Bukkit.broadcastMessage("§aDer Spieler §c§l"+kicked+"§a wurde vom Server §c§l"+msg);
		Bukkit.broadcastMessage("§aGrund: §c§l"+reason);
		Bukkit.broadcastMessage("§a"+msg+" von: §c§l"+owned);
		Bukkit.broadcastMessage("§aZusätzliche Informationen: ");
		for(String i : info){
			Bukkit.broadcastMessage("§c "+i);
		}
		Bukkit.broadcastMessage("§6*****************************");
		BanManager.logger.log(Level.INFO,kicked+" von "+owned+" "+msg+": "+reason+"... "+Arrays.asList(info));
	}

}
