package com.temitope.owl.catalogue;

public class OwlElement {
	public String className;
	public String propertyName;
	public String objectName;

	public OwlElement(String className, String propertyName, String objectName) {
		this.className = className;
		this.propertyName = propertyName;
		this.objectName = objectName;
	}

	public String getClassName() {
		return className;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getObjectName() {
		return objectName;
	}
	

}
