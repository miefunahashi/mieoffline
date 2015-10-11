package com.mieoffline.json;

import com.mieoffline.functional.Function;

public class ObjectMapper<T> {
    private final Function<Node, T, ObjectMapper.MappingException> fromJson;
    private final Function<T, Node, ObjectMapper.MappingException> toJson;

    public ObjectMapper(Function<Node, T,ObjectMapper.MappingException> fromJson, Function<T, Node, ObjectMapper.MappingException> toJson) {
        this.fromJson = fromJson;
        this.toJson = toJson;
    }

    public T fromJson(Node node) throws MappingException {
        try {
            return this.fromJson.apply(node);
        } catch (ObjectMapper.MappingException e) {
            throw new MappingException("Error reading JSON", e);
        } catch (Exception e) {
            throw new MappingException("Could not map JSON", e);
        }
    }
    public Node toJson(T t) throws MappingException {
        try {
            return this.toJson.apply(t);
        } catch (ObjectMapper.MappingException e) {
            throw new MappingException("Error writing JSON", e);
        } catch (Exception e) {
            throw new MappingException("Could mapping JSON", e);
        }
    }


    public static class MappingException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = -6790320221013183193L;

		public MappingException(String s, Exception e) {
            super(s, e);
        }

        public MappingException(String s) {
            super(s);
        }
    }
}
