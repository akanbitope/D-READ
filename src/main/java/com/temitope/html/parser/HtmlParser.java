package com.temitope.html.parser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.temitope.ifc.parser.IFCFileParserUtil;

public class HtmlParser {
	Map<String, Integer> dataMap = new HashMap<>();
	public HtmlParser(String fileName) {
		parse(fileName);
	}
	
	public void parse(String fileName) {
		try {
			Document doc = Jsoup.parse(new File("inDir/" + fileName+".htm"), "UTF-8");
			Element content = doc.getElementsByTag("body").first();
			Elements links = content.getElementsByTag("h3");
			for (Element link : links) {
				String data[] = link.text().split(" ");
				dataMap.put(data[0].trim(),
						Integer.valueOf(data[1].substring(data[1].indexOf("(") + 1, data[1].indexOf(")"))));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String, Integer> getDataMap() {
		return dataMap;
	}
	

}
