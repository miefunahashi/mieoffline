package com.mieoffline.http.fileupload.repository.postgres.json.from;

import com.markoffline.site.database.model.DatabaseEntity;
import com.markoffline.site.database.model.DatabaseReference;
import com.mieoffline.http.fileupload.repository.postgres.json.constants.DatabaseEntityMappingConstants;
import com.mieoffline.json.MapFromNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;
import com.mieoffline.json.ObjectMapper;
import com.mieoffline.site.Value;

import java.io.Serializable;

public class DatabaseEntityFromNode<KEY_TYPE extends Serializable, OBJECT_TYPE extends Serializable> implements MapFromNode<DatabaseEntity<KEY_TYPE,OBJECT_TYPE>> {

    private final MapFromNode<KEY_TYPE> kMapping;
    private final MapFromNode<OBJECT_TYPE> vMapping;

    public DatabaseEntityFromNode(MapFromNode<KEY_TYPE> kMapping, MapFromNode<OBJECT_TYPE> vMapping) {
        this.kMapping = kMapping;
        this.vMapping = vMapping;
    }

    @Override
    public DatabaseEntity<KEY_TYPE, OBJECT_TYPE> apply(Node node) throws ObjectMapper.MappingException {
        try {
            final NodeMap object = node.asMap().orElseThrow(() -> new ObjectMapper.MappingException("No object"));
            final KEY_TYPE k = this.kMapping.apply(object.get(DatabaseEntityMappingConstants.DATABASE_REFERENCE).orElseThrow(() -> new ObjectMapper.MappingException("No database reference")));
            final DatabaseReference<KEY_TYPE> databaseReference = new DatabaseReference.Builder<KEY_TYPE>().setReference(k).build();
            final OBJECT_TYPE v = this.vMapping.apply(object.get(DatabaseEntityMappingConstants.OBJECT).orElseThrow(() -> new ObjectMapper.MappingException("No database object")));
            return new DatabaseEntity.Builder<KEY_TYPE, OBJECT_TYPE>()
                    .setDatabaseReference(databaseReference)
                    .setObject(v)
                    .build();
        } catch (Value.BuilderIncompleteException e) {
            throw new ObjectMapper.MappingException("Could not build database entity", e);
        }
    }
}
