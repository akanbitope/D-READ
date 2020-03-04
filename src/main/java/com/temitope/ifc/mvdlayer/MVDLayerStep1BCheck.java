package com.temitope.ifc.mvdlayer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.temitope.ifc.common.IFCConstants;
import com.temitope.ifc.parser.IFCFileParser;
import com.temitope.ifc.processors.IFCProcessor;

public class MVDLayerStep1BCheck {
	private Set<String> entities = new HashSet<>();

	public MVDLayerStep1BCheck(Set<String> entities) {
		this.entities = entities;
	}

	public boolean layer2CheckPass() {
		boolean checkPass = false;
	    	boolean IFCOPENINGELEMENT = false;
			boolean IFCMATERIALLAYERSET = false;
			for (String name : entities) {
				if (name.equals(IFCConstants.IFC_OPENING_ELEMENT)) {
					IFCOPENINGELEMENT = true;
				}
				if (name.equals(IFCConstants.IFC_MATERIAL_LAYER_SET)) {
					IFCMATERIALLAYERSET = true;
				}
			}
			if (IFCOPENINGELEMENT == true && IFCMATERIALLAYERSET == true) {
				checkPass = true;
			}
	    
		return checkPass;
	}

	public void listOutMissingEntities(Set<String> ifcTypes) {
		Set<String> missingEntities = new HashSet<String>();
		for (String er : IFCConstants.LAYER1_STEPB_MANDATORY_ENTITES) {
			if (!ifcTypes.contains(er)) {
				missingEntities.add(er);
			}
		}
		System.out.println("LAYER 1 (step 1b) --> Missing IFCTypes [] : " + missingEntities);

	}

}
