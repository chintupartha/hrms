package com.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

public class CustomPropertyConfigurator {

	Properties props = new Properties();
	OutputStream output = null;

	@SuppressWarnings("unused")
	public Properties getConfigProperties(String filePath){
		FileInputStream istream = null;
		try {
			String configfileName = filePath;
			istream = new FileInputStream(filePath);
			if(istream != null){
				props.load(istream);
				istream.close();
			}else{
				throw new FileNotFoundException();
			}
			
			Enumeration enuKeys = props.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = props.getProperty(key);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return props;
	}
}
