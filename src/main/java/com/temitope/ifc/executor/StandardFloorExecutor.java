package com.temitope.ifc.executor;

import com.github.jsonldjava.utils.Obj;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.temitope.ifc.catalogue.ProductsCatalogue;
import com.temitope.ifc.common.EntityHolderHelper;
import com.temitope.ifc.common.IFCConstants;
import com.temitope.ifc.parser.IFCFileParserUtil;
import com.temitope.owl.catalogue.OwlModal.House.Floor;
import com.temitope.owl.model.FloorMaterial;
import com.temitope.owl.model.WallMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StandardFloorExecutor {
	private List<String> fileLines = new ArrayList<>();
	private HashMap<String, String> fileHash = new HashMap<>();
	private Set<String> entities = new HashSet<>();
	private LinkedHashSet<String> carpetSet = new LinkedHashSet<>();
	private LinkedHashSet<String> PlywoodAndSheathingSet = new LinkedHashSet<>();
	private LinkedHashSet<String> StructureWoodJoistRafterLayerSet = new LinkedHashSet<>();
	private LinkedHashMap<String, LinkedHashSet<String>> thicknessMap = new LinkedHashMap();
	private final String IFC_SLAB = "IFCSLAB";
	private final String IFC_MATERIAL_LAYER_SET = "IFCMATERIALLAYERSET(";
	private final String IFC_ARBITRARY_CLOSED_PROFILE_DEF = "IFCARBITRARYCLOSEDPRFILEDEF";
	private final String IFC_RECTANGLE_PROFILE_DEF = "IFCRECTANGLEPROFILEDEF";
	private String startingLineIV = null;
	private String startingLineV = null;
	private String startingLineVI = null;
	private ArrayList<String> myLineV = new ArrayList<>();
	private static Map<Integer, ProductsCatalogue> productsCatalogueMap = new HashMap<Integer, ProductsCatalogue>();
	private static ProductsCatalogue prod, prod1, prod2, prod3;
	private ArrayList<String> products = new ArrayList<>();
	ArrayList<Double> Areaoffloor = new ArrayList<Double>();
	ArrayList<Double> costsum = new ArrayList<Double>();
	private String height;
	Double cos;
	Double temp1a = 0.0;
    private FloorMaterial floorMaterial;
	static {
		prod = new ProductsCatalogue(IFCFileParserUtil.FLOOR.Carpet1, "0.0104166666666667", 0.35);
		prod1 = new ProductsCatalogue(IFCFileParserUtil.FLOOR.Plywood, "0.0625", 0.34);
		prod2 = new ProductsCatalogue(IFCFileParserUtil.FLOOR.StructureWoodJoistRafterLayer, "0.989583333333333", 0.34);
		prod3 = new ProductsCatalogue(IFCFileParserUtil.FLOOR.StructureWoodJoistRafterLayerBattInsulation,
				"0.208333333333333", 0.75);
		ProductsCatalogue[] productsCatalogues = { prod, prod1, prod2, prod3 };
		for (int i = 0; i < productsCatalogues.length; i++) {
			productsCatalogueMap.put(i, productsCatalogues[i]);
		}
	}

	public StandardFloorExecutor(List<String> fileLines) {
		this.fileLines = fileLines;
	}

	public void execute() {
	 floorMaterial = new FloorMaterial();
		if (startingLineIV == null) {
			return;
		}

		String a = "";
		String b = "";
		String c = "";
		String d = "";
		String e = "";
		Double cos;

		String fiftiethAttribute = fileHash.get(startingLineIV);
		String fiftiethoneAttribute = getDataAtIndex(fiftiethAttribute, 6);
		if (fiftiethoneAttribute != null) {
			String fiftiethtwoAttribute = getDataAtIndex(fileHash.get(fiftiethoneAttribute), 2);
			String fiftieththreeAttribute = fileHash.get(fiftiethtwoAttribute);

			if (fiftieththreeAttribute.contains("SweptSolid")) {
				String fiftiethfourAttribute = fileHash.get(getDataAtIndex(fiftieththreeAttribute, 3));

				String height = getDataAtIndex(fiftiethfourAttribute, 3);
				String fiftiethfiveAttribute = fileHash.get(getDataAtIndexII(fiftiethfourAttribute, 1));
				String length = getDataAtIndex(fiftiethfiveAttribute, 3);
				String width = getDataAtIndex(fiftiethfiveAttribute, 4);

				System.out.println("Height of floor is:\t " + height);
				floorMaterial.setHeightOfFloor(height);
				setHeight(height);
				System.out.println("Length of floor is:\t " + length);
				floorMaterial.setLengthOfFloor(length);
				System.out.println("Width of floor is:\t " + width);
				floorMaterial.setWidthOfFloor(width);

				Double teste = Double.valueOf(height);
				Double testf = Double.valueOf(length);
				Double testm = Double.valueOf(width);
				Double Areaoffloor = testf * testm;
				Double Volumeoffloor = teste * testf * testm;

				System.out.println("Area of floor is:\t " + Areaoffloor);
				floorMaterial.setAreaOfFloor(String.valueOf(Areaoffloor));
				System.out.println("Volume of floor is:\t " + Volumeoffloor);
				floorMaterial.setVolumeOfFloor(String.valueOf(Volumeoffloor));

			}

		}
		String[] materialLayer = new String[myLineV.size()];
		for (int i = 0; i < myLineV.size(); i++) {

			String oneHundredNinetyAttribute = getDataAtIndex((myLineV.get(i)), 3);
			String twoHundredAttribute = getDataAtIndexIII((myLineV.get(i)), 1);
			String twoHundredTenAttribute = fileHash.get(twoHundredAttribute);
			String twoHundredTenoneAttribute = fileHash.get(getDataAtIndexII(twoHundredTenAttribute, 1));
			String twoHundredTentwoAttribute = getDataAtIndex(twoHundredTenAttribute, 1);

			String twoHundredOneAttribute = getDataAtIndex((myLineV.get(i)), 1);
			String twoHundredElevenAttribute = fileHash.get(twoHundredOneAttribute);
			String twoHundredElevenoneAttribute = fileHash.get(getDataAtIndexII(twoHundredElevenAttribute, 1));
			String twoHundredEleventwoAttribute = getDataAtIndex(twoHundredElevenAttribute, 1);

			String twoHundredTwoAttribute = getDataAtIndex((myLineV.get(i)), 2);
			String twoHundredTwelveAttribute = fileHash.get(twoHundredTwoAttribute);
			String twoHundredTwelveoneAttribute = fileHash.get(getDataAtIndexII(twoHundredTwelveAttribute, 1));
			String twoHundredTwelvetwoAttribute = getDataAtIndex(twoHundredTwelveAttribute, 1);

			//System.out.println("\n\t" + extractDataForLineV(String.valueOf(myLineV)) + "\t\n");
			//System.out.println(oneHundredNinetyAttribute + "\n");
		
			String ifcMaterialLayerSet = extractDataForLineV(String.valueOf(myLineV)).replaceAll(IFCConstants.IFC_MATERIAL_LAYER_SET, "").trim();
			System.out.println("\n" + IFCConstants.IFC_MATERIAL_LAYER_SET+ifcMaterialLayerSet+"\n");
			floorMaterial.setIfcMaterialLayerSet(ifcMaterialLayerSet);
			
			System.out.println("IFCMATERIAL 1:\t\t\t " + extractData(twoHundredTenoneAttribute));
			System.out.println("Thickness of material is:\t " + twoHundredTentwoAttribute);
			carpetSet.add(twoHundredTentwoAttribute.substring(0, 9));

			System.out.println("IFCMATERIAL 2:\t\t\t " + extractData(twoHundredElevenoneAttribute));
			System.out.println("Thickness of material is:\t " + twoHundredEleventwoAttribute);
			PlywoodAndSheathingSet.add(twoHundredEleventwoAttribute);
	
			System.out.println("IFCMATERIAL 3:\t\t\t " + extractData(twoHundredTwelveoneAttribute));
			System.out.println("Thickness of material is:\t " + twoHundredTwelvetwoAttribute);
			StructureWoodJoistRafterLayerSet.add(twoHundredTwelvetwoAttribute.substring(0, 9));

			thicknessMap.put(extractData(twoHundredTenoneAttribute), carpetSet);
			thicknessMap.put(extractData(twoHundredElevenoneAttribute), PlywoodAndSheathingSet);
			thicknessMap.put(extractData(twoHundredTwelveoneAttribute), StructureWoodJoistRafterLayerSet);
			
		
		}
		processingData(floorMaterial, thicknessMap);
	}

	private void processingData(FloorMaterial floorMaterial, LinkedHashMap<String, LinkedHashSet<String>> thicknessMap) {
		String materialWithThickness = "";
		for (Map.Entry<String, LinkedHashSet<String>> map : thicknessMap.entrySet()) {
			String materialKey = map.getKey();
			String[] thicknessArr = map.getValue().toArray(new String[map.getValue().size()]);
			for (int i = 0; i < thicknessArr.length; i++) {
				  if(i==0) {
						materialWithThickness += "("+materialKey + "(#" + thicknessArr[i] + "))"; 
						continue;
				  }
					materialWithThickness += "("+materialKey + "(#" + thicknessArr[i] + "))";  	  
			}
		
		}
		floorMaterial.setMaterialsWithThickness(materialWithThickness);

	}

	private String extractData(String val) {
		int start = val.indexOf("('");
		int end = val.indexOf("')");
		return val.substring(start + 2, end);
	}
	private String extractDataForLineV(String val) {
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
				if (line.contains(IFC_SLAB)) {
					startingLineIV = identifier;

				}
				if (line.contains(IFC_MATERIAL_LAYER_SET)) {
					startingLineV = identifier;
					myLineV.add(remLine);
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

	
	public Set<String> getEntities() {
		return entities;
	}

	public LinkedHashMap<String, LinkedHashSet<String>> getThicknessMap() {
		return thicknessMap;
	}

	public Object getFloorMaterial() {
		return floorMaterial;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
	

}
