package de.wolfi.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import de.wolfi.utils.timing.Sched;


public class TimingsHandler {

	private static ArrayList<Sched> schedList = new ArrayList<>();
	private static Timer timer = new Timer("BinaryTimingsHandlerSpigotPlugin");
	
	@SuppressWarnings("unchecked")
	public static void load(){
		try {
			ObjectInputStream input = new ObjectInputStream(new FileInputStream("scheds.bin"));
			Object obj = input.readObject();
			
			if(obj != null){
				if( obj instanceof ArrayList<?>) schedList = (ArrayList<Sched>) obj;
			}
			input.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setup();
		
	}
	
	private static void setup(){
		for(Sched s : new ArrayList<>(schedList)){
			Date d = s.getDate();
			if(d.before(Calendar.getInstance().getTime())){
				s.run();
				schedList.remove(s);
			}else{
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					s.run();
					
				}
			}, d);
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					schedList.remove(s);					
				}
			}, d.getTime()+1000);
		}
		}
	}
	
	public static void save(){
		try {
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("scheds.bin"));
			output.writeObject(schedList);
			
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		timer.cancel();
		
	}
	
	public static void addScheduler(Sched s){
		if(s.getDate().before(Calendar.getInstance().getTime())) {
			s.run();
			return;
		}
		schedList.add(s);
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				s.run();
				
			}
		}, s.getDate());
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				schedList.remove(s);			
			}
		}, s.getDate().getTime()+1000);
	}
}


