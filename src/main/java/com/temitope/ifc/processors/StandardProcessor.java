package com.temitope.ifc.processors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StandardProcessor implements IFCProcessor {
	private List<String> fileLines;
	private HashMap<String, String> fileHash;
	private Set<String> entities;

	public StandardProcessor(List<String> fileLines) {
		this.fileLines = fileLines;
		entities = new HashSet<>();
		fileHash = new HashMap<>();
		fileLines = new ArrayList<>();
	}
	
	@Override
	public void parse() {
		createFileIndex();
	}

	private void createFileIndex() {
		for (String line : fileLines) {
			if (line.startsWith("#")) {
				String identifier = slice_range(line, 0, line.indexOf("="));
				String remLine = slice_range(line, line.indexOf("=") + 1, line.length());
				fileHash.put(identifier, remLine);
				String ifcType = slice_range(remLine, 0, remLine.indexOf("("));
				entities.add(ifcType.trim());
			}
		}

	}

	private String slice_range(String s, int startIndex, int endIndex) {
		if (startIndex < 0)
			startIndex = s.length() + startIndex;
		if (endIndex < 0)
			endIndex = s.length() + endIndex;
		return s.substring(startIndex, endIndex);
	}

	public Set<String> getEntities() {
		return entities;
	}

}
