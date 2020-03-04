package com.temitope.owl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

/**
 * @author cis
 *
 */
@Entity
@Table(name="Gypsum_Board")
public class Gypsum_Board {
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
	private String Finish;
    @Column(nullable = true)
	private String Thickness;
    @Column(nullable = true)
	private String Tool;
    @Column(nullable = true)
    private Double netArea=0.0; 
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

	public String getFinish() {
		return Finish;
	}

	public void setFinish(String finish) {
		Finish = finish;
	}

	public String getThickness() {
		return Thickness;
	}

	public void setThickness(String thickness) {
		Thickness = thickness;
	}

	public String getTool() {
		return Tool;
	}

	public void setTool(String tool) {
		Tool = tool;
	}

	
	@Override
	public String toString() {
		return "Gypsum_Board [Id=" + Id + ", Location=" + Location + ", Level=" + Level + ", Name=" + Name + ", Finish="
				+ Finish + ", Thickness=" + Thickness + ", Tool=" + Tool + ", netArea=" + netArea + "]";
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






	

}
