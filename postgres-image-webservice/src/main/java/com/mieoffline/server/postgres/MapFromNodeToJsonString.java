package com.mieoffline.server.postgres;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.collect.ImmutableList;
import com.mieoffline.functional.MieRunnable;
import com.mieoffline.json.MapFromNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;

import java.util.*;

public class MapFromNodeToJsonString implements MapFromNode<String> {
    @Override
    public String apply(Node node) throws com.mieoffline.json.ObjectMapper.MappingException {
        final Optional<String> optionalString = node.asString();
        if (optionalString.isPresent()) {
            return new TextNode(optionalString.get()).toString();
        }
        final JsonNode jsonNode;
        final ObjectMapper objectMapper = new ObjectMapper();
        final Queue<MieRunnable<com.mieoffline.json.ObjectMapper.MappingException>> runnables = new LinkedList<>();
        final Optional<ImmutableList<Node>> optionalArray = node.asList();
        if (optionalArray.isPresent()) {
            final ArrayNode arrayNode = objectMapper.createArrayNode();
            for (final Node childNode : optionalArray.get()) {
                runnables.add(new ArrayNodeModel(arrayNode,childNode,runnables,objectMapper));
            }
            jsonNode = arrayNode;
        }else {
        final Optional<NodeMap> nodeMap = node.asMap();
        if(nodeMap.isPresent()){
            final ObjectNode objectNode = objectMapper.createObjectNode();
            for(final Map.Entry<String,Node> childNode: nodeMap.get().getKeys()){
                runnables.add(new ObjectNodeModel(objectNode,childNode.getKey(),childNode.getValue(),runnables,objectMapper));
            }
            jsonNode = objectNode;
        }else {
        throw new com.mieoffline.json.ObjectMapper.MappingException("Nothing to map");}
        }
        runRunnables(runnables);

        try {
            return objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new com.mieoffline.json.ObjectMapper.MappingException("Error creating Jackson json",e);
        }
    }
    private void runRunnables(final Queue<MieRunnable<com.mieoffline.json.ObjectMapper.MappingException>> runnables) throws com.mieoffline.json.ObjectMapper.MappingException {
        while(!runnables.isEmpty()){
            runnables.remove().apply(null);
        }
    }

    private static class ObjectNodeModel implements MieRunnable<com.mieoffline.json.ObjectMapper.MappingException> {
        private final ObjectNode parent;
        private final String key;
        private final Node toAdd;
        private final Queue<MieRunnable<com.mieoffline.json.ObjectMapper.MappingException>> nodeQueue;
        private final ObjectMapper objectMapper;

        private ObjectNodeModel(ObjectNode parent, String key, Node toAdd, Queue<MieRunnable<com.mieoffline.json.ObjectMapper.MappingException>> nodeQueue, ObjectMapper objectMapper) {
            this.parent = parent;
            this.key = key;
            this.toAdd = toAdd;
            this.nodeQueue = nodeQueue;
            this.objectMapper = objectMapper;
        }

        @Override
        public Void apply(Void avoid) throws com.mieoffline.json.ObjectMapper.MappingException {
            final Optional<String> optional = this.toAdd.asString();
            if (optional.isPresent()) {
                this.parent.put(this.key, optional.get());
            }
            final Optional<ImmutableList<Node>> nodes = this.toAdd.asList();
            if (nodes.isPresent()) {
                final ArrayNode arrayNode = this.objectMapper.createArrayNode();
                this.parent.set(this.key, arrayNode);
                final ArrayNode arrayNodeParent = arrayNode;
                for (final Node node : nodes.get()) {
                    final Node anotherNodetoAdd = node;
                    this.nodeQueue.add(new ArrayNodeModel(arrayNodeParent, anotherNodetoAdd, this.nodeQueue, this.objectMapper));
                }
            }
            final Optional<NodeMap> nodeMap = this.toAdd.asMap();
            if (nodeMap.isPresent()) {
                final ObjectNode objectNode = this.objectMapper.createObjectNode();
                this.parent.set(this.key, objectNode);
                for (final Map.Entry<String, Node> entry : nodeMap.get().getKeys()) {
                    this.nodeQueue.add(new ObjectNodeModel(objectNode, entry.getKey(), entry.getValue(), this.nodeQueue, this.objectMapper));
                }
            }
            return null;
        }
    }

    private static class ArrayNodeModel implements MieRunnable<com.mieoffline.json.ObjectMapper.MappingException> {
        private final ArrayNode parent;
        private final Node toAdd;
        private final Queue<MieRunnable<com.mieoffline.json.ObjectMapper.MappingException>> nodeQueue;
        private final ObjectMapper objectMapper;

        private ArrayNodeModel(ArrayNode parent, Node toAdd, Queue<MieRunnable<com.mieoffline.json.ObjectMapper.MappingException>> nodeQueue, ObjectMapper objectMapper) {
            this.parent = parent;
            this.toAdd = toAdd;
            this.nodeQueue = nodeQueue;
            this.objectMapper = objectMapper;
        }


        @Override
        public Void apply(Void avoid) {
            final Optional<String> optionalString = this.toAdd.asString();
            if (optionalString.isPresent()) {
                this.parent.add(new TextNode(optionalString.get()).toString());
            }
            final Optional<ImmutableList<Node>> optionalArray = this.toAdd.asList();
            if (optionalArray.isPresent()) {
                final ArrayNode arrayNode = this.objectMapper.createArrayNode();
                this.parent.add(arrayNode);
                for (final Node childNode : optionalArray.get()) {
                    this.nodeQueue.add(new ArrayNodeModel(arrayNode, childNode, this.nodeQueue, this.objectMapper));
                }
            }
            final Optional<NodeMap> nodeMap = this.toAdd.asMap();
            if (nodeMap.isPresent()) {
                final ObjectNode objectNode = this.objectMapper.createObjectNode();
                this.parent.add(objectNode);
                for (Map.Entry<String, Node> entry : nodeMap.get().getKeys()) {
                    this.nodeQueue.add(new ObjectNodeModel(objectNode, entry.getKey(), entry.getValue(), this.nodeQueue, this.objectMapper));
                }
            }
            return null;
        }
    }
}
