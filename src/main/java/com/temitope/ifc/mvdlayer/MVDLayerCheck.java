package com.temitope.ifc.mvdlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.temitope.ifc.common.IFCConstants;
import com.temitope.ifc.parser.IFCFileParserUtil;

public class MVDLayerCheck {
	Map<String, Integer> mapData;
	Set<String> entities;

	public MVDLayerCheck(Map<String, Integer> mapData) {
		this.mapData = mapData;
	}

	public MVDLayerCheck(Set<String> entities) {
		this.entities = entities;
	}

	public boolean isLayerStepACheckPass(String ifcType) {
		boolean pass = false;
		for (Entry<String, Integer> entry : mapData.entrySet()) {
			if (entry.getKey().trim().equals(ifcType.trim()) && entry.getValue() > 0) {
				pass = true;
			}
		}
		return pass;
	}

	public boolean isLayerStepBCheckPass() {
		boolean checkPass = false;
		boolean IFCDOOR = false;
		boolean IFCWINDOW = false;
		for (Entry<String, Integer> entry : mapData.entrySet()) {
			if (entry.getKey().trim().equals(IFCConstants.IFC_DOOR.trim()) && entry.getValue() > 0) {
				IFCDOOR = true;
			}
			if (entry.getKey().trim().equals(IFCConstants.IFC_WINDOW.trim()) && entry.getValue() > 0) {
				IFCWINDOW = true;
			}
		}
		if (IFCDOOR == true && IFCWINDOW == true) {
			checkPass = true;
		}

		return checkPass;
	}

	public List<String> listOutMissingItems() {
		List<String> list = new ArrayList<>();
		for (Entry<String, Integer> entry : mapData.entrySet()) {
			if (entry.getKey().trim().equals(IFCFileParserUtil.Constants.IFC_WALL_STANDARD_CASE.trim())) {
				list.add(entry.getKey() + "(" + entry.getValue() + ")");
			} else if (entry.getKey().trim().equals(IFCFileParserUtil.Constants.IFC_SLAB.trim())) {
				list.add(entry.getKey() + "(" + entry.getValue() + ")");
			} else if (entry.getKey().trim().equals(IFCConstants.IFC_DOOR.trim())) {
				list.add(entry.getKey() + "(" + entry.getValue() + ")");
			} else if (entry.getKey().trim().equals(IFCConstants.IFC_WINDOW.trim())) {
				list.add(entry.getKey() + "(" + entry.getValue() + ")");
			}
		}
		return list;
	}

}
