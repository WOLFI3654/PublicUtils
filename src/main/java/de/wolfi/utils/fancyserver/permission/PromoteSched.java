package de.wolfi.utils.fancyserver.permission;

import java.sql.Date;
import java.util.UUID;

import de.wolfi.utils.SerializeableMethod;
import de.wolfi.utils.fancyserver.PermissionManager;
import de.wolfi.utils.timing.Sched;


public class PromoteSched extends Sched{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2012122353787948814L;
	private UUID player;
	private String rank;
	private String oldRank;
	private final SerializeableMethod permissionManager;
	
	public PromoteSched(Date d,UUID player, String rank, SerializeableMethod perm) {
		super(d);
		this.permissionManager = perm;
		this.player = player;
		this.rank = rank;
		this.oldRank = ((PermissionManager) this.permissionManager.execute(null)).getPlayerRank(player).getName();
	}

	@Override
	public void run() {
		PermissionManager permissionManager = (PermissionManager) this.permissionManager.execute(null);
		if(permissionManager.getPlayerRank(player).getName().equals(oldRank)) permissionManager.promote(player, rank);
	}

}
