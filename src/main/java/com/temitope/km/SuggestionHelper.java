package com.temitope.km;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.temitope.dao.BaseDAOHibernate;
import com.temitope.ifc.common.EntityHolderHelper;
import com.temitope.ifc.common.HibernateRegistry;
import com.temitope.ifc.common.KMConstants;
import com.temitope.model.Material;
import com.temitope.nlp.NlpProcessor;
import com.temitope.owl.model.Gypsum_Board;
import com.temitope.owl.model.Insulation;
import com.temitope.owl.model.Stud;
import com.temitope.owl.parser.impl.OwlOntologyParser;

public class SuggestionHelper extends BaseDAOHibernate {
	private KnowledgeModal knowledgeModal;
	private Session session;
	private Gypsum_Board gypsum_Board;
	private Insulation insulation;
	private Stud stud;

	public SuggestionHelper(KnowledgeModal knowledgeModal) {
		this.knowledgeModal = knowledgeModal;
		session = HibernateRegistry.getInstance().getSession();
		session.beginTransaction();
	}

	public void askSuggestionForKnowledgeModal(OwlOntologyParser owlOntologyParser, Object component) {
		boolean flag = false;
		LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> km = knowledgeModal.getKnowledgeMap();
		if (owlOntologyParser.getOwlThicknessMap().containsKey(KMConstants.BATT_INSULATION)) {
			modifyKnowledgeModalMap(km, KMConstants.BATT_INSULATION);
		} else if (owlOntologyParser.getOwlThicknessMap().containsKey(KMConstants.BLANKET_INSULATION)) {
			modifyKnowledgeModalMap(km, KMConstants.BLANKET_INSULATION);
		}

		for (Map.Entry<String, LinkedHashSet<String>> materialThicknessKeys : owlOntologyParser.getOwlThicknessMap()
				.entrySet()) {
			String thickness = materialThicknessKeys.getValue().iterator().next();
			if (km.containsKey(materialThicknessKeys.getKey())) {
				knowledgeModal.suggession(session, km, materialThicknessKeys.getKey());
			}
		}
		session.save(component);
		Map<String, Material> materialCostMap = knowledgeModal.getMaterialMap();
		gypsum_Board = knowledgeModal.getGypsum_Board();
		insulation = knowledgeModal.getInsulation();
		stud = knowledgeModal.getStud();
		
		
		List<Material> materialCosts = getMaterials();
		for (Map.Entry<String, LinkedHashSet<String>> materialThicknessKeys : owlOntologyParser.getOwlThicknessMap()
				.entrySet()) {
			if (materialCostMap.containsKey(materialThicknessKeys.getKey())) {
				if (!isAlreadyExists(getMaterials(), materialCostMap.get(materialThicknessKeys.getKey()))) {
					session.save(materialCostMap.get(materialThicknessKeys.getKey()));
				}
			}
		}

		if (!materialCosts.isEmpty() || materialCosts.size() != 0) {
			for (Material materialCost : materialCosts) {
				if (materialCost.getMaterial().equals(KMConstants.GYPSUM_BOARD)) {
					if (materialCost.getName().equals(gypsum_Board.getName())
							&& materialCost.getThickness().equals(gypsum_Board.getThickness())
							&& materialCost.getFinish().equals(gypsum_Board.getFinish())) {
						gypsum_Board.setCost(materialCost.getCost());
						materialCost.setTotalCost(Double.valueOf(gypsum_Board.getCost() * gypsum_Board.getNetArea()));
						session.saveOrUpdate(materialCost);
					}
				} else if (materialCost.getMaterial().equals(KMConstants.BATT_INSULATION)) {
					if (materialCost.getName().equals(insulation.getName())
							&& materialCost.getNumber().equals(insulation.getNumber())
							&& materialCost.getThickness().equals(insulation.getThickness())) {
						insulation.setCost(materialCost.getCost());
						materialCost.setTotalCost(insulation.getCost() * insulation.getNetArea());
						session.saveOrUpdate(materialCost);
					}
				} else if (materialCost.getMaterial().equals(KMConstants.BLANKET_INSULATION)) {
					if (materialCost.getName().equals(insulation.getName())
							&& materialCost.getNumber().equals(insulation.getNumber())
							&& materialCost.getThickness().equals(insulation.getThickness())) {
						insulation.setCost(materialCost.getCost());
						materialCost.setTotalCost(insulation.getCost() * insulation.getNetArea());
						session.saveOrUpdate(materialCost);
					}
				} else if (materialCost.getMaterial().equals(KMConstants.INSULATION)) {
					if (materialCost.getName().equals(insulation.getName())
							&& materialCost.getThickness().equals(insulation.getThickness())
									&& materialCost.getNumber().equals(insulation.getNumber())) {
						insulation.setCost(materialCost.getCost());
						materialCost.setTotalCost(insulation.getCost() * insulation.getNetArea());
						session.saveOrUpdate(materialCost);
					}
				} else if (materialCost.getMaterial().equals(KMConstants.STUD)) {
					if (materialCost.getHeight().equals(stud.getHeight()) && materialCost.getThickness().equals(stud.getThickness()) && materialCost.getNailed().equals(stud.getNailed())) {
						stud.setCost(materialCost.getCost());
						materialCost.setTotalCost(stud.getCost() * stud.getNetArea());
						session.saveOrUpdate(materialCost);

					}
				}
			}
		}
/*		System.out.println("\n\n");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Summary of Materials");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		printGypsumBoard(gypsum_Board);
		printInsulation(insulation);
		printStud(stud);*/
		new NlpProcessor().processNlp(getMaterials());
		flag = true;
		HibernateRegistry.getInstance().closeSession(session, flag);
		printMessageToUser();
	}

	private boolean isAlreadyExists(List<Material> mcList, Material materialCost) {
		boolean matched = false;
		for (Material mCost : mcList) {
			if (mCost.getMaterial().equals(materialCost.getMaterial())
					&& materialCost.getMaterial().equals(KMConstants.GYPSUM_BOARD)) {
				if (mCost.getName().equals(materialCost.getName())
						&& mCost.getThickness().equals(materialCost.getThickness())) {
					matched = true;
				}
			} else if (mCost.getMaterial().equals(materialCost.getMaterial())
					&& materialCost.getMaterial().equals(KMConstants.INSULATION)) {
				if (mCost.getName().equals(materialCost.getName())
						&& mCost.getThickness().equals(materialCost.getThickness())
								&& mCost.getNumber().equals(materialCost.getNumber()) ) {
					matched = true;
				}
			}  else if (mCost.getMaterial().equals(materialCost.getMaterial())
					&& materialCost.getMaterial().equals(KMConstants.BATT_INSULATION)) {
				if (mCost.getName().equals(materialCost.getName())
						&& mCost.getThickness().equals(materialCost.getThickness())
						&& mCost.getNumber().equals(materialCost.getNumber()) ) {
					matched = true;
				}
			}  else if (mCost.getMaterial().equals(materialCost.getMaterial())
					&& materialCost.getMaterial().equals(KMConstants.BLANKET_INSULATION)) {
				if (mCost.getName().equals(materialCost.getName())
						&& mCost.getThickness().equals(materialCost.getThickness())
						&& mCost.getNumber().equals(materialCost.getNumber()) ) {
					matched = true;
				}
			} else if (mCost.getMaterial().equals(materialCost.getMaterial())
					&& materialCost.getMaterial().equals(KMConstants.STUD)) {
				if (mCost.getThickness().equals(materialCost.getThickness())
						&& mCost.getHeight().equals(materialCost.getHeight())
						&& mCost.getNailed().equals(materialCost.getNailed())
						) {
					matched = true;
				}
			}
		}
		return matched;
	}

	private List<Material> getMaterials() {
		return session.createCriteria(Material.class).list();
	}

	private void modifyKnowledgeModalMap(LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> km,
			String type) {
		LinkedHashMap<String, LinkedHashSet<String>> data = km.get(KMConstants.INSULATION);
		if (km.containsKey(KMConstants.INSULATION)) {
			km.remove(KMConstants.INSULATION);
			km.put(type, data);
		}

	}

	public void printGypsumBoard(Gypsum_Board gypsum_Board) {
		System.out.println("GYPSUM BOARD");
		System.out.println("------------------------");
		System.out.println("\tName:\t\t" + gypsum_Board.getName());
		System.out.println("\tThickness:\t\t" + gypsum_Board.getThickness());
		System.out.println("\tFinish:\t\t" + gypsum_Board.getFinish());
		System.out.println("\tTool:\t\t" + gypsum_Board.getTool());
		System.out.println("\tCost:\t\t" + gypsum_Board.getCost());
		System.out.println("\tTotal Cost:\t\t" + gypsum_Board.getNetArea() * gypsum_Board.getCost());
		System.out.println("\n\n");
	}

	public void printInsulation(Insulation insulation) {
		System.out.println("\nINSULATION");
		System.out.println("---------------------------");
		System.out.println("\tName:\t\t" + insulation.getName());
		System.out.println("\tThickness:\t\t" + insulation.getThickness());
		System.out.println("\tNumber:\t\t" + insulation.getNumber());
		System.out.println("\tFaced:\t\t" + insulation.getFaced());
		if (insulation.getName().equals(KMConstants.Fiberglass)) {
			System.out.println("\tFiberglass Type:\t\t" + insulation.getFiberglassType());
			System.out.println("\tFiberglass CF:\t\t" + insulation.getFiberglassCF());
		}
		System.out.println("\tCost:\t\t" + insulation.getCost());
		System.out.println("\tTotal Cost:\t\t" + insulation.getNetArea() * insulation.getCost());
		System.out.println("\n\n");

	}

	public void printStud(Stud stud) {
		System.out.println("STUD");
		System.out.println("------------------------");
		System.out.println("\tHeight\t:\t" + stud.getHeight());
		System.out.println("\tThickness:\tt" + stud.getThickness());
		System.out.println("\tSpaciing:\t\t" + stud.getSpacing());
		System.out.println("\tNailed:\t\t" + stud.getNailed());
		System.out.println("\tCost:\t\t" + stud.getCost());
		System.out.println("\tTotal Cost:\t\t" + stud.getNetArea() * stud.getCost());
		System.out.println("\n\n");

	}

	public void printMessageToUser() {
		System.out.println("\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("All Missing Values has been picking up by user successfully");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
	}
}
