package com.temitope.test;

import java.util.List;
import java.util.Map;

import com.temitope.dao.EntityDao;
import com.temitope.ifc.common.HibernateRegistry;
import com.temitope.ifc.parser.IFCFileParser;
import com.temitope.ifc.parser.IFCFileParserImpl;
import com.temitope.ifc.parser.IFCFileParserUtil;
import com.temitope.km.DisplayIFCMaterialData;
import com.temitope.km.DisplayMissingValuesData;

public class AppTest {
	public static void main(String[] args) throws Exception {
		IFCFileParser ifcFileParser = new IFCFileParserImpl();
		IFCFileParserUtil ifcFileParserUtil = IFCFileParserUtil.getInstance();
		ifcFileParserUtil.parseFiles(IFCFileParserUtil.getScanDirectory(), ifcFileParser);
		HibernateRegistry.closeSessionFactory();
		System.exit(0);
	}

}
