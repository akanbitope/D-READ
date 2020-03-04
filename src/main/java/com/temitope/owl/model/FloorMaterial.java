package com.temitope.owl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author cis
 *
 */
@Entity
public class FloorMaterial {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int floorMaterialId;
	@Column(nullable = true)
	private String heightOfFloor;
	@Column(nullable = true)
	private String lengthOfFloor;
	@Column(nullable = true)
	private String WidthOfFloor;
	@Column(nullable = true)
	private String AreaOfFloor;
	@Column(nullable = true)
	private String volumeOfFloor;
	@Column(nullable = true)
	private String totalCost;
	@Column(nullable = true)
	private String materialsWithThickness;
	@Column(nullable = true)
	private String ifcMaterialLayerSet;
	
	
	public int getFloorMaterialId() {
		return floorMaterialId;
	}
	public void setFloorMaterialId(int floorMaterialId) {
		this.floorMaterialId = floorMaterialId;
	}
	public String getHeightOfFloor() {
		return heightOfFloor;
	}
	public void setHeightOfFloor(String heightOfFloor) {
		this.heightOfFloor = heightOfFloor;
	}
	public String getLengthOfFloor() {
		return lengthOfFloor;
	}
	public void setLengthOfFloor(String lengthOfFloor) {
		this.lengthOfFloor = lengthOfFloor;
	}
	public String getWidthOfFloor() {
		return WidthOfFloor;
	}
	public void setWidthOfFloor(String widthOfFloor) {
		WidthOfFloor = widthOfFloor;
	}
	public String getAreaOfFloor() {
		return AreaOfFloor;
	}
	public void setAreaOfFloor(String areaOfFloor) {
		AreaOfFloor = areaOfFloor;
	}
	public String getVolumeOfFloor() {
		return volumeOfFloor;
	}
	public void setVolumeOfFloor(String volumeOfFloor) {
		this.volumeOfFloor = volumeOfFloor;
	}
	public String getMaterialsWithThickness() {
		return materialsWithThickness;
	}
	public void setMaterialsWithThickness(String materialsWithThickness) {
		this.materialsWithThickness = materialsWithThickness;
	}
	public String getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}
	
	public String getIfcMaterialLayerSet() {
		return ifcMaterialLayerSet;
	}
	public void setIfcMaterialLayerSet(String ifcMaterialLayerSet) {
		this.ifcMaterialLayerSet = ifcMaterialLayerSet;
	}
	@Override
	public String toString() {
		return "FloorMaterial [floorMaterialId=" + floorMaterialId + ", heightOfFloor=" + heightOfFloor
				+ ", lengthOfFloor=" + lengthOfFloor + ", WidthOfFloor=" + WidthOfFloor + ", AreaOfFloor=" + AreaOfFloor
				+ ", volumeOfFloor=" + volumeOfFloor + ", totalCost=" + totalCost + ", materialsWithThickness="
				+ materialsWithThickness + "]";
	}
	
	

}
