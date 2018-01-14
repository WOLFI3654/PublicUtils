package de.wolfi.utils;

import java.util.ArrayList;

public class Circle<T> {
	
	private ArrayList<T> list;
	
	public Circle() {
		list = new ArrayList<>();
	}

	public void add(T object) {
		list.add(object);	
	}
	
	public void remove(T object){
		list.remove(object);
	}
	
	public int indexOf(T object){
		return list.indexOf(object);
	}
	
	
	public T next(T object){
		
		int nextIndex = indexOf(object)+1;
		
		if(nextIndex == list.size()) {
			
			return list.get(0);
			
		} else {
			
			return list.get(indexOf(object)+1);
			
		}
		
	}
	
	public T previus(T object){
		
		return list.get(indexOf(object)-1);
	}

	public ArrayList<T> toList(){
		return list;
	}
	
}
