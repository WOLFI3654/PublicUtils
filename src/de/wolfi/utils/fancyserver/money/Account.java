package de.wolfi.utils.fancyserver.money;

import java.io.Serializable;
import java.util.UUID;

import de.wolfi.utils.fancyserver.MoneyManager;



public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9139407815878331728L;
	private int money;
	private int rc;
	private UUID uuid;

	public Account(UUID playerUUID, int money, int rc) {
		this.rc = rc;
		this.uuid = playerUUID;
		this.money = money;
		MoneyManager.accounts.add(this);
	}

	public int getMoney() {
		return money;
	}

	public int getRC() {
		return rc;
	}

	public UUID getUUID() {
		return uuid;
	}
	
	public boolean hasMoney(int needet){
		return needet <= money;
	}
	
	public boolean hasRC(int needet){
		return needet <= rc;
	}
	
	public void giveRC(int rc){
		this.rc+=rc;
	}
	
	public void giveMoney(int money){
		this.money+=money;
	}
	
	public boolean takeMoney(int amount){
		if(!hasMoney(amount)){
			return false;
		}
		money-=amount;
		return true;
	}
	
	public boolean takeRC(int amount){
		if(!hasRC(amount)){
			return false;
		}
		rc-=amount;
		return true;
	}
}