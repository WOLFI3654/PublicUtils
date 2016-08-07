package de.wolfi.utils.fancyserver.ban;

import java.io.Serializable;

public class Ban implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9045981037798961488L;

	private String m;

	public Ban(String banMessage){
		this.m = banMessage;
	}
	

	public String getMessage() {
		// TODO Auto-generated method stub
		return m;
	}

}
