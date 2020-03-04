package com.temitope.owl.parser.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;

import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLQuantifiedObjectRestriction;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;

import com.temitope.ifc.common.KMConstants;
import com.temitope.ifc.parser.IFCFileParserUtil;
import com.temitope.owl.catalogue.OwlElement;
import com.temitope.owl.catalogue.OwlModal;
import com.temitope.owl.parser.util.OwlManagerUtil;

public class OwlOntologyParser {
	private LinkedHashMap<String, LinkedHashSet<String>> SubClassesMap;
	private LinkedHashMap<String, LinkedHashSet<String>> equivalentClassesMap;
	private LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> knowledgeModalMap;
	private LinkedHashMap<String, LinkedHashSet<String>> owlThicknessMap;
	private LinkedHashMap<String, LinkedHashSet<String>> ifcThicknessMap;
	private LinkedHashMap<String, LinkedHashSet<String>> insulationThickness;

	public OwlOntologyParser(LinkedHashMap<String, LinkedHashSet<String>> thicknessMap) {
		insulationThickness = new LinkedHashMap<>();
		knowledgeModalMap = new LinkedHashMap<>();
		SubClassesMap = new LinkedHashMap<>();
		equivalentClassesMap = new LinkedHashMap<>();
		owlThicknessMap = extractingThickness(thicknessMap);
		this.ifcThicknessMap = ifcThicknessMap;
	}

	public void parse(OWLOntology ontology) {
		for (final OWLClass oc : ontology.getClassesInSignature()) {
			String owlClassString = extractValue(oc.toString());
			// System.out.println("Class: " + owlClassString);
			for (OWLAxiom axiom : ontology.getAxioms(oc)) {
				String axiomString = extractValue(axiom.toString());
				if (axiomString.startsWith("SubClassOf") && !axiomString.contains("ObjectSomeValuesFrom")) {
					extractSubClassOfData(axiomString);
				}
				// System.out.println("\tAxiom: " + axiomString);
				if (axiomString.startsWith("EquivalentClasses(") && axiomString.endsWith(" )")) {
					extractEquivalentClassesData(axiomString);
				}
				axiom.accept(new OWLObjectVisitorAdapter() {

					public void visit(OWLSubClassOfAxiom subClassAxiom) {
						subClassAxiom.getSuperClass().accept(new OWLObjectVisitorAdapter() {
							public void visit(OWLObjectSomeValuesFrom someValuesFromAxiom) {
								printQuantifiedRestriction(oc, someValuesFromAxiom);
							}
						});
					}
				});

			}

			// System.out.println();
		}
		setInsulationThickness();
		populatingDataIntoKnowledgeModalMap();

	}

	private void printQuantifiedRestriction(OWLClass oc, OWLQuantifiedObjectRestriction restriction) {
		String propertyString = extractValue(restriction.getProperty().toString());
		String classString = extractValue(oc.toString()).trim();
		String objectString = extractValue(restriction.getFiller().toString()).replaceAll("_", " ").trim();
		if (propertyString.startsWith(KMConstants.IS)) {
			propertyString = propertyString.substring(2).trim();
		} else if (propertyString.startsWith(KMConstants.HAS)) {
			propertyString = propertyString.substring(3).trim();

		} else if (propertyString.startsWith(KMConstants.USE)) {
			propertyString = propertyString.substring(3).trim();
		}
		createKnowledgeModal(knowledgeModalMap, new OwlElement(classString, propertyString, objectString));
	}

	private void setInsulationThickness() {
		LinkedHashSet<String> fiberglassThickness = getEquivalentClassesMap().get(KMConstants.Fiberglass);
		LinkedHashSet<String> foamglassThickness = getEquivalentClassesMap().get(KMConstants.Foamglass);
		LinkedHashSet<String> isocyanuratesThickness = getEquivalentClassesMap().get(KMConstants.Isocyanurate);
		LinkedHashSet<String> perlitesThickness = getEquivalentClassesMap().get(KMConstants.Perlite);
		LinkedHashSet<String> extruded_PolystrenesThickness = getEquivalentClassesMap()
				.get(KMConstants.Extruded_Polystrene);
		LinkedHashSet<String> expanded_PolystrenesThickness = getEquivalentClassesMap()
				.get(KMConstants.Expanded_Polystrene);
		LinkedHashMap<String, LinkedHashSet<String>> thicknessMap = new LinkedHashMap();
		thicknessMap.put(KMConstants.Fiberglass, fiberglassThickness);
		thicknessMap.put(KMConstants.Foamglass, foamglassThickness);
		thicknessMap.put(KMConstants.Isocyanurate, isocyanuratesThickness);
		thicknessMap.put(KMConstants.Perlite, perlitesThickness);
		thicknessMap.put(KMConstants.Extruded_Polystrene, extruded_PolystrenesThickness);
		thicknessMap.put(KMConstants.Expanded_Polystrene, expanded_PolystrenesThickness);
		setInsulationThickness(thicknessMap);

	}

	private void createKnowledgeModal(Map<String, LinkedHashMap<String, LinkedHashSet<String>>> knowledgeModalMap,
			OwlElement material) {
		if (knowledgeModalMap.isEmpty() || !knowledgeModalMap.containsKey(material.getClassName())) {
			LinkedHashMap<String, LinkedHashSet<String>> hasMap = new LinkedHashMap();
			LinkedHashSet<String> hasSet = new LinkedHashSet<>();
			hasSet.add(material.getObjectName());
			hasMap.put(material.getPropertyName(), hasSet);
			knowledgeModalMap.put(material.getClassName(), hasMap);
		} else if (!knowledgeModalMap.isEmpty() || knowledgeModalMap.containsKey(material.getClassName())) {
			LinkedHashMap<String, LinkedHashSet<String>> tempMap = new LinkedHashMap();
			for (Entry<String, LinkedHashMap<String, LinkedHashSet<String>>> dataMap : knowledgeModalMap.entrySet()) {
				if (dataMap.getKey().equals(material.getClassName())) {
					if (dataMap.getValue().containsKey(material.getPropertyName())) {
						tempMap.putAll(dataMap.getValue());
						for (Entry<String, LinkedHashSet<String>> hasMap : dataMap.getValue().entrySet()) {
							if (hasMap.getKey().equals(material.getPropertyName())
									&& dataMap.getKey().equals(material.getClassName())) {
								hasMap.getValue().add(material.getObjectName());
								tempMap.put(hasMap.getKey(), hasMap.getValue());
								knowledgeModalMap.put(dataMap.getKey(), tempMap);
							}
						}
					} else if (!dataMap.getValue().containsKey(material.getPropertyName())) {
						Map<String, LinkedHashSet<String>> map = knowledgeModalMap.get(material.getClassName());
						LinkedHashSet<String> sets = new LinkedHashSet();
						sets.add(material.getObjectName());
						tempMap.putAll(map);
						tempMap.put(material.getPropertyName(), sets);
						knowledgeModalMap.put(dataMap.getKey(), tempMap);

					}
				}

			}
		}
	}

	private static String extractValue(String value) {
		String string = value.replaceAll("<http://www.semanticweb.org/pixie/ontologies/2018/9/KnowledgeModel#", "");
		return string.replaceAll(">", "");
	}

	public LinkedHashMap<String, LinkedHashSet<String>> getEquivalentClassesMap() {
		return equivalentClassesMap;
	}

	public LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> getKnowledgeModalMap() {
		return knowledgeModalMap;
	}

	private void extractEquivalentClassesData(String inputString) {
		int startIndex = OwlManagerUtil.Constant.equivalentClasses.length();
		int endIndex = inputString.indexOf(OwlManagerUtil.Constant.spaceCloseParenthesis);
		String data = inputString.substring(startIndex, endIndex).trim();
		int end = data.indexOf(OwlManagerUtil.Constant.objectUnionOf);
		String missingElement = data.substring(0, end).trim();
		String objectUnionOfValues = data.substring(data.indexOf(OwlManagerUtil.Constant.openParenthesis) + 1,
				data.indexOf(OwlManagerUtil.Constant.closeParenthesis)).trim();
		String missingValues[] = objectUnionOfValues.split(" ");
		LinkedHashSet<String> missingValuesSet = new LinkedHashSet<>();
		for (int i = 0; i < missingValues.length; i++) {
			missingValuesSet.add(missingValues[i]);
		}
		equivalentClassesMap.put(missingElement, missingValuesSet);
	}

	private void populatingDataIntoKnowledgeModalMap() {
		LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> knowledgeModalMapTemp = new LinkedHashMap<>();
		knowledgeModalMapTemp.putAll(knowledgeModalMap);
		knowledgeModalMapTemp.forEach((k, v) -> {
			LinkedHashMap<String, LinkedHashSet<String>> valueMap = v;
			if (!k.equals(KMConstants.HOUSE)) {
				LinkedHashSet<String> levelSet = getDataSetByKeyFromEquivalentClass(KMConstants.LEVEL);
				if (levelSet.contains("Basement")) {
					levelSet.remove("Basement");
				}
				valueMap.put(KMConstants.LEVEL, levelSet);
				// valueMap.put("Location", getDataSetByKeyFromEquivalentClass("Location"));
			}
			if ((k.equals(OwlModal.House.Wall.Gypsum_Board.toString()))
					|| (k.equals(OwlModal.House.Wall.Gypsum_Drywall.toString()))) {
				populatingGypsumBoardDataIntoKM(valueMap);

			}
			if ((k.equals(OwlModal.House.Wall.Insulation.toString()))) {
				populatingInsulatinDataIntoKM(valueMap);

			}
			if (k.equals(OwlModal.House.Wall.Stud.toString())) {
				populatingStudDataIntoKM(valueMap);

			}
			if ((k.equals(OwlModal.House.Wall.Plywood_Sheathing.toString()))
					|| (k.equals(OwlModal.House.Wall.Sheathing.toString()))) {
				populatingSheathingDataIntoKM(valueMap);

			}
			
			
			knowledgeModalMap.put(k, valueMap);
		});

	}

	private void populatingSheathingDataIntoKM(LinkedHashMap<String, LinkedHashSet<String>> valueMap) {
		valueMap.put(KMConstants.BARRIER, getDataSetByKeyFromSubclass(KMConstants.BARRIER));
		valueMap.put(KMConstants.NAILED, getDataSetByKeyFromEquivalentClass(KMConstants.NAILED));
		LinkedHashSet<String> gypsum_type = new LinkedHashSet<>();
		LinkedHashSet<String> names = valueMap.get(KMConstants.NAME);
		Iterator<String> iterator = names.iterator();
		while (iterator.hasNext()) {
			String v = iterator.next();
			if (v.contains("Gypsum") || v.contains("gypsum") || v.contains("GYPSUM")) {
				gypsum_type.add(v);
			}
		}
		valueMap.put(KMConstants.GYPSUM_TYPE, gypsum_type);
	}

	private void populatingGypsumBoardDataIntoKM(LinkedHashMap<String, LinkedHashSet<String>> valueMap) {
		LinkedHashSet<String> thickness = valueMap.get(KMConstants.THICKNESS);
		LinkedHashSet<String> levels = valueMap.get(KMConstants.LEVEL);
		LinkedHashSet<String> finishs = valueMap.get(KMConstants.FINISH);
		LinkedHashSet<String> names = valueMap.get(KMConstants.NAME);
        valueMap.clear();
        valueMap.put(KMConstants.NAME, names);
        valueMap.put(KMConstants.FINISH, finishs);
		valueMap.put(KMConstants.TOOL, getDataSetByKeyFromEquivalentClass(KMConstants.TOOL));
		/*if (!getOwlThicknessMap().get(OwlModal.House.Wall.Gypsum_Board.toString()).isEmpty()) {
			valueMap.remove(KMConstants.THICKNESS);
			valueMap.put(KMConstants.THICKNESS, getOwlThicknessMap().get(OwlModal.House.Wall.Gypsum_Board.toString()));
		} else {
			valueMap.put(KMConstants.THICKNESS, thickness);
		}*/
        valueMap.put(KMConstants.LEVEL, levels);
		valueMap.put(KMConstants.THICKNESS, getOwlThicknessMap().get(OwlModal.House.Wall.Gypsum_Board.toString()));

	}

	private void populatingInsulatinDataIntoKM(LinkedHashMap<String, LinkedHashSet<String>> valueMap) {
		LinkedHashSet<String> names = valueMap.get(KMConstants.NAME);
		LinkedHashSet<String> thicknessFromOwl = valueMap.get(KMConstants.THICKNESS);
		LinkedHashSet<String> numbers = valueMap.get(KMConstants.NUMBER);
		LinkedHashSet<String> levels = valueMap.get(KMConstants.LEVEL);
		valueMap.clear();
		valueMap.put(KMConstants.NAME, names);
		valueMap.put(KMConstants.FACED, getDataSetByKeyFromEquivalentClass(KMConstants.FACED));
		// valueMap.put(KMConstants.BARRIER,
		// getDataSetByKeyFromSubclass(KMConstants.BARRIER));
		valueMap.put(KMConstants.LEVEL, levels);
		valueMap.put(KMConstants.CF, getDataSetByKeyFromEquivalentClass(KMConstants.CF));
		valueMap.put(KMConstants.TYPE, getDataSetByKeyFromEquivalentClass(KMConstants.TYPE));
		// valueMap.put(KMConstants.WIDTH,
		// getDataSetByKeyFromEquivalentClass(KMConstants.WIDTH));

	/*	if (!getOwlThicknessMap().get(OwlModal.House.Wall.Insulation.toString()).isEmpty()) {
			valueMap.remove(KMConstants.THICKNESS);
			valueMap.put(KMConstants.THICKNESS, getOwlThicknessMap().get(OwlModal.House.Wall.Insulation.toString()));
		} else {
			valueMap.put(KMConstants.THICKNESS, thicknessFromOwl);
		}*/
		valueMap.put(KMConstants.THICKNESS, getOwlThicknessMap().get(KMConstants.BATT_INSULATION));
		valueMap.put(KMConstants.NUMBER, numbers);
		
	}

	private void populatingStudDataIntoKM(LinkedHashMap<String, LinkedHashSet<String>> valueMap) {
		valueMap.put(KMConstants.NAILED, getDataSetByKeyFromEquivalentClass(KMConstants.NAILED));
		/*if (!getOwlThicknessMap().get(OwlModal.House.Wall.Stud.toString()).isEmpty()) {
			valueMap.remove(KMConstants.THICKNESS);
			valueMap.put(KMConstants.THICKNESS, getOwlThicknessMap().get(OwlModal.House.Wall.Stud.toString()));
		}*/
		valueMap.put(KMConstants.THICKNESS, getOwlThicknessMap().get(OwlModal.House.Wall.Stud.toString()));

	}

	private void extractSubClassOfData(String input) {
		int e = OwlManagerUtil.Constant.subClassOf.length();
		String s1 = input.substring(e, input.length() - 1).trim();
		String values[] = s1.split(OwlManagerUtil.Constant.space);
		String propertyKey = values[1].trim();
		String propertyValue = values[0];

		if (SubClassesMap.isEmpty() || !SubClassesMap.containsKey(propertyKey)) {
			LinkedHashSet<String> hasSet = new LinkedHashSet<>();
			hasSet.add(propertyValue);
			SubClassesMap.put(propertyKey, hasSet);
		} else if (!SubClassesMap.isEmpty() || SubClassesMap.containsKey(propertyKey)) {
			Map<String, LinkedHashSet<String>> tempMap = new HashMap<>();
			LinkedHashSet<String> setData = null;
			for (Entry<String, LinkedHashSet<String>> dataMap : SubClassesMap.entrySet()) {
				if (dataMap.getKey().equals(propertyKey)) {
					dataMap.getValue().add(propertyValue);
					setData = dataMap.getValue();
				}

			}
			SubClassesMap.put(propertyKey, setData);
		}
	}

	private LinkedHashMap<String, LinkedHashSet<String>> extractingThickness(LinkedHashMap<String, LinkedHashSet<String>> thicknessMap) {
		if (thicknessMap.containsKey(OwlManagerUtil.Wall.GYPSUM_WALL_BOARD)) {
			thicknessMap.put(OwlModal.House.Wall.Gypsum_Board.toString(),
					thicknessMap.get(OwlManagerUtil.Wall.GYPSUM_WALL_BOARD));
			thicknessMap.remove(OwlManagerUtil.Wall.GYPSUM_WALL_BOARD);
		}
		return thicknessMap;
	}

	public LinkedHashSet<String> getObjectUnionOf(String inputString) {
		if (inputString == null || inputString.isEmpty() || !equivalentClassesMap.containsKey(inputString)) {
			return new LinkedHashSet<>();
		} else {
			return equivalentClassesMap.get(inputString);
		}
	}

	public Map<String, LinkedHashSet<String>> getSubClassesMap() {
		return SubClassesMap;
	}

	public LinkedHashSet<String> getDataSetByKeyFromEquivalentClass(String key) {
		return getEquivalentClassesMap().get(key);
	}

	public LinkedHashSet<String> getDataSetByKeyFromSubclass(String key) {
		return getSubClassesMap().get(key);
	}

	public LinkedHashSet<String> getDataSetOfWall() {
		return getSubClassesMap().get("Wall");
	}

	public LinkedHashSet<String> getDataSetOfFloor() {
		return getSubClassesMap().get("Floor");
	}

	public Map<String, LinkedHashSet<String>> getIfcThicknessMap() {
		return ifcThicknessMap;
	}

	public LinkedHashMap<String, LinkedHashSet<String>> getOwlThicknessMap() {
		return owlThicknessMap;
	}

	public Map<String, LinkedHashSet<String>> getInsulationThickness() {
		return insulationThickness;
	}

	public void setInsulationThickness(LinkedHashMap<String, LinkedHashSet<String>> insulationThickness) {
		this.insulationThickness = insulationThickness;
	}

}
