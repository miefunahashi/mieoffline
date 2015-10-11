package com.mieoffline.json;

import com.mieoffline.functional.Function;


public class MapToNodeFromString<T> implements Function<MapFromNode<T>, MapFromStringJson<T>, ObjectMapper.MappingException> {
    final MapToNode<String> deserializer;

    public MapToNodeFromString(MapToNode<String> deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public MapFromStringJson<T> apply(MapFromNode<T> tMapFromNode) throws ObjectMapper.MappingException {
        return new MapFromStringJson<T>() {
            @Override
            public T apply(String string) throws ObjectMapper.MappingException {
                return tMapFromNode.apply(MapToNodeFromString.this.deserializer.apply(string));
            }
        };
    }
}
