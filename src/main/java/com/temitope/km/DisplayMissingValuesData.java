package com.temitope.km;

import com.temitope.dao.EntityDao;
import com.temitope.owl.model.Gypsum_Board;
import com.temitope.owl.model.Insulation;
import com.temitope.owl.model.Plywood_Sheathing;
import com.temitope.owl.model.Stud;

public class DisplayMissingValuesData {
	EntityDao entityDao;

	public DisplayMissingValuesData(EntityDao entityDao) {
		this.entityDao = entityDao;
	}

	public void printInsulation() {
		printHeading("Insulation Records");
		System.out.println(String.format("%-15s%-25s%-15s%-15s%-15s%-15s%-15s", "Thickness", "Name", "Number", "Faced",
				"CF", "Type", "Width"));
		printUpperLine();
		for (Insulation i : entityDao.getInsulations()) {
			System.out.println(String.format("%-15s%-25s%-15s%-15s%-15s%-15s%-15s", i.getThickness(), i.getName(),
					i.getNumber(), i.getFaced(), i.getFiberglassCF(), i.getFiberglassType(), i.getFiberglassWidth()));
		}
		printLowerLine();

	}

	public void printStud() {
		printHeading("Stud Records");
		System.out.println(String.format("%-15s%-25s%-15s%-15s", "Thickness", "Height", "Spacing", "Nailed"));
		printUpperLine();
		for (Stud i : entityDao.getStuds()) {
			System.out.println(String.format("%-15s%-25s%-15s%-15s", i.getThickness(), i.getHeight(), i.getSpacing(),
					i.getNailed()));
		}
		printLowerLine();
	}

	public void printGypsum() {
		printHeading("Gypsum Board Records");
		System.out.println(String.format("%-25s%-25s%-35s%-35s", "Thickness", "Name", "Finish", "Tool"));
		printUpperLine();
		for (Gypsum_Board i : entityDao.getGypsums()) {
			System.out.println(
					String.format("%-25s%-25s%-35s%-35s", i.getThickness(), i.getName(), i.getFinish(), i.getTool()));
		}
		printLowerLine();
	}

	public void printSheathing() {
		printHeading("Plywood-Sheathing Records");
		System.out.println(String.format("%-15s%-25s%-15s%-15s%-15s%-15s", "Thickness", "Name", "Nailed", "Laid",
				"Berrier", "Gypsum Type"));
		printUpperLine();
		for (Plywood_Sheathing i : entityDao.getSheathings()) {
			System.out.println(String.format("%-15s%-25s%-15s%-15s%-15s%-15s%-15s", i.getThickness(), i.getName(),
					i.getNailed(), i.getLaid(), i.getBarrier(), i.getLocation(), i.getGypsumType()));
		}
		printLowerLine();
	}

	public void printUpperLine() {
		System.out.println(
				"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

	public void printLowerLine() {
		System.out.println(
				"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
	}

	public void printHeading(String name) {
		System.out.println(
				"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("                                            " + name
				+ "                                              ");
		System.out.println(
				"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

}
