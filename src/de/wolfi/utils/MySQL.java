package de.wolfi.utils;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;

import java.sql.Connection;

public class MySQL {

	private Connection connection;
	private final String server;
	private final int port;
	private final String user,passwd,db;
	
	static{
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Bukkit.getLogger().log(Level.WARNING, "Error while init MySQL", e);
		}
		
	}
	
	public MySQL(String server, int port,String user, String passwd, String database){
		this.server = server;
		this.port = port;
		this.user = user;
		this.passwd = passwd;
		this.db = database;
	}
	
	public Connection connect(){
		
		try{
			connection = DriverManager.getConnection("jdbc:mysql://" + this.server + ":" + this.port + "/" + this.db, this.user, this.passwd);
			return connection;
		} catch (SQLException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Could not connect to MySQL server! because: " + e.getMessage());
    	}
		return null;
	}
	
	   
	    public boolean checkConnection() {
	        return connection != null;
	    }

	    
	    public Connection preCheckConnection(){
	    	if(checkConnection()) return connection;
	    	return connect();
	    } 
	    
	    public Connection getConnection() {
	        return connection;
	    }
	
	public void closeConnection(){
			try {
				if(connection != null) if(!connection.isClosed()) connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	@Nullable
	 public ResultSet querySQL(String query) {
	        Connection c = preCheckConnection();

	        Statement s = null;

	        try {
	            s = c.createStatement();
	        } catch (SQLException e1) {
	            e1.printStackTrace();
	            return null;
	        }
	        
	        ResultSet ret = null;

	        try {
	            ret = s.executeQuery(query);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }


	        return ret;
	    }

	    public void updateSQL(String update) {

	        Connection c = preCheckConnection();

	        Statement s = null;

	        try {
	            s = c.createStatement();
	            s.executeUpdate(update);
	        } catch (SQLException e1) {
	            e1.printStackTrace();
	        }

	    }

}
