package com.temitope.owl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author cis
 *
 */
@Entity
@Table(name = "Insulation")
public class Insulation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	@Column(nullable = true)
	private String Location;
	@Column(nullable = true)
	private String Level;
	@Column(nullable = true)
	private String Name;
	@Column(nullable = true)
	private String thickness;
	@Column(nullable = true)
	private String Number;
	@Column(nullable = true)
	private String Faced;
	@Column(nullable = true)
	private String FiberglassThickness;
	@Column(nullable = true)
	private String FiberglassType;
	@Column(nullable = true)
	private String FiberglassWidth;
	@Column(nullable = true)
	private String FiberglassCF;
	@Column(nullable = true)
	private String Barrier;
    @Column(nullable = true)
    private Double netArea = 0.0; 
    @Transient
    private Double cost = 0.0;
   

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getLevel() {
		return Level;
	}

	public void setLevel(String level) {
		Level = level;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getNumber() {
		return Number;
	}

	public void setNumber(String number) {
		Number = number;
	}

	public String getFaced() {
		return Faced;
	}

	public void setFaced(String faced) {
		Faced = faced;
	}

	public String getFiberglassThickness() {
		return FiberglassThickness;
	}

	public void setFiberglassThickness(String fiberglassThickness) {
		FiberglassThickness = fiberglassThickness;
	}

	public String getFiberglassType() {
		return FiberglassType;
	}

	public void setFiberglassType(String fiberglassType) {
		FiberglassType = fiberglassType;
	}

	public String getFiberglassWidth() {
		return FiberglassWidth;
	}

	public void setFiberglassWidth(String fiberglassWidth) {
		FiberglassWidth = fiberglassWidth;
	}

	public String getFiberglassCF() {
		return FiberglassCF;
	}

	public void setFiberglassCF(String fiberglassCF) {
		FiberglassCF = fiberglassCF;
	}

	public String getBarrier() {
		return Barrier;
	}

	public void setBarrier(String barrier) {
		Barrier = barrier;
	}

	public String getThickness() {
		return thickness;
	}

	public void setThickness(String thickness) {
		this.thickness = thickness;
	}

	public Double getNetArea() {
		return netArea;
	}

	public void setNetArea(Double netArea) {
		this.netArea = netArea;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}


	@Override
	public String toString() {
		return "Insulation [Id=" + Id + ", Location=" + Location + ", Level=" + Level + ", Name=" + Name
				+ ", thickness=" + thickness + ", Number=" + Number + ", Faced=" + Faced + ", FiberglassThickness="
				+ FiberglassThickness + ", FiberglassType=" + FiberglassType + ", FiberglassWidth=" + FiberglassWidth
				+ ", FiberglassCF=" + FiberglassCF + ", Barrier=" + Barrier + ", netArea=" + netArea + "]";
	}


	

}
