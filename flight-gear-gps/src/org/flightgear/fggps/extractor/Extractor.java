package org.flightgear.fggps.extractor;

import org.flightgear.data.PropertyTree;

import android.util.Log;

/**
 * Abstract class representing a data object that can be extracted from game properties
 * @author leandro
 *
 * @param <T> The type of data object
 */
public abstract class Extractor<T> {
	
	private PropertyTree propertyTree;
	
	private T dataObject;
	
	public Extractor(PropertyTree propertyTree, T dataObject) {
		this.propertyTree = propertyTree;
		this.dataObject = dataObject;
	}
	
	public void extractData() {
		try {
			extractData(propertyTree, dataObject);
		} catch (RuntimeException e) {
			Log.w(Extractor.class.getSimpleName(), "Error ocurred during data extraction", e);
		}
	}
	
	public abstract void extractData(PropertyTree propertyTree, T dataObject);
}
