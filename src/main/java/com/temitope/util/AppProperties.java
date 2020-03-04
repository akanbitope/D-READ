package com.temitope.util;

import java.io.IOException;
import java.util.Properties;

import com.temitope.ifc.parser.IFCFileParser;

public class AppProperties {
	private static Properties properties;
	static {
		properties = new Properties();
		try {
			properties.load(AppProperties.class.getClassLoader().getResourceAsStream("app.properties"));
			System.out.println("loading the file from location : " + properties.getProperty("file_location"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static final String SCAN_DIRECTORY = properties.getProperty("file_location");
	public static final String MVD_Layer_Step1A_Check_Enable = properties.getProperty("MVD_Layer_Step1A_Check_Enable");
	public static final String MVD_Layer_Step1B_Check_Enable = properties.getProperty("MVD_Layer_Step1B_Check_Enable");
	public static final String OWL_FILE_NAME = properties.getProperty("owl_file_name");

}
