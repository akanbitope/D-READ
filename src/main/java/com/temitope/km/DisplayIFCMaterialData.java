package com.temitope.km;

import java.util.List;

import com.temitope.dao.EntityDao;
import com.temitope.owl.model.FloorMaterial;
import com.temitope.owl.model.Insulation;
import com.temitope.owl.model.WallMaterial;

public class DisplayIFCMaterialData {
	EntityDao entityDao;

	public DisplayIFCMaterialData(EntityDao entityDao) {
		this.entityDao = entityDao;
	}

	public void printWall() {
		printHeading("WALL");
		printUpperLine();
		System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s%-30s%-40s%-40s%-40s", "Height", "Length", "Width",
				"Wall_Area", "Wall_Net_Area", "Wall_Volume", "Wall_Net_Volume", "Total_Area_Of_Opening",
				"Total_Volume_Of_Opening","Material_With_Thickness","Ifc_MaterialLayer_Set"));
		printUpperLine();
		for (WallMaterial i : entityDao.getWalls()) {
			System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s%-20s%-25s%-40s%-35s%-40s%-40s", i.getHeightOfWall(),
					i.getLengthOfWall(), i.getWidthOfWall(), i.getAreaOfWall(), i.getNetAreaOfWall(),
					i.getVolumeOfWall(), i.getNetVolumeOfWall(), i.getTotalAreaOfOpening(),
					i.getTotalVolumeOfOpening(),i.getMaterialsWithThickness(),i.getIfcMaterialLayerSet()));
		}
		printLowerLine();
	}

	public void printFloor() {
		printHeading("FLOOR");
		printUpperLine();
		System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s%-25s", "Height", "Length", "Width", "Floor_Area",
				"Floor_Volume", "Material_With_Thickness"));
		printUpperLine();
		for (FloorMaterial i : entityDao.getFloors()) {
			System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s%-20s", i.getHeightOfFloor(),
					i.getLengthOfFloor(), i.getWidthOfFloor(), i.getAreaOfFloor(), i.getVolumeOfFloor(),
					i.getMaterialsWithThickness()));

		}
		printLowerLine();
	}

	public void printHeading(String name) {
		System.out.println(
				"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("                                            " + name
				+ "                                              ");
		System.out.println(
				"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

	public void printUpperLine() {
		System.out.println(
				"--------------------------------------------------------------------------------------------------");
	}

	public void printLowerLine() {
		System.out.println(
				"--------------------------------------------------------------------------------------------------\n\n\n");
	}

}
