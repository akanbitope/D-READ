package com.temitope.ifc.common;

import java.util.EnumSet;
import java.util.Set;

public class IFCConstants {
	public static final String IFC_WALL_STANDARD_CASE = "IFCWALLSTANDARDCASE";
	public static final String IFC_SLAB = "IFCSLAB";
	public static final String IFC_OPENING_ELEMENT = "IFCOPENINGELEMENT";
	public static final String IFC_MATERIAL_LAYER_SET = "IFCMATERIALLAYERSET";
	public static final String IFC_DOOR = "IfcDoor";
	public static final String IFC_WINDOW = "IfcWindow";

	public static final String[] LAYER1_STEPA_MANDATORY_ENTITES = { IFC_WALL_STANDARD_CASE, IFC_SLAB, IFC_OPENING_ELEMENT,
			IFC_MATERIAL_LAYER_SET };
	
	public static final String[] LAYER1_STEPB_MANDATORY_ENTITES = { IFC_OPENING_ELEMENT, IFC_MATERIAL_LAYER_SET };
	public static final String IFCPRODUCTDEFINITIONSHAPE = "IFCPRODUCTDEFINITIONSHAPE";
	public static final String IFCSHAPEREPRESENTATION = "IFCSHAPEREPRESENTATION";
	public static final String IFCEXTRUDEDAREASOLID = "IFCEXTRUDEDAREASOLID";
	public static final String IFCBOOLEANCLIPPINGRESULT = "IFCBOOLEANCLIPPINGRESULT";
	
	public static final String GYPSUM_WALL_BOARD = "Gypsum Wall Board";
	public static final String STUD_AND_INSULATION = "Structure, Wood Joist/Rafter Layer, Batt Insulation";
	

}