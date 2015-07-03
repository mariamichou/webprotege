package edu.stanford.bmir.protege.web.shared.visualization.vowl;

import java.io.Serializable;
import java.util.Map;

public class GraphDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This is a wrapper class for a JSON object's representation.
     * A JSON object in our case is a map which contains details about an entity graph. 
     * Each entity (i.e. a class or property) contains a set of present keys, 
     * e.g. name, type, etc and their corresponding values.
     * As this is a flat representation of the object, we use 
     * a ValueDetails object which can be a String or an ArrayList of strings.
     */
    private Map<String, ValueDetails> detailsMap;
    
    /**
     * For serialization purposes only
     */
    GraphDetails() {}
    
    /**
     * Gets the size of this details map.
     * @return The size of this map. 
     */
    public int size() {
        return detailsMap.values().size();
    }
    
    public GraphDetails(Map<String, ValueDetails> detailsMap) {
        this.detailsMap = detailsMap;
    }
    
    public Map<String, ValueDetails> getMap() {
    	return detailsMap;
    }
    
    @Override
    public String toString() {
    	String str = "{ ";
    	for(String key : detailsMap.keySet()) {
    		str += "[ " + key + " : " + detailsMap.get(key).toString() + " ]";
    	}
    	str += " }";
    	return str;
    }
    
    /* Not serializable!!!
     * public Set<String> getKeys() {
    	return detailsMap.keySet();
    }
    
    public Collection<ValueDetails> getValues() {
    	return detailsMap.values();
    }*/

}
