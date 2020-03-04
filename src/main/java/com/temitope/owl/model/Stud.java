package com.temitope.owl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="Stud")
public class Stud {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
    @Column(nullable = true)
	private String Location;
    @Column(nullable = true)
	private String Level;
    @Column(nullable = true)
	private String Height;
    @Column(nullable = true)
	private String Nailed;	
    @Column(nullable = true)
	private String Spacing;	
    @Column(nullable = true)
	private String Thickness;
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
	public String getHeight() {
		return Height;
	}
	public void setHeight(String height) {
		Height = height;
	}
	public String getNailed() {
		return Nailed;
	}
	public void setNailed(String nailed) {
		Nailed = nailed;
	}
	public String getSpacing() {
		return Spacing;
	}
	public void setSpacing(String spacing) {
		Spacing = spacing;
	}
	public String getThickness() {
		return Thickness;
	}
	public void setThickness(String thickness) {
		Thickness = thickness;
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
		return "Stud [Id=" + Id + ", Location=" + Location + ", Level=" + Level + ", Height=" + Height + ", Nailed="
				+ Nailed + ", Spacing=" + Spacing + ", Thickness=" + Thickness + ", netArea=" + netArea + ", cost="
				+ cost + "]";
	}


}
