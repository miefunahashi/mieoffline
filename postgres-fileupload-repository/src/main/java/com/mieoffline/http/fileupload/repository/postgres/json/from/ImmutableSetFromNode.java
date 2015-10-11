package com.mieoffline.http.fileupload.repository.postgres.json.from;

import com.google.common.collect.ImmutableSet;
import com.mieoffline.json.MapFromNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.ObjectMapper;

public class ImmutableSetFromNode<T> implements MapFromNode<ImmutableSet<T>> {

    private final MapFromNode<T> tMapFromNode;

    public ImmutableSetFromNode(MapFromNode<T> tMapFromNode) {
        this.tMapFromNode = tMapFromNode;
    }

    @Override
    public ImmutableSet<T> apply(Node node) throws ObjectMapper.MappingException {
        final ImmutableSet.Builder<T> setBuilder = ImmutableSet.builder();

        for (Node n : node.asList().orElseThrow(() -> new ObjectMapper.MappingException("Not an array"))) {
            setBuilder.add(this.tMapFromNode.apply(n));
        }
        return setBuilder.build();
    }
}
