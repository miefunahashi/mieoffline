package com.mieoffline.http.fileupload.repository.postgres.json.from;

import com.markoffline.site.database.model.DatabaseEntity;
import com.mieoffline.http.fileupload.repository.postgres.json.constants.DatabaseEntityMappingConstants;
import com.mieoffline.json.MapFromNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;
import com.mieoffline.json.ObjectMapper;
import com.mieoffline.site.Value;

import java.io.Serializable;

public class DatabaseEntityFromNode<OBJECT_TYPE extends Serializable> implements MapFromNode<DatabaseEntity<OBJECT_TYPE>> {

    private final MapFromNode<Long> kMapping;
    private final MapFromNode<OBJECT_TYPE> vMapping;

    public DatabaseEntityFromNode(MapFromNode<Long> kMapping, MapFromNode<OBJECT_TYPE> vMapping) {
        this.kMapping = kMapping;
        this.vMapping = vMapping;
    }

    @Override
    public DatabaseEntity<OBJECT_TYPE> apply(Node node) throws ObjectMapper.MappingException {
        try {
            final NodeMap object = node.asMap().orElseThrow(() -> new ObjectMapper.MappingException("No object"));
            final Long k = this.kMapping.apply(object.get(DatabaseEntityMappingConstants.DATABASE_REFERENCE).orElseThrow(() -> new ObjectMapper.MappingException("No database reference")));
            final OBJECT_TYPE v = this.vMapping.apply(object.get(DatabaseEntityMappingConstants.OBJECT).orElseThrow(() -> new ObjectMapper.MappingException("No database object")));
            return new DatabaseEntity.Builder<OBJECT_TYPE>()
                    .setDatabaseReference(k)
                    .setObject(v)
                    .build();
        } catch (Value.BuilderIncompleteException e) {
            throw new ObjectMapper.MappingException("Could not build database entity", e);
        }
    }
}
