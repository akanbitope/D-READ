package com.temitope.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the synonyms database table.
 * 
 */
@Entity
@Table(name="synonyms")
@NamedQuery(name="Synonym.findAll", query="SELECT s FROM Synonym s")
public class Synonym implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Lob
	private String words;

	public Synonym() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWords() {
		return this.words;
	}

	public void setWords(String words) {
		this.words = words;
	}

}