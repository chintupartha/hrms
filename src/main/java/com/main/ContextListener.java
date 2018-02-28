package com.main;

import java.io.File;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.PropertyConfigurator;

import com.util.CustomPropertyConfigurator;

@WebListener("application context listener")
public class ContextListener implements ServletContextListener{
	
	/**
     * Initialize log4j when the application is being started
     */
    public void contextInitialized(ServletContextEvent event) {
    	
    	ServletContext sContext = event.getServletContext();
        // initialize log4j here
        ServletContext context = event.getServletContext();
        String log4jConfigFile = context.getInitParameter("log4j-config-location");
        String log4jPath = context.getRealPath("") + File.separator + log4jConfigFile;
        
        String dbConfigFile = context.getInitParameter("db-config-location");
        String dbPath = context.getRealPath("") + File.separator + dbConfigFile;
        
        PropertyConfigurator.configure(log4jPath);
        CustomPropertyConfigurator configurator = new CustomPropertyConfigurator(); 
        Properties dbProps = configurator.getConfigProperties(dbPath);
        sContext.setAttribute("DbDetails", dbProps);
    }
     
    public void contextDestroyed(ServletContextEvent event) {
        // do nothing
    }
}
