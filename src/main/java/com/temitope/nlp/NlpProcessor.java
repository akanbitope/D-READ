package com.temitope.nlp;

import java.util.ArrayList;
import java.util.List;

import com.temitope.ifc.common.ProductSearch;
import com.temitope.model.Material;

public class NlpProcessor {
	
	public void processNlp(List<Material> materials) {
		 
		for(Material material: materials) {
			System.out.println("\n\n-------------------------------NLP------------------------------\n");
              ProductSearch productSearch = new ProductSearch();
              productSearch.setMaterialTitle(material.getTitle());
              productSearch.setThickness(String.valueOf(material.getThickness()));
              String[] tokenArray = productSearch.tokenizer(material.getTitle());
              List<Material> maList = productSearch.getAllMaterial(tokenArray,String.valueOf(material.getThickness()));
              material = productSearch.doExecution();
              if(material != null){
              	calculateCost(material);
              }
		}
        
	}
	
	private void calculateCost(Material material) {
		Double cos = 0.0;
		System.out.println("Unit Cost of " + material.getTitle().replaceAll(",", ", ") + " with thickness " + material.getThickness()
				+ " is $" + material.getCost());
		cos = material.getNetArea() * material.getCost();
		System.out.println(
				"Total Cost of " + material.getTitle().replaceAll(",", ", ") + " with thickness " + material.getThickness() + " is $" + cos);
	}
}
