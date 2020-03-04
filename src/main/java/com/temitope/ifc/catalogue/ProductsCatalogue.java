package com.temitope.ifc.catalogue;


public class ProductsCatalogue {
	
    public String thickness;
    public double value;
    public String prod;

    public ProductsCatalogue(String product, String thickness, double value){
        this.prod = product;
        this.thickness = thickness;
        this.value = value;
    }
}


