package com.temitope.dao;

import java.util.List;

import org.hibernate.Session;

import com.temitope.owl.model.FloorMaterial;
import com.temitope.owl.model.Gypsum_Board;
import com.temitope.owl.model.Insulation;
import com.temitope.owl.model.Plywood_Sheathing;
import com.temitope.owl.model.Stud;
import com.temitope.owl.model.WallMaterial;

public class EntityDao extends BaseDAOHibernate {
	public EntityDao() {
		getSession().beginTransaction();
	}

	public List<WallMaterial> getWalls() {
		return loadAll(WallMaterial.class);
	}
	public List<FloorMaterial> getFloors() {
		return loadAll(FloorMaterial.class);
	}
	public List<Gypsum_Board> getGypsums() {
		return loadAll(Gypsum_Board.class);
	}
	public List<Insulation> getInsulations() {
		return loadAll(Insulation.class);
	}
	public List<Stud> getStuds() {
		return loadAll(Stud.class);
	}
	public List<Plywood_Sheathing> getSheathings() {
		return loadAll(Plywood_Sheathing.class);
	}
}
