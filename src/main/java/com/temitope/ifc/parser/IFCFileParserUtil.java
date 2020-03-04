package com.temitope.ifc.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import com.temitope.ifc.common.IFCFileFilter;
import com.temitope.util.AppProperties;

public class IFCFileParserUtil {
//  private static final String SCAN_DIRECTORY = "./inDir";

	private static IFCFileParserUtil instance;
	private String fileName;
	public static final String COMPONENTS_HOUSE_WALL = "WALL";
	public static final String COMPONENTS_HOUSE_FLOOR = "FLOOR";
	public static final String MVD_LAYER_CHECK = "MVD Layer CHECK";
	public static final String ENABLE = "true";
	public static final String DISABLE = "false";
	public static final String PASSED = "PASSED";
	public static final String FAILED = "FAILED";

	private IFCFileParserUtil() {
		// intentionally created private constructor
	}

	public void parseFiles(String dir, IFCFileParser ifcFileParser) throws Exception {
		// Checks for all files within the directory that has a .ifc extension
		Set<String> directoryFiles;
		directoryFiles = scanForFiles(dir);

		if (directoryFiles.isEmpty()) {
			System.out.println("No files to parse");
		} else {
			processFiles(dir, directoryFiles, ifcFileParser);

		}
	}

	private HashSet<String> scanForFiles(String scanPath) throws Exception {
		File dir = new File(scanPath);
		// Validate Directory is valid
		if (!dir.isDirectory()) {
			System.out.println("Directory does not exists : " + scanPath);
			return null;
		}
		// Create extension Filter
		IFCFileFilter filter = new IFCFileFilter();
		// List all files that matches the filter
		String[] ifcFiles = dir.list(filter);
		if (ifcFiles == null) {
			return new HashSet<>();
		}
		return new HashSet<>(Arrays.asList(ifcFiles));
	}

	private void processFiles(String dir, Set<String> directoryFiles, IFCFileParser ifcFileParser) throws Exception {
		for (String file : directoryFiles) {
			StringBuilder sb = new StringBuilder(dir).append("/").append(file);

			// Reads the file and gets all the lines within the file
			List<String> fileLines = getFileLines(sb.toString());
			if (fileLines.isEmpty()) {
				System.out.println("File is empty, nothing to parse");
			} else {
				printProcessingFileName(file);
				ifcFileParser.parseFile(fileLines, file);
			}
		}
	}

	private void printProcessingFileName(String file) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("\t\tOutput of File" + file + "\t\t");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
	}

	private static List<String> getFileLines(String path) throws Exception {
		List<String> lines = new ArrayList<>();
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
			inputStream = new FileInputStream(path);
			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				lines.add(line);
			}
			// note that Scanner suppresses exceptions
			if (sc.ioException() != null) {
				throw sc.ioException();
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (sc != null) {
				sc.close();
			}
		}
		return lines;
	}

	public static String getScanDirectory() {
		return AppProperties.SCAN_DIRECTORY;
	}

	public static IFCFileParserUtil getInstance() {
		if (instance == null) {
			synchronized (IFCFileParserUtil.class) {
				if (instance == null) {
					instance = new IFCFileParserUtil();
				}
			}
		}
		return instance;

	}

	public static void printLayerStatus(String type, String layerMessage, boolean status) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		if (status) {
			System.out.println("\t" + type + ":\t" + layerMessage + " " + IFCFileParserUtil.PASSED);
		} else {
			System.out.println("\t" + type + ":\t" + layerMessage + " " + IFCFileParserUtil.FAILED);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

	}

	public final static class WALL {
		public static final String GypsumWallBoard = "Gypsum Wall Board";
		public static final String StructureWoodJoistRafterLayerBattInsulation = "Structure, Wood Joist/Rafter Layer, Batt Insulation";
		public static final String Stud = "Stud";
		public static final String Insulation = "Insulation";

	}

	public final static class FLOOR {
		public static final String Carpet1 = "Carpet (1)";
		public static final String Plywood = "Plywood";
		public static final String PlywoodSheathing = "Plywood, Sheathing";
		public static final String StructureWoodJoistRafterLayer = "Structure, Wood Joist/Rafter Layer";
		public static final String StructureWoodJoistRafterLayerBattInsulation = "Structure, Wood Joist/Rafter Layer, Batt Insulation";

	}

	public final static class Constants {
		public static final String IFC_WALL_STANDARD_CASE = "IfcWallStandardCase";
		public static final String IFC_SLAB = "IfcSlab";
		public static final String IFC_OPENING_ELEMENT = "IFCOPENINGELEMENT";
		public static final String IFC_MATERIAL_LAYER_SET = "IFCMATERIALLAYERSET";

		public static final String[] LAYER1_STEPA_MANDATORY_ENTITES = { IFC_WALL_STANDARD_CASE, IFC_SLAB,
				IFC_OPENING_ELEMENT, IFC_MATERIAL_LAYER_SET };

		public static final String[] LAYER1_STEPB_MANDATORY_ENTITES = { IFC_OPENING_ELEMENT, IFC_MATERIAL_LAYER_SET };

	}

}
