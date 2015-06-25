package edu.stanford.bmir.protege.web.shared.visualization.vowl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Optional;

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
	private Optional<String> value;
	//private Optional<HashMap<String,String>> valueMap;
	private Optional<ArrayList<String>> valueArray;
	
	/**
     * For serialization purposes only
     */
	private ValueDetails() {}
	
	public ValueDetails(String value) {
		this.value = Optional.of(value);
	}
	
	public ValueDetails(ArrayList<String> valueArray) {
		this.valueArray = Optional.of(valueArray);
	}
	
	public String getValue() {
		return value.get();
	}
	
	public ArrayList<String> getArray() {
		return valueArray.get();
	}
	
	/*public ValueDetails(HashMap<String,String> valueMap) {
		this.valueMap = Optional.of(valueMap);
	}*/
	
	public String getType() {
		return value.isPresent()? "String":"Array";
	}
	
	/**
	 * @return The array's size.
	 */
	public int size() {
		return valueArray.get().size();
	}
}
