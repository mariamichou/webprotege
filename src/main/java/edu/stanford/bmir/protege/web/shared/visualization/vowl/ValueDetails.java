package edu.stanford.bmir.protege.web.shared.visualization.vowl;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This object will hold the values of the graph details map.
 * It is a flat serialization of JSON objects that are essential for an entity. 
 * I.e. this object maps keys to their corresponding values.
 * E.g. a key as a String will map to a value as a String (html or plain text)
 * OR an ArrayList of strings (e.g. a drop-down list).
 * @author Maria Michou <maria.michou@gmail.com>
 *
 */
public class ValueDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String value="";
	private ArrayList<String> valueArray = new ArrayList<String>();
	
	/**
     * For serialization purposes only
     */
	ValueDetails() {}
	
	public ValueDetails(String value) {
		//this.value = Optional.of(value);
		this.value = value;
	}
	
	public ValueDetails(ArrayList<String> valueArray) {
		//this.valueArray = Optional.of(valueArray);
		this.valueArray = valueArray;
	}
	
	public String getValue() {
		//return value.isPresent()? value.get() : Optional.<String>absent().get();
		return value;
	}
	
	public ArrayList<String> getArray() {
		//return valueArray.isPresent()? valueArray.get() : Optional.<ArrayList<String>>absent().get();
		return valueArray;
	}
	
	
	/*public ValueDetails(HashMap<String,String> valueMap) {
		this.valueMap = Optional.of(valueMap);
	}*/
	
	public String getType() {
		return value.equals("")? "Array": "String";
	}
	
	/**
	 * @return The array's size.
	 */
	public int size() {
		return getType().equals("Array") ? valueArray.size() : 1;
	}
	
	@Override
	public String toString() {
		if(!value.equals("")) 
			return value;
		else if(!valueArray.isEmpty()) 
			return valueArray.toString();
		else
			return "ValueDetails object absent";
	}
	
}
