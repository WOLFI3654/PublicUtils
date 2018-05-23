package de.wolfi.utils;

import org.bukkit.ChatColor;

public class Messages {
	public static String Prefix = "§8§l[§6§lMesto§f§lMC§8]§a ";
	public static final String BAN_FEEDBACK = Messages.Prefix + "§aDer Spieler §c§l%P%§a wurde erfolgreich gebannt.";

	public static final String BAN_USAGE = Messages.Prefix + "§a/ban §l" + ChatColor.ITALIC + "<playerName> [Grund...]\n"
			+ Messages.Prefix + "§a/tmpban §l" + ChatColor.ITALIC + "<playername> <time> [Grund...]\n" + Messages.Prefix
			+ "§a/kick §l" + ChatColor.ITALIC + "<player> <grund...>\n" + Messages.Prefix + "§a/mute §l"
			+ ChatColor.ITALIC + "<player>\n" + Messages.Prefix + "§a/tmpmute §l" + ChatColor.ITALIC
			+ "<player> <time>";

	public static final String KICKED ="\n"+Messages.Prefix+"\n§cDu wurdest vom Server gekickt!\n§7%R%";
	
	public static final String KICK_FEEDBACK = Messages.Prefix + "§aDer Spieler §c§l%P%§a wurde erfolgreich gekickt.";

	
	public static final String BANNED = "\n" + Messages.Prefix
			+ "\n§cDu bist vom Server gebannt. Wende dich an einen Owner/Admin, solltest du zu unrecht gebannt worden sein.\n§7%R%\n§8Ablauf: %DATE%";

	public static final String FLY_NOTFOUND = Messages.Prefix + "§cDer Spieler §4%PLAYER%§c ist nicht online!";

	public static final String FLY_OTHER = Messages.Prefix + "§aFly von §l%PLAYER%§r §aist nun §l%FLY%§7.";

	public static final String FLY_TOGGLE = Messages.Prefix + "§aFly wurde getoggelt. §7Fly§8: §l%FLY%§7.";

	public static final String GIVEALL = Messages.Prefix + "§aDu hast §l%ITEM%§r §aan alle verteilt.";

	public static final String GIVEALL_RECEIVE = Messages.Prefix
			+ "§aDu hast §l%ITEM%§r §avon §l%PLAYER%§r §abekommen.";

	public static final String GLOBAL_CHATAKTIVATION = Messages.Prefix + "§aDer Chat wurde von §c%P% §a aktiviert.";

	public final static String GLOBAL_CHATCLEAR = Messages.Prefix + "§aDer Chat wurde von §c%P% §agecleart.";

	public static final String GLOBAL_CHATDEAKTIVATION = Messages.Prefix + "§aDer Chat wurde von §c%P% §a deaktiviert.";

	public static final String GLOBAL_CHATTRY = Messages.Prefix
			+ "§cDu kannst nichts schreiben, da der Chat deaktiviert wurde!";

	public static final String GOD = Messages.Prefix + "§aGod wurde getoggelt. §7God§8: §l%GOD%§7.";

	public static final String INVALID_FORMAT = Messages.Prefix
			+ "§cDie Angabe §5§l%F%§c enthält ein falsches Format für §5§l%T%§c!";

	public static final String IPCHECK_WAIT = Messages.Prefix + "§aIp's werden überprüft. Bitte habe ein wenig geduld!";

	public static final String JUMPPAD_ADD = Messages.Prefix + "§aJumppad hinzugefügt.";

	public static final String JUMPPAD_REMOVE = Messages.Prefix + "§aJumppad in der Nähe von 0.9B wurde entfernt.";

	public static final String JUMPPAD_USAGE = Messages.Prefix + "§a/jumppad §l" + ChatColor.ITALIC + "add\n"
			+ Messages.Prefix + "§a/jumppad §l" + ChatColor.ITALIC + "remove";

	public static final String MAINTENANCE_KICK = "\n" + Messages.Prefix
			+ "\n§eZurzeit finden Wartungen statt.\n§c Bitte komme später wieder.";

	public static final String MAINTENANCE_TOGGLE = Messages.Prefix
			+ "§aWartungs Mode getogglet. §7Status§8: §l%STATUS%§7.";
	public static final String MONEY = Messages.Prefix + "§aDein Money: §6§l%MONEY%\n" + Messages.Prefix
			+ "§aDeine RC: §6§l%RC%";

	public static final String NOCOMMANDPERMISSIONS = Messages.Prefix + "§cDu hast nicht die Rechte für §l%cmd%§r §c!";

	public static final String PEEDI_CMDADD = Messages.Prefix + "§a§l%CMD% §awurde zu %GROUP%§a hinzugefügt.";

	public static final String PEEDI_CMDGROUPLIST = Messages.Prefix + "§a%GROUP%: §7%CMD%";

	public static final String PEEDI_CMDREMOVE = Messages.Prefix + "§a§l%CMD% §awurde von %GROUP%§a entfernt.";

	public static final String PEEDI_DEFAULTSET = Messages.Prefix + "§%CC%§l%NAME% §awurde als Default gesetzt.";

	public static final String PEEDI_GROUPADD = Messages.Prefix + "§%CC%§l%NAME% §awurde hinzugefügt.";

	public static final String PEEDI_GROUPALREADYEXIST = Messages.Prefix
			+ "§cDie Gruppe \"§c§l%NAME%\" §cexistiert schon!";

	public static final String PEEDI_GROUPLIST = Messages.Prefix + "§a%NAME%: §7[§%CC%%NAME%§7]";

	public static final String PEEDI_GROUPNOTEXIST = Messages.Prefix + "§cDie Gruppe \"§c§l%NAME%\" §cexistiert nicht!";

	public static final String PEEDI_USAGE = Messages.Prefix + "§a/peedi §l" + ChatColor.ITALIC + "user <playerName>\n"
			+ Messages.Prefix + "§a/peedi §l" + ChatColor.ITALIC + "group list\n" + Messages.Prefix + "§a/peedi §l"
			+ ChatColor.ITALIC + "group list <name>\n" + Messages.Prefix + "§a/peedi §l" + ChatColor.ITALIC
			+ "group addcmd <group> <cmd>\n" + Messages.Prefix + "§a/peedi §l" + ChatColor.ITALIC
			+ "group removecmd <group> <cmd>\n" + Messages.Prefix + "§a/peedi §l" + ChatColor.ITALIC
			+ "group add <name> <colornumber>\n" + Messages.Prefix + "§a/peedi §l" + ChatColor.ITALIC
			+ "setdefault <groupname>\n" + Messages.Prefix + "§a/peedi §l" + ChatColor.ITALIC
			+ "promote <onlinePlayer> <groupname>\n" + Messages.Prefix + "§a/peedi §l" + ChatColor.ITALIC
			+ "tmppromote <onlinePlayer> <groupname> <date> <backgroup>";

	public static final String PEEDI_USERLIST = Messages.Prefix + "§a%NAME%: §7%GROUP%";
	public static final String PEEDI_USERNEVERONLINE = Messages.Prefix + "§c§l%NAME% §cwar niemals auf dem Server!";
	public static final String PEEDI_USERPROMOTE = Messages.Prefix + "§aDu bist nun in der Gruppe §7[§%CC%%NAME%§7]§a.";
	public static final String PEEDI_USERPROMOTEFEEDBACK = Messages.Prefix
			+ "§a%USER% ist nun ein §7[§%CC%%NAME%§7]§a.";
	public static final String PING = Messages.Prefix + "§eDein Ping: §a§l%P%";
	
	public static final String SURVEY = Messages.Prefix + "§a-------§9Umfrage§a-------\n" + Messages.Prefix
			+ "§3§l%TEXT%\n" + Messages.Prefix + "\n" + Messages.Prefix + "§r§a/Ja\n" + Messages.Prefix + "§c/Nein\n"
			+ Messages.Prefix + "\n" + Messages.Prefix + "§860 Sekunden bis zum Umfrageschluss!§r\n" + Messages.Prefix
			+ "§a-------§9Umfrage§a-------";
	public static final String SURVEY_ALREADY = Messages.Prefix + "§cDu hast schon abgestimmt!";;
	public static final String SURVEY_NO = Messages.Prefix + "§cDu hast für Nein gestimmt.";;
	public static final String SURVEY_YES = Messages.Prefix + "§aDu hast für Ja gestimmt.";;

	public static final String SURVEYEND = Messages.Prefix + "§a-------§9Umfrage§a-------\n" + Messages.Prefix
			+ "§3§l%TEXT%\n" + Messages.Prefix + "\n" + Messages.Prefix + "§r§aJa: §l%YES%§r\n" + Messages.Prefix
			+ "§cNein: §l%NO%\n" + Messages.Prefix + "§r\n" + Messages.Prefix + "§8Umfrage beendet!§r\n"
			+ Messages.Prefix + "§a-------§9Umfrage§a-------";

	public static final String SURVEYUSAGE = Messages.Prefix + "§a/umfrage §l" + ChatColor.ITALIC + "Text";

	public static final String UNKNOWCOMMAND = Messages.Prefix + "§cDieser Befehl existiert nicht!";

	public static final String USERNOTONLINE = Messages.Prefix + "§c§l%NAME% §cist nicht auf dem Server!";

	public static final String VANISH = Messages.Prefix + "§aVanish wurde getoggelt. §7Vanish§8: §l%V%§7.";

	public static final String VOTE = "[\"\",{\"text\":\"" + Messages.Prefix
			+ "§6Vote \",\"color\":\"gold\"},{\"text\":\"jeden Tag, um tolle Sachen zu bekommen.\n \",\"color\":\"green\"},{\"text\":\"Um diese Tollen Sachen zu bekommen, Klicke bitte \",\"color\":\"green\"},{\"text\":\">>HIER<<\",\"color\":\"dark_aqua\",\"bold\":true,\"underlined\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%SITE%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"%SITE%\",\"color\":\"gold\"},{\"text\":\"\",\"color\":\"gold\"}]}}}]";

	public static final String BAN_FEEDBACK_UNBAN = Messages.Prefix + "§aDer Spieler §c§l%P%§a wurde erfolgreich unbannt.";


}
