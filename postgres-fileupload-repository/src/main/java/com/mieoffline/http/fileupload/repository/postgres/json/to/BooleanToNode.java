package com.mieoffline.http.fileupload.repository.postgres.json.to;

import com.mieoffline.json.MapToNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.ObjectMapper;

public class BooleanToNode implements MapToNode<Boolean> {

    @Override
    public Node apply(Boolean aBoolean) throws ObjectMapper.MappingException {
        return new Node(aBoolean.toString());
    }
}
