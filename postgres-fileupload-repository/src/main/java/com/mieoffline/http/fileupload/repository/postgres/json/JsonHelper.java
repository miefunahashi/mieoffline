package com.mieoffline.http.fileupload.repository.postgres.json;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;
import com.mieoffline.json.ObjectMapper;

import java.util.Map;

public class JsonHelper {

    public JsonHelper() {

    }

    public Node getJsonNodesFromMap(ImmutableMap<String, ImmutableList<String>> uploadProperties) {
        final ImmutableMap.Builder<String, Node> propertiesNode = ImmutableMap.builder();
        for (final Map.Entry<String, ImmutableList<String>> entry : uploadProperties.entrySet()) {
            final ImmutableList.Builder<Node> values = ImmutableList.builder();
            for (final String value : entry.getValue()) {
                values.add(new Node(value));
            }

            propertiesNode.put(entry.getKey(), new Node(values.build()));
        }
        return new Node(new NodeMap(propertiesNode.build()));
    }

    public ImmutableMap<String, ImmutableList<String>> getMapeFromJsonNode(Node httpHeadersNode) throws ObjectMapper.MappingException {
        final ImmutableSet<Map.Entry<String, Node>> keys = httpHeadersNode.asMap().orElseThrow(() -> new ObjectMapper.MappingException("Not a json map")).getKeys();
        final ImmutableMap.Builder<String, ImmutableList<String>> httpHeadersBuilder = ImmutableMap.builder();
        for (Map.Entry<String, Node> key : keys) {
            final ImmutableList.Builder<String> valueBuilder = ImmutableList.builder();
            final ImmutableList<Node> nodes = key.getValue().asList().orElseThrow(() -> new ObjectMapper.MappingException("Not a list node"));
            for (Node node : nodes) {
                final String value = node.asString().orElseThrow(() -> new ObjectMapper.MappingException("Not a string json node"));
                valueBuilder.add(value);
            }
            httpHeadersBuilder.put(key.getKey(), valueBuilder.build());
        }
        return httpHeadersBuilder.build();
    }

}
