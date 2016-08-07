package de.wolfi.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

import org.bukkit.Bukkit;


public final class Reflection {

	private static String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) {
		try {
			return clazz.getConstructor(params);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Field getField(Class<?> clazz, String name) {
		try {

			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			for(Field f : clazz.getDeclaredFields()){
				System.out.println("Avalible: "+f.getName());
			}
			return null;
		}
	}

	public static Method getMethod(Class<?> clazz, String name, Class<?>... objects) {
		try {
			return clazz.getDeclaredMethod(name, objects);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Class<?> getNMSClass(String name) {
		// org.bukkit.craftbukkit.v1_8_R3...

		try {
			return Class.forName("net.minecraft.server." + Reflection.version + "." + name);
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Class<?> getOBCClass(String name) {
		// org.bukkit.craftbukkit.v1_8_R3...

		try {
			return Class.forName("org.bukkit.craftbukkit." + Reflection.version + "." + name);
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Constructor<?> getUnsafeConstructor(Class<?> clazz, int argsAmmount) {
		for (Constructor<?> m : clazz.getDeclaredConstructors()) {
			if (m.getParameterTypes().length == argsAmmount) {
				return m;
			}
		}
		return null;
	}

	public static Method getUnsafeMethod(Class<?> clazz, String name, Class<?> firstArg, int argsAmmount) {
		for (Method m : clazz.getMethods()) {
			if (m.getName().equals(name) && m.getParameterTypes().length == argsAmmount) {
				
				if (!firstArg.getName().equals(m.getParameterTypes()[0].getName()))
					continue;

				return m;
			}
		}
		return null;
	}

	public static Method getUnsafeMethod(Class<?> clazz, String name, int argsAmmount) {
		for (Method m : clazz.getMethods()) {
			if (m.getName().equals(name) && m.getParameterTypes().length == argsAmmount) {

				return m;
			}
		}
		return null;
	}
	
	public static Method getUnsafeMethod(Class<?> clazz, String name) {
		for (Method m : clazz.getMethods()) {
			if (m.getName().equals(name)) {

				return m;
			}
		}
		return null;
	}

	public static Object invokeField(Field f, Object main) {
		Object o = null;
		try {
			
			if (!f.isAccessible()) {

				f.setAccessible(true);
				o = f.get(main);
				f.setAccessible(false);
			} else {
				o = f.get(main);
			}
		} catch (Exception e) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot invoke Field", e);
			e.printStackTrace();
		}
		return o;
	}

	public static Object invokeMethod(Method m, Object main, Object... args) {
		try {
			return m.invoke(main, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Object newInstance(Constructor<?> c, Object... params) {
		try {
			return c.newInstance(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Deprecated
	public static void set(Field f, Object main, Object value) {
		try {
			if (!f.isAccessible()) {
				f.setAccessible(true);
				f.set(main, value);
				f.setAccessible(false);
			} else {
				f.set(main, value);
			}
		} catch (Exception e) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot set Field", e);
			e.printStackTrace();
		}
	}

	public static void setField(Object main, String field, Object obj) {
		Field f = getField(main.getClass(), field);
		boolean b = f.isAccessible();
		f.setAccessible(true);
		try {
			f.set(main, obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		f.setAccessible(b);
	}
}
