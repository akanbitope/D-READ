package com.temitope.km;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.lucene.util.PrintStreamInfoStream;
import org.hibernate.Session;
import org.semanticweb.owlapi.model.OWLOntology;

import com.github.jsonldjava.utils.Obj;
import com.temitope.ifc.common.KMConstants;
import com.temitope.ifc.processors.IFCProcessor;
import com.temitope.model.Material;
import com.temitope.owl.model.FloorMaterial;
import com.temitope.owl.model.Gypsum_Board;
import com.temitope.owl.model.Insulation;
import com.temitope.owl.model.Plywood_Sheathing;
import com.temitope.owl.model.Stud;
import com.temitope.owl.model.WallMaterial;
import com.temitope.owl.parser.impl.OwlOntologyParser;
import com.temitope.owl.parser.util.OwlManagerUtil;
import com.temitope.util.AppProperties;

public class KnowledgeModal {
	private OwlOntologyParser owlOntologyParser;
	private IFCProcessor ifcProcessor;
	private Map<String, LinkedHashMap<String, LinkedHashSet<String>>> knowledgeModalMap;
	private Scanner scanner = new Scanner(System.in);
	private Map<String, LinkedHashSet<String>> insulationThickness;
	private String heightOfComponent;
	private WallMaterial wallMaterial;
	private Plywood_Sheathing plywood_Sheathing;
	private Gypsum_Board gypsum_Board;
	private Stud stud;
	private Insulation insulation;
	private Object material;
	List<Material> materialCosts = new ArrayList<>();
	Map<String, Material> materialCostMap = new ConcurrentHashMap<String, Material>();

	public KnowledgeModal(Object material, LinkedHashMap<String, LinkedHashSet<String>> thicknessMap, String height) {
		this.heightOfComponent = height;
		this.wallMaterial = (WallMaterial) material;
		processKnowledgeModal(thicknessMap, material);
		

	}

	private void processKnowledgeModal(LinkedHashMap<String, LinkedHashSet<String>> thicknessMap, Object material) {
	
	try {
		OwlManagerUtil owlManagerUtil = OwlManagerUtil.getInstance();
		OWLOntology ontology = owlManagerUtil.getOwlOntologyByFile(AppProperties.OWL_FILE_NAME);
		owlOntologyParser = new OwlOntologyParser(thicknessMap);
		owlOntologyParser.parse(ontology);
		knowledgeModalMap = owlOntologyParser.getKnowledgeModalMap();
		insulationThickness = owlOntologyParser.getInsulationThickness();
		SuggestionHelper suggestionHelper =  new SuggestionHelper(this);
		 suggestionHelper.askSuggestionForKnowledgeModal(owlOntologyParser, material);
	
   	
	} catch (Exception e) {
		System.err.println("Exception Occured while processing Knowledge Modal");
	}


	}

	public void suggession(Session session,
			LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> knowledgeModalMap, String stringKey) {
		
		try {
			if (stringKey.equals(KMConstants.BATT_INSULATION)
					&& knowledgeModalMap.containsKey(KMConstants.BATT_INSULATION)) {
				giveSuggestionForInsulationToUser(session, knowledgeModalMap.get(KMConstants.BATT_INSULATION),
						KMConstants.BATT_INSULATION);                                                                                        
			} else if (stringKey.equals(KMConstants.BLANKET_INSULATION)
					&& knowledgeModalMap.containsKey(KMConstants.BLANKET_INSULATION)) {
				giveSuggestionForInsulationToUser(session, knowledgeModalMap.get(KMConstants.BLANKET_INSULATION),
						KMConstants.BLANKET_INSULATION);
			} else if (stringKey.equals(KMConstants.INSULATION)
					&& knowledgeModalMap.containsKey(KMConstants.INSULATION)) {
				giveSuggestionForInsulationToUser(session, knowledgeModalMap.get(KMConstants.INSULATION));
			} else if (stringKey.equals(KMConstants.GYPSUM_BOARD) && knowledgeModalMap.containsKey(stringKey)) {
				giveSuggestionForGypsumBoardToUser(session, knowledgeModalMap.get(KMConstants.GYPSUM_BOARD));
			} else if (stringKey.equals(KMConstants.STUD) && knowledgeModalMap.containsKey(stringKey)) {
				giveSuggestionForStudToUser(session, knowledgeModalMap.get(KMConstants.STUD));
			} else if (stringKey.equals(KMConstants.Playwood_Sheathing) && knowledgeModalMap.containsKey(stringKey)) {
				giveSuggestionForSheathingToUser(session, knowledgeModalMap.get(KMConstants.Playwood_Sheathing));
			}
		} catch (Exception e) {

		}
		

	}

	private Plywood_Sheathing processingSheathing(Session session, Map<String, LinkedHashSet<String>> sheathingMap) {
		 plywood_Sheathing = new Plywood_Sheathing();

		for (Entry<String, LinkedHashSet<String>> mapEntry : sheathingMap.entrySet()) {
			if (mapEntry.getKey().equals(KMConstants.NAME)) {
				String name = askingMissingValuesToInsert(KMConstants.NAME, mapEntry);
				plywood_Sheathing.setName(name.replaceAll("_", " ").trim());
				/*
				 * if(name.contains("Plywood")) { if
				 * (mapEntry.getKey().equals(KMConstants.NAILED)) { String nailed =
				 * askingMissingValuesToInsert(KMConstants.NAILED, mapEntry);
				 * plywood_Sheathing.setNailed(nailed); } }else if(name.contains("Boards")) { if
				 * (mapEntry.getKey().equals(KMConstants.LAID)) { String laid =
				 * askingMissingValuesToInsert(KMConstants.LAID, mapEntry);
				 * plywood_Sheathing.setLaid(laid); } }else if(name.contains("Gypsum")) { if
				 * (mapEntry.getKey().equals(KMConstants.GYPSUM_TYPE)) { String gypsumType =
				 * askingMissingValuesToInsert(KMConstants.GYPSUM_TYPE, mapEntry);
				 * plywood_Sheathing.setGypsumType(gypsumType); } }
				 */
			} else if (mapEntry.getKey().equals(KMConstants.GYPSUM_TYPE)) {
				String gypsumType = askingMissingValuesToInsert(KMConstants.GYPSUM_TYPE, mapEntry);
				plywood_Sheathing.setGypsumType(gypsumType);
			} else if (mapEntry.getKey().equals(KMConstants.LAID)) {
				String laid = askingMissingValuesToInsert(KMConstants.LAID, mapEntry);
				plywood_Sheathing.setLaid(laid);
			} else if (mapEntry.getKey().equals(KMConstants.NAILED)) {
				String nailed = askingMissingValuesToInsert(KMConstants.NAILED, mapEntry);
				plywood_Sheathing.setNailed(nailed);
			}
		}
			plywood_Sheathing.setNetArea(Double.valueOf(wallMaterial.getNetAreaOfWall()));
		    Material plywood_sheathingMc = new Material();
		    plywood_sheathingMc.setName(plywood_Sheathing.getName());
		    plywood_sheathingMc.setThickness(Double.valueOf(plywood_Sheathing.getThickness()));
		    plywood_sheathingMc.setNetArea(Double.valueOf(plywood_Sheathing.getNetArea()));
			materialCosts.add(plywood_sheathingMc);
		    materialCostMap.put(KMConstants.Playwood_Sheathing, plywood_sheathingMc);
		return plywood_Sheathing;
	}

	private Gypsum_Board processingGypsumBoard(Session session, Map<String, LinkedHashSet<String>> hasMap) {
		Gypsum_Board gypsum_Board = new Gypsum_Board();
		gypsum_Board.setThickness(getThicknessFromIfcFile(hasMap));
		for (Entry<String, LinkedHashSet<String>> mapEntry : hasMap.entrySet()) {
			if (mapEntry.getKey().equals(KMConstants.NAME)) {
				String name = askingMissingValuesToInsert(KMConstants.NAME, mapEntry);
				gypsum_Board.setName(name.replaceAll("_", " ").trim());

			} else if (mapEntry.getKey().equals(KMConstants.FINISH)) {
				String finish = askingMissingValuesToInsert(KMConstants.FINISH, mapEntry);
				gypsum_Board.setFinish(finish.replaceAll("_", " ").trim());

			} else if (mapEntry.getKey().equals(KMConstants.TOOL)) {
				String tool = askingMissingValuesToInsert(KMConstants.TOOL, mapEntry);
				gypsum_Board.setTool(tool);

			}
		}
			gypsum_Board.setNetArea(Double.valueOf(wallMaterial.getNetAreaOfWall()));
		    Material gypsum_board = new Material();
		    gypsum_board.setMaterial(KMConstants.GYPSUM_BOARD);
		    gypsum_board.setName(gypsum_Board.getName());
		    gypsum_board.setThickness(Double.valueOf(gypsum_Board.getThickness()));
		    gypsum_board.setFinish(gypsum_Board.getFinish());
		    gypsum_board.setNetArea(Double.valueOf(gypsum_Board.getNetArea()));
		    gypsum_board.setTitle(KMConstants.GYPSUM_BOARD.replaceAll("_", " ").trim()+","+gypsum_Board.getName()+","+gypsum_Board.getFinish());
			materialCosts.add(gypsum_board);
		    materialCostMap.put(KMConstants.GYPSUM_BOARD, gypsum_board );
		    return gypsum_Board;
	}

	private Stud processingStud(Session session, Map<String, LinkedHashSet<String>> hasMap) {
		Stud stud = new Stud();
		stud.setThickness(getThicknessFromIfcFile(hasMap));
		stud.setHeight(getHeightFromIfcFile(getHeightOfComponent()));
		for (Entry<String, LinkedHashSet<String>> mapEntry : hasMap.entrySet()) {
			/*if (mapEntry.getKey().equals(KMConstants.LOCATION)) {
				String location = askingMissingValuesToInsert(KMConstants.LOCATION, mapEntry);
				stud.setLocation(location);
			} else*/ if (mapEntry.getKey().equals(KMConstants.NAILED)) {
				String nailed = askingMissingValuesToInsert(KMConstants.NAILED, mapEntry);
				stud.setNailed(nailed);
			} else if (mapEntry.getKey().equals(KMConstants.SPACING)) {
				String spacing = askingMissingValuesToInsert(KMConstants.SPACING, mapEntry);
				stud.setSpacing(spacing);
			}
		}

			stud.setNetArea(Double.valueOf(wallMaterial.getNetAreaOfWall()));
		    Material studMc = new Material();
		    studMc.setMaterial(KMConstants.STUD);
		    studMc.setHeight(stud.getHeight());
		    studMc.setNailed(stud.getNailed());
		    studMc.setThickness(Double.valueOf(stud.getThickness()));
		    studMc.setNetArea(Double.valueOf(stud.getNetArea()));
		    studMc.setTitle(KMConstants.STUD+","+stud.getHeight()+","+stud.getNailed().replaceAll("_", " ").trim());
			materialCosts.add(studMc);
			materialCostMap.put(KMConstants.STUD, studMc);
		return stud;
	}

	private String getHeightFromIfcFile(Long height) {
		System.out.println("Height is " + height + " extracted from IFC File\n");
		return String.valueOf(height);
	}

	private Insulation processingInsulation(Session session, Map<String, LinkedHashSet<String>> hasMap) {
		Insulation insulation = new Insulation(); // create insulation object
		insulation.setThickness(getThicknessFromIfcFile(hasMap));
		System.out.println("thickness " + insulation.getThickness());
		for (Entry<String, LinkedHashSet<String>> mapEntry : hasMap.entrySet()) {
			if (mapEntry.getKey().equals(KMConstants.NAME)) {
				String name = askingMissingValuesToInsert(KMConstants.NAME, mapEntry);
				insulation.setName(name.replaceAll("_", " "));
				for (Map.Entry<String, LinkedHashSet<String>> entry : insulationThickness.entrySet()) {
					if (entry.getKey().equals(name) && entry.getKey().equals(KMConstants.Fiberglass)) {
						LinkedHashMap<String, LinkedHashSet<String>> insulationMap = new LinkedHashMap();
						insulationMap.putAll(hasMap);
						for (Map.Entry<String, LinkedHashSet<String>> insulationEntry : insulationMap.entrySet()) {
							if (insulationEntry.getKey().equals(KMConstants.CF)) {
								String cF = askingMissingValuesToInsert(KMConstants.Fiberglass + " " + KMConstants.CF,
										insulationEntry);
								insulation.setFiberglassCF(cF);
							}
							if (insulationEntry.getKey().equals(KMConstants.TYPE)) {
								String type = askingMissingValuesToInsert(
										KMConstants.Fiberglass + " " + KMConstants.TYPE, insulationEntry);
								insulation.setFiberglassType(type);

							}
							if (insulationEntry.getKey().equals(KMConstants.WIDTH)) {
								String width = askingMissingValuesToInsert(KMConstants.WIDTH, insulationEntry);
								insulation.setFiberglassWidth(width);
							}
						}

					}
				}

			} else if (mapEntry.getKey().equals(KMConstants.NUMBER)) {
				String number = askingMissingValuesToInsert(KMConstants.NUMBER, mapEntry);
				insulation.setNumber(number);

			} else if (mapEntry.getKey().equals(KMConstants.FACED)) {
				String faced = askingMissingValuesToInsert(KMConstants.FACED, mapEntry);
				insulation.setFaced(faced.replaceAll("_", " "));
			}

		}
			insulation.setNetArea(Double.valueOf(wallMaterial.getNetAreaOfWall()));
			Material insulationMc = new Material();
			insulationMc.setMaterial(KMConstants.INSULATION);
			insulationMc.setName(insulation.getName());
			insulationMc.setNumber(insulation.getNumber());
			insulationMc.setThickness(Double.valueOf(insulation.getThickness()));
			insulationMc.setNetArea(Double.valueOf(insulation.getNetArea()));
			insulationMc.setTitle(KMConstants.INSULATION+","+insulation.getName()+","+insulation.getNumber());
		    materialCosts.add(insulationMc);
		    materialCostMap.put(KMConstants.INSULATION, insulationMc);
		return insulation;
	}

	private Insulation processingInsulation(Session session, Map<String, LinkedHashSet<String>> hasMap,
			String insulationType) {
		Insulation insulation = new Insulation(); // create insulation object
		insulation.setThickness(getThicknessFromIfcFile(hasMap));
		insulation.setName(getNameFromIfcFile());
		insulation.setFiberglassType(getFiberglassTypeFromIfcFile(insulationType));
		if(insulation.getName().equals(KMConstants.Fiberglass)) {
			for (Map.Entry<String, LinkedHashSet<String>> insulationEntry : hasMap.entrySet()) {
				if (insulationEntry.getKey().equals(KMConstants.CF)) {
					String cF = askingMissingValuesToInsert(KMConstants.Fiberglass + " " + KMConstants.CF,
							insulationEntry);
					insulation.setFiberglassCF(cF);
				}
				else if (insulationEntry.getKey().equals(KMConstants.WIDTH)) {
					String width = askingMissingValuesToInsert(KMConstants.WIDTH, insulationEntry);
					insulation.setFiberglassWidth(width);
				}
			}
		}
		for (Entry<String, LinkedHashSet<String>> mapEntry : hasMap.entrySet()) {
			
			 if (mapEntry.getKey().equals(KMConstants.NUMBER)) {
				String number = askingMissingValuesToInsert(KMConstants.NUMBER, mapEntry);
				insulation.setNumber(number);
				

			} else if (mapEntry.getKey().equals(KMConstants.FACED)) {
				String faced = askingMissingValuesToInsert(KMConstants.FACED, mapEntry);
				insulation.setFaced(faced.replaceAll("_", " "));
			}

		}
			insulation.setNetArea(Double.valueOf(wallMaterial.getNetAreaOfWall())); 
			Material insulationMc = new Material();
			insulationMc.setMaterial(insulationType);
			insulationMc.setName(insulation.getName());
			insulationMc.setNumber(insulation.getNumber());
			insulationMc.setThickness(Double.valueOf(insulation.getThickness()));
			insulationMc.setNetArea(Double.valueOf(insulation.getNetArea()));
			insulationMc.setTitle(insulationType.replaceAll("_", " ")+","+insulation.getName()+","+insulation.getNumber());

		    materialCosts.add(insulationMc);
		    materialCostMap.put(insulationType, insulationMc);
		return insulation;
	}

	private String getFiberglassTypeFromIfcFile(String insulationType) {
		System.out.println(
				"Type is " + insulationType.replace(KMConstants._INSULATION, "").trim() + " "
						+ "\n");
		return insulationType.replace(KMConstants._INSULATION, "").trim();
	}

	private String getNameFromIfcFile() {
		System.out.println("Name is " + KMConstants.Fiberglass + "\n");
		return KMConstants.Fiberglass;
	}

	private String getThicknessFromIfcFile(Map<String, LinkedHashSet<String>> hasMap) {
		String thickness = "";
		try {
			if (hasMap.get(KMConstants.THICKNESS).size() > 1) {
				for (Entry<String, LinkedHashSet<String>> mapEntry : hasMap.entrySet()) {
					thickness = askingMissingValuesToInsert(KMConstants.THICKNESS, mapEntry);
				}
			} else {
				thickness = hasMap.get(KMConstants.THICKNESS).iterator().next();
				System.out.println("Thickness is " + thickness + " extracted from IFC File\n");
			}
		} catch (Exception e) {
		}
		return thickness;
	}

	private Plywood_Sheathing giveSuggestionForSheathingToUser(Session session, Map<String, LinkedHashSet<String>> sheathingMap) {
		try {
			printMaterialName(KMConstants.Playwood_Sheathing);
			 plywood_Sheathing = processingSheathing(session, sheathingMap);
			session.save(plywood_Sheathing);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plywood_Sheathing;
	}

	private Stud giveSuggestionForStudToUser(Session session, Map<String, LinkedHashSet<String>> studMap) {
		try {
			printMaterialName(KMConstants.STUD);
			 stud = processingStud(session, studMap);
			session.save(stud);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stud;
	}

	private Gypsum_Board giveSuggestionForGypsumBoardToUser(Session session, Map<String, LinkedHashSet<String>> gypsumMap) {
		try {
			printMaterialName(KMConstants.GYPSUM_BOARD);

			 gypsum_Board = processingGypsumBoard(session, gypsumMap);
			session.save(gypsum_Board);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gypsum_Board;
	}

	private Insulation giveSuggestionForInsulationToUser(Session session,
			LinkedHashMap<String, LinkedHashSet<String>> insulationMap) {
		try {
			printMaterialName(KMConstants.INSULATION);
			 insulation = processingInsulation(session, insulationMap);
			session.save(insulation);
		} catch (Exception e) {

		}
		return insulation;
	}

	private Insulation giveSuggestionForInsulationToUser(Session session,
			LinkedHashMap<String, LinkedHashSet<String>> insulationMap, String type) {
		try {
			printMaterialName(type);
			 insulation = processingInsulation(session, insulationMap, type);
			session.save(insulation);
		} catch (Exception e) {

		}
		return insulation;
	}

	private String askingMissingValuesToInsert(String key, Map.Entry<String, LinkedHashSet<String>> mapEntry) {
		String optionKey = "";
		String value = "";
		try {
			Map<String, String> mapData = Collections.EMPTY_MAP;
			do {
				System.out.println("Choose " + key + " from following option by entering Option Name.");
				mapData = showOptions(mapEntry.getValue());
				System.out.print("Enter Option:  ");
				optionKey = scanner.next();
				if (mapData.containsKey(optionKey)) {
					value = mapData.get(optionKey);
				}
			} while (!mapData.containsKey(optionKey));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	private Map<String, String> showOptions(LinkedHashSet<String> dataSet) {
		Map<String, String> mapData = new HashMap<>();
		Iterator<String> iterator = dataSet.iterator();
		char start = 'a';
		while (iterator.hasNext()) {
			String value = iterator.next();
			System.out.println(start + ". " + value);
			mapData.put(Character.toString(start), value);
			start++;
		}
		System.out.println();
		return mapData;
	}

	public LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> getKnowledgeMap() {
		return owlOntologyParser.getKnowledgeModalMap();
	}

	public Map<String, LinkedHashSet<String>> getEnquivalentMap() {
		return owlOntologyParser.getEquivalentClassesMap();
	}

	private void getData(String hasKey, List<Object> hasValues) {
		Object valueList = "";
		for (int i = 0; i < hasValues.size(); i++) {
			if (i == 0) {
				valueList = hasValues.get(0);
			} else {
				valueList += ", " + hasValues.get(i);
			}
		}
		System.out.print("\t  " + hasKey + ":\t\t");
		System.out.println(valueList);
	}

	private void printMaterialName(String name) {
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("For " + name + ": ");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

	public Map<String, LinkedHashMap<String, LinkedHashSet<String>>> getKnowledgeModalMap() {
		return knowledgeModalMap;
	}

	public Long getHeightOfComponent() {
		return Math.round(Double.valueOf(heightOfComponent))+1;
	}

	public Plywood_Sheathing getPlywood_Sheathing() {
		return plywood_Sheathing;
	}

	public void setPlywood_Sheathing(Plywood_Sheathing plywood_Sheathing) {
		this.plywood_Sheathing = plywood_Sheathing;
	}

	public Gypsum_Board getGypsum_Board() {
		return gypsum_Board;
	}

	public void setGypsum_Board(Gypsum_Board gypsum_Board) {
		this.gypsum_Board = gypsum_Board;
	}

	public Stud getStud() {
		return stud;
	}

	public void setStud(Stud stud) {
		this.stud = stud;
	}

	public Insulation getInsulation() {
		return insulation;
	}

	public void setInsulation(Insulation insulation) {
		this.insulation = insulation;
	}

	public List<Material> getMaterials() {
		return materialCosts;
	}

	public Map<String, Material> getMaterialMap() {
		return materialCostMap;
	}
	
	

}
