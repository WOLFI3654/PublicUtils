package de.wolfi.utils.fancyserver;

import java.util.ArrayList;
import java.util.UUID;

import de.wolfi.utils.fancyserver.money.Account;





public class MoneyManager {
	
	public static ArrayList<Account> accounts = new ArrayList<>();
	
	private final ConfigManager cfg;
	
	protected MoneyManager(ConfigManager cfg) {
		this.cfg = cfg;
	}

	@SuppressWarnings("unchecked")
	public void load() {
		Object o = cfg.get("MoneyBank");
		if (o instanceof ArrayList<?>) {
			MoneyManager.accounts = (ArrayList<Account>) o;
		}
		
	}

	public void save() {
		cfg.set("MoneyBank", MoneyManager.accounts);
	}
	
	public Account getPlayerAccount(UUID player){
		for(Account c : accounts){
			if(c.getUUID() == player){
				return c;
			}
		}
		return null;
	}

}