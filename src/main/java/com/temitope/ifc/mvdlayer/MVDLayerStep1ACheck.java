package com.temitope.ifc.mvdlayer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.temitope.ifc.common.IFCConstants;

public class MVDLayerStep1ACheck {
	private Set<String> entities = new HashSet<>();
	public MVDLayerStep1ACheck(Set<String> entities) {
		this.entities = entities;
	}

	public boolean layer1CheckPass(String ifcType) {
		return entities.contains(ifcType);
	}
	
	public void listOutMissingEntities(Set<String> ifcTypes){
		Set<String> missingEntities = new HashSet<String>();
		for(String er : IFCConstants.LAYER1_STEPA_MANDATORY_ENTITES) {
			if(!ifcTypes.contains(er)) {
				missingEntities.add(er);
			}
		}
		System.out.println("LAYER 1 (step 1a) --> Missing IFCTypes [] : "+missingEntities);
		
	}

}
