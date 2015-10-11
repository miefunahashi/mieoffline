package com.mieoffline.json;

public class MapFromNodeToString  {
    final MapFromNode<String> serializer;

    public MapFromNodeToString(MapFromNode<String> serializer) {
        this.serializer = serializer;
    }
    public <T> MapToStringJson<T> apply(MapToNode<T> mapToNode)  {
        return new MapToStringJson<T>() {
            @Override
            public String apply(T t) throws ObjectMapper.MappingException {
                Node apply = mapToNode.apply(t);
				return MapFromNodeToString.this.serializer.apply(apply);
            }
        };
    }
}
