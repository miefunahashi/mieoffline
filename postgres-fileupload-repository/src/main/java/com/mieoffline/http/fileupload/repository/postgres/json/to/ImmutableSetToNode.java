package com.mieoffline.http.fileupload.repository.postgres.json.to;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mieoffline.json.MapToNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;
import com.mieoffline.json.ObjectMapper;

public class ImmutableSetToNode<T> implements MapToNode<ImmutableSet<T>> {
    private final MapToNode<T> tMapToNode;
    private final String key;

    public ImmutableSetToNode(String key, MapToNode<T> tMapToNode) {
        this.key = key;
        this.tMapToNode = tMapToNode;
    }

    @Override
    public Node apply(ImmutableSet<T> ts) throws ObjectMapper.MappingException {
        final ImmutableList.Builder<Node> nodeBuilder = ImmutableList.builder();
        for (T t : ts) {
            nodeBuilder.add(this.tMapToNode.apply(t));
        }
        final Node map = new Node(new NodeMap(ImmutableMap.<String, Node>of(this.key,new Node(nodeBuilder.build()))));
        return map;

    }
}
