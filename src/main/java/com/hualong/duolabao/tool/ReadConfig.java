package com.hualong.duolabao.tool;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadConfig {
	
	public Properties prop=null;
	public ReadConfig() {
		
	}

	public Properties getprop() {
		Properties prop = new Properties();
		InputStream in = this.getClass().getResourceAsStream("/db.properties");
		try {
			prop.load(in);
			in.close();
			return prop;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Properties getprop(String string) {
		Properties prop = new Properties();
		InputStream in = this.getClass().getResourceAsStream(string);
		try {
			prop.load(in);
			in.close();
			return prop;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String arg[]) {
	//	System.out.println(new ReadConfig().getprop().getProperty("IP"));
		// new ReadConfig().getprop();
		
		//System.out.println("{\"resultStatus\":\"" + 0 + "\""+ "}");
		
	
		try {
			Properties pro = new Properties();
			pro.setProperty("force", "value1");
			pro.setProperty("key2", "value2");
			FileWriter fw = new FileWriter("/version.properties");
			pro.store(fw, "key-value");
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
