package com.temitope.ifc.executor;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.regex.MatchResult;

import com.github.jsonldjava.utils.Obj;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.temitope.ifc.catalogue.ProductsCatalogue;
import com.temitope.ifc.common.EntityHolderHelper;
import com.temitope.ifc.common.IFCConstants;
import com.temitope.ifc.common.KMConstants;
import com.temitope.ifc.common.ProductSearch;
import com.temitope.ifc.mvdlayer.MVDLayerStep1BCheck;
import com.temitope.ifc.parser.IFCFileParserUtil;
import com.temitope.ifc.processors.IFCProcessor;
import com.temitope.model.Material;
import com.temitope.owl.model.WallMaterial;

public class StandardWallExecutor  {
	private List<String> fileLines = new ArrayList<>();
	private LinkedHashSet<String> entities = new LinkedHashSet<>();
	private Map<String, String> productCatalogueMap = new HashMap<>();
	private LinkedHashMap<String, LinkedHashSet<String>> thicknessMap = new LinkedHashMap();
	private HashMap<String, String> fileHash = new HashMap<>();
	private LinkedHashSet<String> gypsumThicknessSet = new LinkedHashSet<String>();
	private LinkedHashSet<String> InsulationThicknessSet = new LinkedHashSet<String>();
	private LinkedHashSet<String> StudThicknessSet = new LinkedHashSet<String>();
	private String height;
	private final String IFC_WALL_STANDARD_CASE = "IFCWALLSTANDARDCASE";
	private final String IFC_ARBITRARY_CLOSED_PROFILE_DEF = "IFCARBITRARYCLOSEDPRFILEDEF";
	private final String IFC_RECTANGLE_PROFILE_DEF = "IFCRECTANGLEPROFILEDEF";
	private final String IFC_CIRCLE = "IFCCIRCLE";
	private final String IFC_OPENING_ELEMENT = "IFCOPENINGELEMENT";
	private String startingLine = null;
	private String startingLineI = null;
	private String startingLineII = null;
	private String startingLineIII = null;
	private String IFC_MATERIAL_LAYER_SET = "IFCMATERIALLAYERSET(";
	private ArrayList<String> myLineI = new ArrayList<>();
	private ArrayList<String> myLineII = new ArrayList<>();
	private ArrayList<String> myLineIII = new ArrayList<>();
	private static Map<Integer, ProductsCatalogue> productsCatalogueMap = new HashMap<Integer, ProductsCatalogue>();
	private static ProductsCatalogue prod, prod1, prod2, prod3, prod4;
	private ArrayList<String> products = new ArrayList<>();
	private WallMaterial wallMaterial;
	static {
		prod = new ProductsCatalogue(IFCFileParserUtil.WALL.GypsumWallBoard, "0.0520833333333333", 1.63);
		prod1 = new ProductsCatalogue(IFCFileParserUtil.WALL.GypsumWallBoard, "0.03125", 1.97);
		prod2 = new ProductsCatalogue(IFCFileParserUtil.WALL.GypsumWallBoard, "0.0416666666666667", 1.99);
		prod4 = new ProductsCatalogue(IFCFileParserUtil.WALL.GypsumWallBoard, "0.020833333333333", 2.66);
		prod3 = new ProductsCatalogue(IFCFileParserUtil.WALL.StructureWoodJoistRafterLayerBattInsulation,
				"0.208333333333333", 14.61);
		ProductsCatalogue[] productsCatalogues = { prod, prod1, prod2, prod3, prod4 };
		for (int i = 0; i < productsCatalogues.length; i++) {
			productsCatalogueMap.put(i, productsCatalogues[i]);
		}

	}

	public StandardWallExecutor(List<String> fileLines) {
		this.fileLines = fileLines;
	}

	public void setFileLines(List<String> fileLines) {
		this.fileLines = fileLines;
	}

	public void execute() {
	 wallMaterial = new WallMaterial();
		if (startingLine == null) {
			return;
		}

		String a = "";
		String b = "";
		String c = "";
		String d = "";
		String l = "";
		Double e;
		Double f;
		Double g;
		Double h;
		Double k;
		Double j;
		String m = "";
		String n = "";
		Double area = 0.0;
		ArrayList<Double> sum = new ArrayList<Double>();
		Double temp = 0.0;
		ArrayList<Double> sumofvol = new ArrayList<Double>();
		Double temp1 = 0.0;
		Double NetArea;
		ArrayList<Double> costsum = new ArrayList<Double>();
		Double cos;
		Double temp1a = 0.0;

		ArrayList<Double> netArea = new ArrayList<Double>();

		String firstAttribute = fileHash.get(startingLine);
		String secondAttribute = getDataAtIndex(firstAttribute, 6);
		if (secondAttribute != null) {
			String thirdAttribute = getDataAtIndex(fileHash.get(secondAttribute), 3);
			String fourthAttribute = fileHash.get(thirdAttribute);

			if (fourthAttribute.contains("SweptSolid")) {
				String fifthAttribute = fileHash.get(getDataAtIndex(fourthAttribute, 3));
				String height = getDataAtIndex(fifthAttribute, 3);
				String sixthAttribute = fileHash.get(getDataAtIndexII(fifthAttribute, 1));
				if (sixthAttribute.contains("IFCRECTANGLEPROFILEDEF")) {
					String length = getDataAtIndex(sixthAttribute, 3);
					String width = getDataAtIndex(sixthAttribute, 4);

					System.out.println("Height of wall is:\t\t " + height);
					wallMaterial.setHeightOfWall(height);
					setHeight(height);
					System.out.println("Length of Wall is:\t\t " + length);
					wallMaterial.setLengthOfWall(length);
					System.out.println("Width of Wall is:\t\t " + width);
					wallMaterial.setWidthOfWall(width);

					Double testc = Double.valueOf(height);
					Double teste = Double.valueOf(length);
					Double testf = Double.valueOf(width);

					Double AreaofWall = testc * teste;
					System.out.println("Area of Wall is:\t\t " + AreaofWall);
					wallMaterial.setAreaOfWall(String.valueOf(AreaofWall));

					Double VolumeofWall = testc * teste * testf;
					System.out.println("Volume of Wall is:\t\t " + VolumeofWall);
					wallMaterial.setVolumeOfWall(String.valueOf(VolumeofWall));

					String[] OpeningElement = new String[myLineII.size()];
					for (int i = 0; i < myLineII.size(); i++) {

						String hundredAttribute = getDataAtIndex((myLineII.get(i)), 6);
						String hundredoneAttribute = fileHash.get(hundredAttribute);
						String hundredtwoAttribute = getDataAtIndex(hundredoneAttribute, 2);
						String hundredthreeAttribute = fileHash.get(hundredtwoAttribute);
						String hundredfourAttribute = getDataAtIndex(hundredthreeAttribute, 3);
						String hundredfiveAttribute = fileHash.get(hundredfourAttribute);
						String widthofopening = getDataAtIndex(hundredfiveAttribute, 3);
						String hundredsixAttribute = fileHash.get(getDataAtIndexII(hundredfiveAttribute, 1));
						String heightofopening = getDataAtIndex(hundredsixAttribute, 3);
						String lengthofopening = getDataAtIndex(hundredsixAttribute, 4);

						Double heightofopenin = Double.valueOf(heightofopening);
						Double lengthofopenin = Double.valueOf(lengthofopening);
						Double widthofopenin = Double.valueOf(widthofopening);

						Double Areaofopening = heightofopenin * lengthofopenin;
						Double Volumeofopening = heightofopenin * lengthofopenin * widthofopenin;

						sum.add(Areaofopening);
						sumofvol.add(Volumeofopening);

					}

					for (int i = 0; i < sum.size(); i++) {
						temp = temp + sum.get(i);
					}

					System.out.println("Total Area of Opening:\t\t " + temp);
					wallMaterial.setTotalAreaOfOpening(String.valueOf(temp));
					NetArea = AreaofWall - temp;
					netArea.add(NetArea);
					
					System.out.println("Net Area of Wall is:\t\t " + NetArea);
					wallMaterial.setNetAreaOfWall(String.valueOf(NetArea));

					for (int i = 0; i < sumofvol.size(); i++) {
						temp1 = temp1 + sumofvol.get(i);
					}
					System.out.println("Total Volume of Openings:\t " + temp1);
					wallMaterial.setTotalVolumeOfOpening(String.valueOf(temp1));
					Double NetVolume = VolumeofWall - temp1;
					System.out.println("Net Volume of Wall is:\t\t " + NetVolume);
					wallMaterial.setNetVolumeOfWall(String.valueOf(NetVolume));

				} else {
					String seventhAttribute = fileHash.get(getDataAtIndex(sixthAttribute, 2));
					String eighthAttribute = fileHash.get(getDataAtIndexIII(seventhAttribute, 1));
					String ninthAttribute = fileHash.get(getDataAtIndex(eighthAttribute, 2));
					a = collapseToDecimal(getDataAtIndex(ninthAttribute, 1));
					b = getDataAtIndex(ninthAttribute, 2);
					d = collapseToDecimal(b);

					String[] radiusValues = new String[myLineI.size()];
					for (int i = 0; i < myLineI.size(); i++) {

						c = getDataAtIndex(myLineI.get(i), 1);
						radiusValues[i] = c;

					}

					Double centerLine = Double.parseDouble(radiusValues[0]);
					Double intLine = Double.parseDouble(radiusValues[1]);
					Double extLine = Double.parseDouble(radiusValues[2]);

					e = centerLine;
					f = intLine;
					g = extLine;
					Double testb = Double.valueOf(a);
					Double testd = Double.valueOf(d);
					Double testp = Double.valueOf(height);
					Double width = f - g;

					Double length = 2 * Math.PI * e * (1 - ((testb - testd) / 360));

					System.out.println("Height of wall is " + testp);
					System.out.println("Length of Wall is " + length);
					System.out.println("Width of Wall is " + width);

					Double AreaofWall = length * width;
					Double VolumeofWall = length * width * testp;
					System.out.println("Area of Wall is " + AreaofWall);
					System.out.println("Volume of Wall is " + VolumeofWall);

					String[] OpeningElement = new String[myLineII.size()];
					for (int i = 0; i < myLineII.size(); i++) {

						String hundredAttribute = getDataAtIndex((myLineII.get(i)), 6);
						String hundredoneAttribute = fileHash.get(hundredAttribute);
						String hundredtwoAttribute = getDataAtIndex(hundredoneAttribute, 2);
						String hundredthreeAttribute = fileHash.get(hundredtwoAttribute);
						String hundredfourAttribute = getDataAtIndex(hundredthreeAttribute, 3);
						String hundredfiveAttribute = fileHash.get(hundredfourAttribute);
						String heightofopening = getDataAtIndex(hundredfiveAttribute, 3);
						String hundredsixAttribute = fileHash.get(getDataAtIndexII(hundredfiveAttribute, 1));
						String lengthofopening = getDataAtIndex(hundredsixAttribute, 3);
						String widthofopening = getDataAtIndex(hundredsixAttribute, 4);

						Double heightofopenin = Double.valueOf(heightofopening);
						Double lengthofopenin = Double.valueOf(lengthofopening);
						Double widthofopenin = Double.valueOf(widthofopening);

						Double Areaofopening = heightofopenin * lengthofopenin;
						Double Volumeofopening = heightofopenin * lengthofopenin * widthofopenin;

						sum.add(Areaofopening);
						sumofvol.add(Volumeofopening);

					}
					for (int i = 0; i < sum.size(); i++) {
						temp = temp + sum.get(i);
					}
					System.out.println("Total Area of Opening: " + temp);
					NetArea = AreaofWall - temp;
					System.out.println("Net Area of Wall is : " + NetArea);
					netArea.add(NetArea);

					for (int i = 0; i < sumofvol.size(); i++) {
						temp1 = temp1 + sumofvol.get(i);
					}

					System.out.println("Total Volume of Opening: " + temp1);
					Double NetVolume = VolumeofWall - temp1;
					System.out.println("Net Volume of Wall is : " + NetVolume);
				}
				for (int i = 0; i < sum.size(); i++) {
					temp = temp + sum.get(i);
				}
			} else {
				String fifthAttribute = fileHash.get(getDataAtIndex(fourthAttribute, 3));
				String height = getDataAtIndex(fifthAttribute, 3);
				String sixthAttribute = fileHash.get(getDataAtIndexII(fifthAttribute, 1));
				String length = getDataAtIndex(sixthAttribute, 3);
				String width = getDataAtIndex(sixthAttribute, 4);

				System.out.println("Height of wall is " + height);
				System.out.println("Length of Wall is " + length);
				System.out.println("Width of Wall is " + width);

				Double testc = Double.valueOf(height);
				Double testd = Double.valueOf(length);

				System.out.println(testc);
			}

			String[] OpeningElement = new String[myLineII.size()];
			for (int i = 0; i < myLineII.size(); i++) {

				String hundredAttribute = getDataAtIndex((myLineII.get(i)), 6);
				String hundredoneAttribute = fileHash.get(hundredAttribute);
				String hundredtwoAttribute = getDataAtIndex(hundredoneAttribute, 2);
				String hundredthreeAttribute = fileHash.get(hundredtwoAttribute);
				String hundredfourAttribute = getDataAtIndex(hundredthreeAttribute, 3);
				String hundredfiveAttribute = fileHash.get(hundredfourAttribute);

				String heightofopening = getDataAtIndex(hundredfiveAttribute, 3);

				String hundredsixAttribute = fileHash.get(getDataAtIndexII(hundredfiveAttribute, 1));
				String lengthofopening = getDataAtIndex(hundredsixAttribute, 3);
				String widthofopening = getDataAtIndex(hundredsixAttribute, 4);

				Double heightofopenin = Double.valueOf(heightofopening);
				Double lengthofopenin = Double.valueOf(lengthofopening);
				Double widthofopenin = Double.valueOf(widthofopening);

				Double Areaofopening = heightofopenin * lengthofopenin;
				Double Volumeofopening = heightofopenin * lengthofopenin * widthofopenin;
			}

			String[] materialLayer = new String[myLineIII.size()];
			for (int i = 0; i < myLineIII.size(); i++) {

				String oneHundredNinetyAttribute = getDataAtIndex((myLineIII.get(i)), 5);
				String twoHundredAttribute = getDataAtIndexIII((myLineIII.get(i)), 1);
				String twoHundredTenAttribute = fileHash.get(twoHundredAttribute);
				String twoHundredTenoneAttribute = fileHash.get(getDataAtIndexII(twoHundredTenAttribute, 1));
				String twoHundredTentwoAttribute = getDataAtIndex(twoHundredTenAttribute, 1);

				System.out.println("\nI " + twoHundredTenAttribute);

				String twoHundredOneAttribute = getDataAtIndex((myLineIII.get(i)), 1);
				String twoHundredElevenAttribute = fileHash.get(twoHundredOneAttribute);
				String twoHundredElevenoneAttribute = fileHash.get(getDataAtIndexII(twoHundredElevenAttribute, 1));
				String twoHundredEleventwoAttribute = getDataAtIndex(twoHundredElevenAttribute, 1);

				String twoHundredTwoAttribute = getDataAtIndex((myLineIII.get(i)), 2);
				String twoHundredTwelveAttribute = fileHash.get(twoHundredTwoAttribute);
				String twoHundredTwelveoneAttribute = fileHash.get(getDataAtIndexII(twoHundredTwelveAttribute, 1));
				String twoHundredTwelvetwoAttribute = getDataAtIndex(twoHundredTwelveAttribute, 1);

				String twoHundredThreeAttribute = getDataAtIndex((myLineIII.get(i)), 3);
				String twoHundredThirteenAttribute = fileHash.get(twoHundredThreeAttribute);
				String twoHundredThirteenoneAttribute = fileHash.get(getDataAtIndexII(twoHundredThirteenAttribute, 1));
				String twoHundredThirteentwoAttribute = getDataAtIndex(twoHundredThirteenAttribute, 1);

				String twoHundredFourAttribute = getDataAtIndex((myLineIII.get(i)), 4);
				String twoHundredFourteenAttribute = fileHash.get(twoHundredFourAttribute);
				String twoHundredFourteenoneAttribute = fileHash.get(getDataAtIndexII(twoHundredFourteenAttribute, 1));
				String twoHundredFourteentwoAttribute = getDataAtIndex(twoHundredFourteenAttribute, 1);
				products.add(twoHundredTenoneAttribute);
				products.add(twoHundredElevenoneAttribute);
				products.add(twoHundredTwelveoneAttribute);
				products.add(twoHundredThirteenoneAttribute);

				ProductSearch p = new ProductSearch();
				String[] tokenArray = p.tokenizer(twoHundredTenoneAttribute);
				String ifcMaterialLayerSet = extractDataForLineIII(String.valueOf(myLineIII)).replaceAll(IFCConstants.IFC_MATERIAL_LAYER_SET, "").trim();
				
				System.out.println("\n" + IFCConstants.IFC_MATERIAL_LAYER_SET+ifcMaterialLayerSet+"\n");
				wallMaterial.setIfcMaterialLayerSet(ifcMaterialLayerSet);
				//System.out.println("\nIFCMATERIAL 0:\t\t\t " + extractData(twoHundredFourteenoneAttribute));
				System.out.println("IFCMATERIAL 1:\t\t\t " + extractData(twoHundredTenoneAttribute));
				System.out.println("Thickness of material is:\t " + twoHundredTentwoAttribute);
				gypsumThicknessSet.add(twoHundredTentwoAttribute.substring(0, 9));

				System.out.println("IFCMATERIAL 2:\t\t\t " + extractData(twoHundredElevenoneAttribute));
				System.out.println("Thickness of material is:\t " + twoHundredEleventwoAttribute);
				gypsumThicknessSet.add(twoHundredEleventwoAttribute.substring(0, 9));

				System.out.println("IFCMATERIAL 3:\t\t\t " + extractData(twoHundredTwelveoneAttribute));
				System.out.println("Thickness of material is:\t " + twoHundredTwelvetwoAttribute);
				
				InsulationThicknessSet.add(twoHundredTwelvetwoAttribute.substring(0, 9));
				StudThicknessSet.add(twoHundredTwelvetwoAttribute.substring(0, 9));
				
				System.out.println("IFCMATERIAL 4:\t\t\t " + extractData(twoHundredThirteenoneAttribute));
				System.out.println("Thickness of material is:\t " + twoHundredThirteentwoAttribute);
				gypsumThicknessSet.add(twoHundredThirteentwoAttribute.substring(0, 9));

				System.out.println("IFCMATERIAL 5:\t\t\t " + extractData(twoHundredFourteenoneAttribute));
				System.out.println("Thickness of material is:\t " + twoHundredFourteentwoAttribute);
				gypsumThicknessSet.add(twoHundredFourteentwoAttribute.substring(0, 9));

				System.out.println("\n");
				thicknessMap.put(extractData(twoHundredTenoneAttribute), gypsumThicknessSet);
				thicknessMap.put(getInsulation(extractData(twoHundredTwelveoneAttribute)),
						InsulationThicknessSet);
				thicknessMap.put(KMConstants.STUD, StudThicknessSet);
				

			}

		}
		processingData(wallMaterial, thicknessMap);

	}


	private String getInsulation(String extractData) {
		 String insulation = extractData.replace("Structure, Wood Joist/Rafter Layer, ", "").trim();
		 return insulation.replaceAll(" ", "_");
		 
		 
	}

/*	private void calculateCost(ArrayList<Double> costsum, ArrayList<Double> netArea, Material material) {
		Double cos;
		System.out.println("Unit Cost of "+ material.getTitle()+ " with thickness " + material.getThickness() + " is $" + material.getCost());
		cos = netArea.get(0) * material.getCost();
		costsum.add(cos);
		System.out.println("Total Cost of "+ material.getTitle()+ " with thickness " + material.getThickness() + " is $" + cos);
	}
	*/
	private void processingData(WallMaterial wallMaterial, LinkedHashMap<String, LinkedHashSet<String>> thicknessMap) {
		String materialWithThickness = "";
		for (Map.Entry<String, LinkedHashSet<String>> map : thicknessMap.entrySet()) {
			String materialKey = map.getKey();
			String[] thicknessArr = map.getValue().toArray(new String[map.getValue().size()]);
			for (int i = 0; i < thicknessArr.length; i++) {
				if (i == 0) {
					materialWithThickness += "(" + materialKey + "(#" + thicknessArr[i] + "))";
					continue;
				}
				materialWithThickness += "(" + materialKey + "(#" + thicknessArr[i] + "))";
			}
		}
		wallMaterial.setMaterialsWithThickness(materialWithThickness);

	}

	private String extractData(String val) {
		int start = val.indexOf("('");
		int end = val.indexOf("')");
		return val.substring(start + 2, end);
	}

	private String extractDataForLineIII(String val) {
		int start = val.indexOf("('");
		int end = val.indexOf(";");
		return val.substring(start + 2, end);
	}

	private String slice_range(String s, int startIndex, int endIndex) {
		if (startIndex < 0)
			startIndex = s.length() + startIndex;
		if (endIndex < 0)
			endIndex = s.length() + endIndex;
		return s.substring(startIndex, endIndex);
	}

	public void createFileIndex() {
		for (String line : fileLines) {
			if (line.startsWith("#")) {
				String identifier = slice_range(line, 0, line.indexOf("="));
				String remLine = slice_range(line, line.indexOf("=") + 1, line.length());
				fileHash.put(identifier, remLine);
				String ifcType = slice_range(remLine, 0, remLine.indexOf("("));
				entities.add(ifcType.trim());

				if (line.contains(IFC_WALL_STANDARD_CASE)) {
					startingLine = identifier;
				}
				if (line.contains(IFC_CIRCLE)) {
					startingLineI = identifier;
					myLineI.add(remLine);
				}
				if (line.contains(IFC_OPENING_ELEMENT)) {
					startingLineII = identifier;
					myLineII.add(remLine);

				}
				if (line.contains(IFC_MATERIAL_LAYER_SET)) {
					startingLineIII = identifier;
					myLineIII.add(remLine);
				}
			}
		}
	}

	private String getDataAtIndex(String line, int index) {
		String[] entries = line.split(","); // split line by comma
		return index > entries.length ? Constants.EMPTYSTRING : entries[index].replaceAll("[\\(\\)\\;]", "");
	}

	private String getDataAtIndexII(String line, int index) {
		String c = line.substring(line.indexOf('(') + 1, ((line.indexOf(',')))); // split line by comma
		return c;
	}

	private String getDataAtIndexIII(String line, int index) {
		String c = line.substring(line.indexOf('(') + 2, ((line.indexOf(',')))); // split line by comma
		return c;
	}

	private String getDataAtIndexIV(String line, int index) {
		String c = line.substring(line.indexOf('(') + 5, ((line.indexOf(',')))); // split line by comma
		return c;
	}

	private String collapseToDecimal(String text) {
		DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols();

		StringBuilder buffer = new StringBuilder();
		boolean firstMinusSign = true;
		boolean firstSeparator = true;
		char minusSign = decimalSymbols.getMinusSign();
		char separator = decimalSymbols.getDecimalSeparator();
		char zero = decimalSymbols.getZeroDigit();

		for (int i = 0; i < text.length(); i++) {
			char testChar = text.charAt(i);

			if (testChar == minusSign && firstMinusSign && buffer.length() == 0) {
				buffer.append(testChar);
				firstMinusSign = false;
				continue;
			}

			if (testChar == separator && firstSeparator) {
				buffer.append(testChar);
				firstSeparator = false;
				continue;
			}

			if (Character.isDigit(testChar)) {
				buffer.append(testChar);
			}
		}
		if (buffer.length() < 1) {
			buffer.append(zero);
		}
		return buffer.toString();
	}

	public LinkedHashSet<String> getEntities() {
		return entities;
	}

	public LinkedHashMap<String, LinkedHashSet<String>> getThicknessMap() {
		return thicknessMap;
	}

	public Object getWallMaterial() {
		return wallMaterial;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
	
}
