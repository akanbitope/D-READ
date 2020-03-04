package com.temitope.ifc.processors;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.temitope.ifc.executor.StandardWallExecutor;
import com.temitope.ifc.mvdlayer.MVDLayerCheck;
import com.temitope.ifc.parser.IFCFileParserUtil;
import com.temitope.km.*;

public class StandardWallProcessor implements IFCProcessor {
	private StandardWallExecutor standardWallExecutor;
	private Set<String> entities;
	MVDLayerCheck mvdLayerCheck;

	public StandardWallProcessor(List<String> fileLines, MVDLayerCheck mvdLayerCheck) {
		standardWallExecutor = new StandardWallExecutor(fileLines);
		standardWallExecutor.createFileIndex();
		this.mvdLayerCheck = mvdLayerCheck;
	}

	@Override
	public void parse() {
		standardWallExecutor.execute();
		if(mvdLayerCheck.isLayerStepBCheckPass()) {
			KnowledgeModal owlModal = new KnowledgeModal(standardWallExecutor.getWallMaterial(), standardWallExecutor.getThicknessMap(),standardWallExecutor.getHeight());
		
		}else {
			KnowledgeModal owlModal = new KnowledgeModal(standardWallExecutor.getWallMaterial(), standardWallExecutor.getThicknessMap(),standardWallExecutor.getHeight());
		
		}
	}

}
