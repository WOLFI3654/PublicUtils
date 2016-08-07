package de.wolfi.utils;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

public class SerializeableMethod implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8612137899994585204L;

	private final Class<?> clazz;
	private final Class<?>[] params;
	private final String method;
	
	public SerializeableMethod(Method m) {
		this.clazz = m.getDeclaringClass();
		this.method = m.getName();
		this.params = m.getParameterTypes();
	}
	
	public SerializeableMethod(Class<?> clazz, String method) {
		this.clazz = clazz;
		this.method = method;
		this.params = Reflection.getUnsafeMethod(clazz, method).getParameterTypes();
	}
	
	private Method convert(){
		return Reflection.getMethod(clazz, method, params);
	}
	
	public Object execute(@Nullable Object owner,@Nullable Object...objects){
		return Reflection.invokeMethod(convert(), owner, objects);
	}
}
