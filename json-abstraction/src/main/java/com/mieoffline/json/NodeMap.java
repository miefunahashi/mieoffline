package com.mieoffline.json;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Optional;

/**
 * Created by mark on 26/07/15.
 */
public class NodeMap {
	private final ImmutableMap<String, Node> nodeMap;

	public NodeMap(ImmutableMap<String, Node> nodeMap) {
		this.nodeMap = nodeMap;
	}

	public Optional<String> asString(final String key) {
		if (this.nodeMap.containsKey(key)) {
			return this.nodeMap.get(key).asString();
		}
		return Optional.empty();
	}

	public Optional<NodeMap> asMap(final String key) {
		if (this.nodeMap.containsKey(key)) {
			return this.nodeMap.get(key).asMap();
		}
		return Optional.empty();
	}

	public Optional<ImmutableList<Node>> asList(final String key) {
		if (this.nodeMap.containsKey(key)) {
			return this.nodeMap.get(key).asList();
		}
		return Optional.empty();
	}

	public Optional<Node> get(final String key) {
		if (this.nodeMap.containsKey(key)) {
			return Optional.of(this.nodeMap.get(key));
		}
		return Optional.empty();
	}

	public ImmutableSet<Map.Entry<String, Node>> getKeys() {
		return this.nodeMap.entrySet();
	}
}
