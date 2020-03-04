package com.temitope.owl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class WallMaterial {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int wallMaterialId;
	@Column(nullable = true)
	private String heightOfWall;
	@Column(nullable = true)
	private String lengthOfWall;
	@Column(nullable = true)
	private String WidthOfWall;
	@Column(nullable = true)
	private String AreaOfWall;
	@Column(nullable = true)
	private String volumeOfWall;
	@Column(nullable = true)
	private String totalAreaOfOpening;
	@Column(nullable = true)
	private String netAreaOfWall;
	@Column(nullable = true)
	private String totalVolumeOfOpening;
	@Column(nullable = true)
	private String netVolumeOfWall;
	@Column(nullable = true)
	private String materialsWithThickness;
	@Column(nullable = true)
	private String ifcMaterialLayerSet;


	public int getWallMaterialId() {
		return wallMaterialId;
	}

	public void setWallMaterialId(int wallMaterialId) {
		this.wallMaterialId = wallMaterialId;
	}

	public String getHeightOfWall() {
		return heightOfWall;
	}

	public void setHeightOfWall(String heightOfWall) {
		this.heightOfWall = heightOfWall;
	}

	public String getLengthOfWall() {
		return lengthOfWall;
	}

	public void setLengthOfWall(String lengthOfWall) {
		this.lengthOfWall = lengthOfWall;
	}

	public String getWidthOfWall() {
		return WidthOfWall;
	}

	public void setWidthOfWall(String widthOfWall) {
		WidthOfWall = widthOfWall;
	}

	public String getAreaOfWall() {
		return AreaOfWall;
	}

	public void setAreaOfWall(String areaOfWall) {
		AreaOfWall = areaOfWall;
	}

	public String getVolumeOfWall() {
		return volumeOfWall;
	}

	public void setVolumeOfWall(String volumeOfWall) {
		this.volumeOfWall = volumeOfWall;
	}

	public String getTotalAreaOfOpening() {
		return totalAreaOfOpening;
	}

	public void setTotalAreaOfOpening(String totalAreaOfOpening) {
		this.totalAreaOfOpening = totalAreaOfOpening;
	}

	public String getNetAreaOfWall() {
		return netAreaOfWall;
	}

	public void setNetAreaOfWall(String netAreaOfWall) {
		this.netAreaOfWall = netAreaOfWall;
	}

	public String getTotalVolumeOfOpening() {
		return totalVolumeOfOpening;
	}

	public void setTotalVolumeOfOpening(String totalVolumeOfOpening) {
		this.totalVolumeOfOpening = totalVolumeOfOpening;
	}

	public String getNetVolumeOfWall() {
		return netVolumeOfWall;
	}

	public void setNetVolumeOfWall(String netVolumeOfWall) {
		this.netVolumeOfWall = netVolumeOfWall;
	}

	public String getMaterialsWithThickness() {
		return materialsWithThickness;
	}

	public void setMaterialsWithThickness(String materialsWithThickness) {
		this.materialsWithThickness = materialsWithThickness;
	}

	@Override
	public String toString() {
		return "WallMaterial [wallMaterialId=" + wallMaterialId + ", heightOfWall=" + heightOfWall + ", lengthOfWall="
				+ lengthOfWall + ", WidthOfWall=" + WidthOfWall + ", AreaOfWall=" + AreaOfWall + ", volumeOfWall="
				+ volumeOfWall + ", totalAreaOfOpening=" + totalAreaOfOpening + ", netAreaOfWall=" + netAreaOfWall
				+ ", totalVolumeOfOpening=" + totalVolumeOfOpening + ", netVolumeOfWall=" + netVolumeOfWall
				+ ", materialsWithThickness=" + materialsWithThickness + "]";
	}

	public String getIfcMaterialLayerSet() {
		return ifcMaterialLayerSet;
	}

	public void setIfcMaterialLayerSet(String ifcMaterialLayerSet) {
		this.ifcMaterialLayerSet = ifcMaterialLayerSet;
	}



}
