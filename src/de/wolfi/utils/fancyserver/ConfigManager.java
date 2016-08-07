package de.wolfi.utils.fancyserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class ConfigManager {

	private final File cfgFile;
	private HashMap<String, Serializable> iCfg = new HashMap<>();

	public ConfigManager(File cfgFile){
		this.cfgFile = cfgFile;
		if (cfgFile.exists()) {
			this.load(cfgFile);
		}
	}

	public Serializable get(String key) {
		Serializable s = this.iCfg.get(key);
		if (s != null) {
			return s;
		} else {
			return new Serializable() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
			};
		}

	}

	@SuppressWarnings("unchecked")
	public void load(File file) {
		try {
			ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
			Object read = stream.readObject();
			if (read instanceof HashMap<?, ?>) {

				this.iCfg = (HashMap<String, Serializable>) read;
			}
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(cfgFile));
			cfgFile.createNewFile();
			stream.writeObject(this.iCfg);
			stream.flush();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void set(String key, Serializable value) {
		this.iCfg.put(key, value);
	}

}
