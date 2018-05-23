package de.wolfi.utils;

import org.bukkit.entity.Player;

public abstract class TimedPlayerMessage implements Runnable {
	String[] message;
	Player p;
	private String pre;

	int time;

	public TimedPlayerMessage(Player p, String prefix, String[] message, int time) {
		this.p = p;
		this.pre = prefix;

		this.message = message;
		this.time = time;
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		for (String message : this.message) {
			this.p.sendMessage(this.pre + message);
			try {
				Thread.sleep(this.time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.runLast();
	}

	public abstract void runLast();

}
