package br.com.fabriciodev.converter.components;

import java.util.HashMap;
import java.util.Map;

public class DataModel {
    private Map<String, Object> attributes = new HashMap<>();
    private Map<String, Object> relations = new HashMap<>();

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public void setRelation(String key, Object value) {
        relations.put(key, value);
    }

    public Object getRelation(String key) {
        return relations.get(key);
    }
}
