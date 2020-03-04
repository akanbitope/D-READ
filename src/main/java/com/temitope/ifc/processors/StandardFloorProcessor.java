package com.temitope.ifc.processors;

import com.temitope.ifc.executor.StandardFloorExecutor;
import com.temitope.ifc.mvdlayer.MVDLayerCheck;
import com.temitope.ifc.parser.IFCFileParserUtil;
import com.temitope.km.KnowledgeModal;

import java.util.List;
import java.util.Map;

public class StandardFloorProcessor implements IFCProcessor {
	List<String> FileLines;
	StandardFloorExecutor standardFloorExecutor;
	MVDLayerCheck mvdLayerCheck;

	public StandardFloorProcessor(List<String> fileLines, MVDLayerCheck mvdLayerCheck) {
		standardFloorExecutor = new StandardFloorExecutor(fileLines);
		standardFloorExecutor.createFileIndex();
		this.mvdLayerCheck = mvdLayerCheck;

	}

	@Override
	public void parse() {
		standardFloorExecutor.execute();
		if (mvdLayerCheck.isLayerStepBCheckPass()) {
			/*new KnowledgeModal(standardFloorExecutor.getFloorMaterial(),
					standardFloorExecutor.getThicknessMap(),standardFloorExecutor.getHeight());*/
		} else {
		/*	new KnowledgeModal(standardFloorExecutor.getFloorMaterial(),
					standardFloorExecutor.getThicknessMap(),standardFloorExecutor.getHeight());*/

		}
	}

}
