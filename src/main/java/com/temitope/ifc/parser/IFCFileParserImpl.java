package com.temitope.ifc.parser;

import com.temitope.html.parser.HtmlParser;
import com.temitope.ifc.mvdlayer.MVDLayerCheck;
import com.temitope.ifc.processors.IFCProcessor;
import com.temitope.ifc.processors.StandardFloorProcessor;
import com.temitope.ifc.processors.StandardProcessor;
import com.temitope.ifc.processors.StandardWallProcessor;

import java.util.*;

public class IFCFileParserImpl implements IFCFileParser {
	@Override
	public void parseFile(List<String> fileLines, String fileName) {
		StandardProcessor standardProcessor = execute(fileLines);
		Set<String> entities = standardProcessor.getEntities();
		parseValidatingResultIfcHtml(fileLines, fileName);

	}

	private void parseValidatingResultIfcHtml(List<String> fileLines, String fileName) {
		HtmlParser htmlParser = new HtmlParser(fileName);
		MVDLayerCheck mvdLayerCheck = new MVDLayerCheck(htmlParser.getDataMap());
		if (mvdLayerCheck.isLayerStepACheckPass(IFCFileParserUtil.Constants.IFC_WALL_STANDARD_CASE)
				&& !mvdLayerCheck.isLayerStepACheckPass(IFCFileParserUtil.Constants.IFC_SLAB)) {
			IFCFileParserUtil.printLayerStatus(IFCFileParserUtil.COMPONENTS_HOUSE_WALL,
					IFCFileParserUtil.MVD_LAYER_CHECK, true);
			printIfcItems(mvdLayerCheck.listOutMissingItems());
			IFCProcessor wp = new StandardWallProcessor(fileLines, mvdLayerCheck);
			wp.parse();

		} else if (mvdLayerCheck.isLayerStepACheckPass(IFCFileParserUtil.Constants.IFC_SLAB)
				&& !mvdLayerCheck.isLayerStepACheckPass(IFCFileParserUtil.Constants.IFC_WALL_STANDARD_CASE)) {
			IFCFileParserUtil.printLayerStatus(IFCFileParserUtil.COMPONENTS_HOUSE_FLOOR,
					IFCFileParserUtil.MVD_LAYER_CHECK, true);
			printIfcItems(mvdLayerCheck.listOutMissingItems());
			IFCProcessor fp = new StandardFloorProcessor(fileLines, mvdLayerCheck);
			fp.parse();

		} else if (!mvdLayerCheck.isLayerStepACheckPass(IFCFileParserUtil.Constants.IFC_WALL_STANDARD_CASE)) {
			IFCFileParserUtil.printLayerStatus(IFCFileParserUtil.COMPONENTS_HOUSE_WALL,
					IFCFileParserUtil.MVD_LAYER_CHECK, false);
		} else if (!mvdLayerCheck.isLayerStepACheckPass(IFCFileParserUtil.Constants.IFC_SLAB)) {
			IFCFileParserUtil.printLayerStatus(IFCFileParserUtil.COMPONENTS_HOUSE_FLOOR,
					IFCFileParserUtil.MVD_LAYER_CHECK, false);
		}

		else if (!mvdLayerCheck.isLayerStepACheckPass(IFCFileParserUtil.Constants.IFC_SLAB)
				&& !mvdLayerCheck.isLayerStepACheckPass(IFCFileParserUtil.Constants.IFC_WALL_STANDARD_CASE)) {
			System.out.println(
					"System does not fullfill minimum exchange requirement for processsing either floor or wall");
			IFCFileParserUtil.printLayerStatus(
					IFCFileParserUtil.COMPONENTS_HOUSE_WALL + " AND " + IFCFileParserUtil.COMPONENTS_HOUSE_FLOOR,
					IFCFileParserUtil.MVD_LAYER_CHECK, false);
		}

	}

	private void printIfcItems(List<String> listOutMissingItems) {
		System.out.println("following are item list found from MVD Layer");
		for (String s : listOutMissingItems) {
			System.out.println(s);
		}
		System.out.println("\n\n");

	}

	private StandardProcessor execute(List<String> fileLines) {
		StandardProcessor standardProcessor = new StandardProcessor(fileLines);
		standardProcessor.parse();
		return standardProcessor;
	}

}
