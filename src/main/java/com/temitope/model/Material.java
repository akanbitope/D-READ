package com.temitope.model;

import java.io.Serializable;
//import javax.persistence.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import antlr.StringUtils;


/**
 * The persistent class for the materials database table.
 * 
 */
@Entity
//@Indexed(index="indexes/essays")
@Table(name="materials")
@NamedQuery(name="Material.findAll", query="SELECT m FROM Material m")
public class Material implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(nullable = true)
	private String material;
	@Column(nullable = true)
	private String name;
	@Column(nullable = true)
	private String number;
	@Column(nullable = true)
	private String height;
	@Column(nullable = true)
	private String nailed;
	@Column(nullable = true)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	private Double thickness;
	@Column(nullable = true)
	private String finish;
	@Column(nullable = true)
	private Double cost = 0.0;
	@Column(nullable = true)
	private Double netArea = 0.0;
	@Column(nullable = true)
	private Double totalCost = 0.0;
	@Column(nullable = true)
	private byte status;
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	private String title;

	//bi-directional many-to-one association to Category
	@ManyToOne
	private Category category;

	public Material() {
	}


	public void setTitle(String title) {
		String rx_prop_sec = "(\\((.+)\\))";
		Pattern p = Pattern.compile(rx_prop_sec);// . represents single character
		Matcher m = p.matcher(title);
		if (m.find()) {
			title = m.group();
			title = StringUtils.stripFrontBack(title, "'", "'");
			title = StringUtils.stripFrontBack(title, "\"", "\"");
		}
		this.title = title;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getMaterial() {
		return material;
	}


	public void setMaterial(String material) {
		this.material = material;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}


	public String getHeight() {
		return height;
	}


	public void setHeight(String height) {
		this.height = height;
	}


	public String getNailed() {
		return nailed;
	}


	public void setNailed(String nailed) {
		this.nailed = nailed;
	}


	public Double getThickness() {
		return thickness;
	}


	public void setThickness(Double thickness) {
		this.thickness = thickness;
	}


	public String getFinish() {
		return finish;
	}


	public void setFinish(String finish) {
		this.finish = finish;
	}


	public Double getCost() {
		return cost;
	}


	public void setCost(Double cost) {
		this.cost = cost;
	}



	public Double getNetArea() {
		return netArea;
	}


	public void setNetArea(Double netArea) {
		this.netArea = netArea;
	}


	public Double getTotalCost() {
		return totalCost;
	}


	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}


	public byte getStatus() {
		return status;
	}


	public void setStatus(byte status) {
		this.status = status;
	}


	public Category getCategory() {
		return category;
	}


	public void setCategory(Category category) {
		this.category = category;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getTitle() {
		return title;
	}


	@Override
	public String toString() {
		return "Material [thickness=" + thickness + ", title=" + title + "]";
	}

	

}