package com.mieoffline.http.fileupload.repository.postgres.json.from;

import com.mieoffline.json.MapFromNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.ObjectMapper;

public class BooleanFromMap implements MapFromNode<Boolean> {
    @Override
    public Boolean apply(Node node) throws ObjectMapper.MappingException {
        String s = node.asString().orElseThrow(() -> new ObjectMapper.MappingException("No value"));
        try {
            return Boolean.valueOf(s);
        } catch (Exception e) {
            throw new ObjectMapper.MappingException("Not a long", e);
        }

    }
}
