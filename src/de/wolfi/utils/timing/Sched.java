package de.wolfi.utils.timing;

import java.io.Serializable;
import java.sql.Date;

public abstract class Sched implements Serializable, Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6740285685740164363L;


	public Date getDate() {
		return date;
		
	}
	
	private Date date;
	

	public Sched(Date d) {
		this.date = d;
		
		
	}

	
	
	
}