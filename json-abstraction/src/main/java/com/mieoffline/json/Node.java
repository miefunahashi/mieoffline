package com.mieoffline.json;

import com.google.common.collect.ImmutableList;
import java.util.Optional;

public class Node {

	private final Object o;

	public Node(final String o) {
		this.o = o;
	}

	public Node(final NodeMap o) {
		this.o = o;
	}

	public Node(final ImmutableList<Node> o) {
		this.o = o;
	}

	public Optional<String> asString() {
		return isAString() ? Optional.of((String) this.o) : Optional.<String> empty();
	}

	public boolean isAString() {
		return this.o instanceof String;
	}

	public Optional<NodeMap> asMap() {
		return isANodeMap() ? Optional.of((NodeMap) this.o) : Optional.<NodeMap> empty();

	}

	public boolean isANodeMap() {
		return this.o instanceof NodeMap;
	}

	public Optional<ImmutableList<Node>> asList() {
		if (this.o instanceof ImmutableList) {
			final ImmutableList<?> immutableList = (ImmutableList<?>) this.o;
			final ImmutableList.Builder<Node> immutableListBuilder = ImmutableList.builder();
			for (Object node : immutableList) {
				if (node instanceof Node) {
					immutableListBuilder.add((Node) node);
				} else {
					throw new IllegalStateException("Not a node");
				}

			}
			return Optional.of(immutableListBuilder.build());
		}
		return Optional.empty();

	}

	public boolean isAnArray() {
		return this.o instanceof ImmutableList;
	}

}
