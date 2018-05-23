package de.wolfi.utils.fancyserver.ban;

import java.sql.Date;
import java.util.UUID;
import java.util.logging.Level;

import de.wolfi.utils.SerializeableMethod;
import de.wolfi.utils.fancyserver.BanManager;
import de.wolfi.utils.timing.Sched;


public class BanSched extends Sched{

	/**
	 * 
	 */
	private final SerializeableMethod banManager;
	private static final long serialVersionUID = -5721279627907452816L;
	private UUID uuid;
	private String ip;
	
	public BanSched(Date d, UUID player, SerializeableMethod banManager) {
		super(d);
		this.banManager = banManager;
		this.uuid = player;
		
	}
	
	public BanSched(Date d, String ip, SerializeableMethod banManager) {
		super(d);
		this.banManager = banManager;
		this.ip = ip;
		
	}

	@Override
	public void run() {
		
			BanManager banManager = (BanManager) this.banManager.execute(null);
			Object banned = null;
			if(uuid != null){ banManager.removeBan(uuid);banned = uuid;}
			if(ip != null){ banManager.removeIPBan(ip);banned = ip;}
			
			BanManager.logger.log(Level.INFO, "Unbann Temporary Player "+banned);
		
	} 

	
	
}
