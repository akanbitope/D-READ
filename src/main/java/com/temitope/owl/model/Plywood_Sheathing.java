package com.temitope.owl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="Plywood_Sheathing")
public class Plywood_Sheathing {
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
	private String Nailed;
    @Column(nullable = true)
	private String Laid;
	//private String Gypsum_Type;
    @Column(nullable = true)
	private String Barrier;
    @Column(nullable = true)
	private String Thickness;
    @Column(nullable = true)
	private String gypsumType;
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
	public String getNailed() {
		return Nailed;
	}
	public void setNailed(String nailed) {
		Nailed = nailed;
	}
	public String getLaid() {
		return Laid;
	}
	public void setLaid(String laid) {
		Laid = laid;
	}
	public String getBarrier() {
		return Barrier;
	}
	public void setBarrier(String barrier) {
		Barrier = barrier;
	}
	public String getThickness() {
		return Thickness;
	}
	public void setThickness(String thickness) {
		Thickness = thickness;
	}
	public String getGypsumType() {
		return gypsumType;
	}
	public void setGypsumType(String gypsumType) {
		this.gypsumType = gypsumType;
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
		return "Plywood_Sheathing [Id=" + Id + ", Name=" + Name + ", Nailed=" + Nailed + ", Thickness=" + Thickness
				+ ", netArea=" + netArea + "]";
	}




}
