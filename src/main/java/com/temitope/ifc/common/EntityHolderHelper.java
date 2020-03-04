package com.temitope.ifc.common;

import com.temitope.owl.model.FloorMaterial;
import com.temitope.owl.model.WallMaterial;

public class EntityHolderHelper {
	private static EntityHolderHelper instance;
	private WallMaterial wallMaterial;
	private FloorMaterial floorMaterial;
	private EntityHolderHelper() {
		// private constructor
	}

	public static EntityHolderHelper getInstance() {
		if (instance == null) {
			instance = new EntityHolderHelper();
		}
		return instance;
	}

	public WallMaterial getWallMaterial() {
		return wallMaterial;
	}

	public void setWallMaterial(WallMaterial wallMaterial) {
		this.wallMaterial = wallMaterial;
	}

	public FloorMaterial getFloorMaterial() {
		return floorMaterial;
	}

	public void setFloorMaterial(FloorMaterial floorMaterial) {
		this.floorMaterial = floorMaterial;
	}
	
}
