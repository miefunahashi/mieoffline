package com.mieoffline.http.fileupload.repository.postgres.json.to;

import com.mieoffline.json.MapToNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.ObjectMapper;

public class LongToMap implements MapToNode<Long> {
    @Override
    public Node apply(Long aLong) throws ObjectMapper.MappingException {
        return new Node(aLong.toString());
    }
}
