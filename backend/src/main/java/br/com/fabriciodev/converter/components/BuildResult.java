package br.com.fabriciodev.converter.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildResult {

    public static Object json(Object data, Object relations) {
        return build(data, relations);
    }

    public static Object json(Object data) {
        return data;
    }

    public static Object build(Object data, Object relations) {
        if (data instanceof List) {
            List<Object> result = new ArrayList<>();
            for (Object item : (List<?>) data) {
                result.add(buildItem(item, relations));
            }
            return result;
        } else {
            return buildItem(data, relations);
        }
    }

    public static Map<String, Object> buildItem(Object data, Object relations) {
        if (data == null)
            return null;

        Map<String, Object> attributes = new HashMap<>();

        if (data instanceof Map) {
            attributes.putAll((Map<? extends String, ?>) data);
        } else if (data instanceof DataModel) {
            DataModel model = (DataModel) data;
            attributes.putAll(model.getAttributes());
        }

        attributes = DataJsonBehavior.convertToOut(attributes);

        if (relations instanceof Map) {
            Map<?, ?> relationMap = (Map<?, ?>) relations;
            for (Map.Entry<?, ?> entry : relationMap.entrySet()) {
                String keyRelation = entry.getKey().toString();
                Object relation = entry.getValue();

                Object relationData = ((DataModel) data).getRelation(keyRelation);
                if (relationData == null)
                    continue;

                if (relationData instanceof List) {
                    List<Object> resultArray = new ArrayList<>();
                    for (Object item : (List<?>) relationData) {
                        resultArray.add(build(item, relation));
                    }
                    attributes.put(keyRelation, resultArray);
                } else if (relationData instanceof DataModel) {
                    attributes.put(keyRelation,
                            DataJsonBehavior.convertToOut(((DataModel) relationData).getAttributes()));
                }
            }
        }

        return attributes;
    }
}
