package de.wolfi.utils.fancyserver.permission;

import java.sql.Date;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.wolfi.utils.Messages;
import de.wolfi.utils.SerializeableMethod;
import de.wolfi.utils.TimeUtil;
import de.wolfi.utils.TimingsHandler;
import de.wolfi.utils.fancyserver.PermissionManager;
import de.wolfi.utils.fancyserver.PluginSetuper;



public class PermissionEditor extends Command {

	private final PermissionManager perm;
	private final SerializeableMethod permissionGetter;
	private final PluginSetuper setup;
	public PermissionEditor(SerializeableMethod perm, PluginSetuper setup) {
		super("peedi");
		this.permissionGetter = perm;
		this.setup = setup;
		this.perm = (PermissionManager) this.permissionGetter.execute(null);
	}

	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
		if (arg0 instanceof Player) {
			switch (arg2.length) {
			case 2:
				if (arg2[0].equalsIgnoreCase("setdefault")) {
					Ranks r = Ranks.getRankByName(arg2[1]);
					if (r == null) {
						arg0.sendMessage(Messages.PEEDI_GROUPNOTEXIST.replaceAll("%NAME%", arg2[0]));
						break;
					}
					perm.setDefaultRank(r);
					arg0.sendMessage(Messages.PEEDI_DEFAULTSET);
					break;
				}
				if (arg2[0].equalsIgnoreCase("group") && arg2[1].equalsIgnoreCase("list")) {
					for (Ranks r : Ranks.ranks) {
						arg0.sendMessage(Messages.PEEDI_GROUPLIST.replaceAll("%NAME%", r.getName()).replaceAll("%CC%",
								String.valueOf(r.getColorChar())) + (r.isDefault() ? " §9(Default)" : ""));
					}
					break;
				}
				if (arg2[0].equalsIgnoreCase("user")) {
					Ranks r = perm.getPlayerRank(arg2[1]);
					if (r == null) {
						arg0.sendMessage(Messages.PEEDI_USERNEVERONLINE.replaceAll("%NAME%", arg2[1]));
						break;
					}
					arg0.sendMessage(
							Messages.PEEDI_USERLIST.replaceAll("%NAME%", arg2[1]).replaceAll("%GROUP%", r.getName()));
					break;
				}
				arg0.sendMessage(Messages.PEEDI_USAGE);
				break;
			case 3:
				if (arg2[0].equalsIgnoreCase("promote")) {
					String player = arg2[1];
					Player p = Bukkit.getPlayerExact(player);
					if (p == null) {
						arg0.sendMessage(Messages.USERNOTONLINE.replaceAll("%NAME%", player));
						break;
					}
					Ranks r = Ranks.getRankByName(arg2[2]);
					if (r == null) {
						arg0.sendMessage(Messages.PEEDI_GROUPNOTEXIST.replaceAll("%NAME%", arg2[2]));
						break;
					}
					perm.promote(p.getUniqueId(), r);
					arg0.sendMessage(Messages.PEEDI_USERPROMOTEFEEDBACK.replaceAll("%USER%", p.getName())
							.replaceAll("%NAME%", r.getName()).replaceAll("%CC%", String.valueOf(r.getColorChar())));
					p.sendMessage(Messages.PEEDI_USERPROMOTE.replaceAll("%NAME%", r.getName()).replaceAll("%CC%",
							String.valueOf(r.getColorChar())));
					p.playSound(p.getLocation(), Sound.WITHER_DEATH, 1.0F, 2.0F);
					
					break;
				}
				if(arg2[0].equalsIgnoreCase("group") && arg2[1].equalsIgnoreCase("list")){
					Ranks r = Ranks.getRankByName(arg2[2]);
					if(r == null){
						arg0.sendMessage(Messages.PEEDI_GROUPNOTEXIST.replaceAll("%NAME%", arg2[2]));
						break;
					}
					for(String s : r.copyOfCommands()){
						arg0.sendMessage(Messages.PEEDI_CMDGROUPLIST.replaceAll("%GROUP%", r.getName()).replaceAll("%CMD%", s));
					}
					break;
				}
				if(arg2[0].equalsIgnoreCase("group") && arg2[1].equalsIgnoreCase("remove")){
					Ranks r = Ranks.getRankByName(arg2[2]);
					if(r == null){
						arg0.sendMessage(Messages.PEEDI_GROUPNOTEXIST.replaceAll("%NAME%", arg2[2]));
						break;
					}
					Ranks.ranks.remove(r);
					Ranks.save();
				}
				
				if(arg2[0].equalsIgnoreCase("setcolorchar")){
					Ranks r = Ranks.getRankByName(arg2[1]);
					if(r == null){
						arg0.sendMessage(Messages.PEEDI_GROUPNOTEXIST.replaceAll("%NAME%", arg2[2]));
						break;
					}
					r.setColorChar(arg2[2].charAt(0));
					arg0.sendMessage(Messages.Prefix+"Color geändert!");
					Ranks.save();
					break;
				}
			
				arg0.sendMessage(Messages.PEEDI_USAGE);
				break;
			case 4:
			
				if (arg2[0].equalsIgnoreCase("group") && arg2[1].equalsIgnoreCase("add")) {
					String name = arg2[2];
					char color = arg2[3].charAt(0);
					if (Ranks.getRankByName(name) != null) {
						arg0.sendMessage(Messages.PEEDI_GROUPALREADYEXIST.replaceAll("%NAME%", name));
						break;
					}
					new Ranks(name, color);
					arg0.sendMessage(Messages.PEEDI_GROUPADD.replaceAll("%CC%", String.valueOf(color))
							.replaceAll("%NAME%", name));
					break;
				}
				if(arg2[0].equalsIgnoreCase("group") && arg2[1].equalsIgnoreCase("addcmd")){
					Ranks r = Ranks.getRankByName(arg2[2]);
					if(r == null){
						arg0.sendMessage(Messages.PEEDI_GROUPNOTEXIST.replaceAll("%NAME%", arg2[2]));
						break;
					}
					if (!setup.commandExist(arg2[3].replaceFirst("/", "")) || !arg2[3].startsWith("/")) {
						arg0.sendMessage(Messages.UNKNOWCOMMAND);
						break;
					}
					r.addCommand(arg2[3]);
					arg0.sendMessage(Messages.PEEDI_CMDADD.replaceAll("%GROUP%", r.toString()).replaceAll("%CMD%", arg2[3]));
					break;
				}
				if(arg2[0].equalsIgnoreCase("group") && arg2[1].equalsIgnoreCase("removecmd")){
					Ranks r = Ranks.getRankByName(arg2[2]);
					if(r == null){
						arg0.sendMessage(Messages.PEEDI_GROUPNOTEXIST.replaceAll("%NAME%", arg2[2]));
						break;
					}
					if (!setup.commandExist(arg2[3].replaceFirst("/", "")) || !arg2[3].startsWith("/")) {
						arg0.sendMessage(Messages.UNKNOWCOMMAND);
						break;
					}
					r.removeCommand(arg2[3]);
					arg0.sendMessage(Messages.PEEDI_CMDREMOVE.replaceAll("%GROUP%", r.toString()).replaceAll("%CMD%", arg2[3]));
					break;
				}
				arg0.sendMessage(Messages.PEEDI_USAGE);
				break;
			case 5:
				if(arg2[0].equalsIgnoreCase("tmppromote")){
					String player = arg2[1];
					Player p = Bukkit.getPlayerExact(player);
					if (p == null) {
						arg0.sendMessage(Messages.USERNOTONLINE.replaceAll("%NAME%", player));
						break;
					}
					Ranks r = Ranks.getRankByName(arg2[2]);
					if (r == null) {
						arg0.sendMessage(Messages.PEEDI_GROUPNOTEXIST.replaceAll("%NAME%", arg2[2]));
						break;
					}
					Date d;
					try{
					d = new Date(TimeUtil.fromTime(arg2[3]));
					}catch(Exception e){
						arg0.sendMessage(Messages.INVALID_FORMAT.replaceAll("%F%",arg2[3]).replaceAll("%T%","Date")+e.getMessage());
						break;
					}
					Ranks bck = Ranks.getRankByName(arg2[4]);
					if (bck == null) {
						arg0.sendMessage(Messages.PEEDI_GROUPNOTEXIST.replaceAll("%NAME%", arg2[4]));
						break;
					}
					TimingsHandler.addScheduler(new PromoteSched(d, p.getUniqueId(), bck.getName(),permissionGetter));
					perm.promote(p.getUniqueId(), r);
					arg0.sendMessage(Messages.PEEDI_USERPROMOTEFEEDBACK.replaceAll("%USER%", p.getName())
							.replaceAll("%NAME%", r.getName()).replaceAll("%CC%", String.valueOf(r.getColorChar())));
					p.sendMessage(Messages.PEEDI_USERPROMOTE.replaceAll("%NAME%", r.getName()).replaceAll("%CC%",
							String.valueOf(r.getColorChar())));
					p.playSound(p.getLocation(), Sound.WITHER_DEATH, 1.0F, 2.0F);
					System.out.println(d.toString());
					break;
				}
				arg0.sendMessage(Messages.PEEDI_USAGE);
				break;
			default:
				arg0.sendMessage(Messages.PEEDI_USAGE);
				break;
			}
		}
		return true;
	}

}
