package com.temitope.ifc.parser;

import java.util.List;

public interface IFCFileParser {
	public void parseFile(List<String> fileLines, String file);
}
