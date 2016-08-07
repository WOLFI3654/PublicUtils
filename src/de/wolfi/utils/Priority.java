package de.wolfi.utils;

public enum Priority {
	HIGHEST(5),
	HIGH(4),
	NORMAL(3),
	LOW(2),
	LOWEST(1);
	
	private int state;
	
	Priority(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
	
	
}
