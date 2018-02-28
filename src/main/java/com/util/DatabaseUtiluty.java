package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class DatabaseUtiluty {
	
	static final Logger LOGGER = Logger.getLogger(DatabaseUtiluty.class); 
	private static DatabaseUtiluty dbIsntance;
    private static Connection con ;
    
    // private constructor 
    private DatabaseUtiluty() {
        
    }
    
    public static DatabaseUtiluty getInstance(){
    	
        if(dbIsntance==null){
            dbIsntance= new DatabaseUtiluty();
        }
        return dbIsntance;
    }
    
    public  Connection getConnection(){
	    if(con==null){
	        try {
	            String host = "jdbc:mysql://localhost:3306/hrms";
	            String username = "root";
	            String password = "admin";

	            con = DriverManager.getConnection( host, username, password );
	            LOGGER.info(DatabaseUtiluty.class.getName()+":"+Level.INFO+":"+"Connection created.");
	        } catch (SQLException ex) {
	        	LOGGER.info(DatabaseUtiluty.class.getName()+":"+Level.FATAL+":"+"Sql Exception happens "+ex);
	        }
	    }
	    return con;
    }
    
    //below is for JNDI related
    /*public  Connection getConnection(){
    	System.out.println("Connection creation started.");
    	InitialContext ctx;
		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:/hrms");
	    	try {
				con = ds.getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Connection creation ended.");
        return con;
    }*/
}
