package com.xxmicloxx.NoteBlockAPI;

import de.wolfi.utils.Priority;

public class MusicData {
	
	private SongPlayer sp;
	private Priority priority;
	
	public MusicData (SongPlayer sp, Priority priority) {
		this.sp = sp;
		this.priority = priority;
	}
	
	public SongPlayer getSongPlayer() {
		return sp;
	}
	
	public Priority getPriority() {
		return priority;
	}
	
}
