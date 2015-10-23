package com.mieoffline.http.fileupload.repository.postgres.json.to;

import com.google.common.collect.ImmutableMap;
import com.markoffline.site.database.model.DatabaseEntity;
import com.mieoffline.json.*;
import java.io.Serializable;

import static com.mieoffline.http.fileupload.repository.postgres.json.constants.DatabaseEntityMappingConstants.DATABASE_REFERENCE;
import static com.mieoffline.http.fileupload.repository.postgres.json.constants.DatabaseEntityMappingConstants.OBJECT;

public class DatabaseEntityToNode< V extends Serializable> implements MapToNode<DatabaseEntity<V>> {
    final MapToNode<Long> kMapping;
    final MapToNode<V> vMapping;

    public DatabaseEntityToNode(MapToNode<Long> kMapping, MapToNode<V> vMapping) {
        this.kMapping = kMapping;
        this.vMapping = vMapping;
    }



    @Override
    public Node apply(DatabaseEntity< V> kvDatabaseEntity) throws ObjectMapper.MappingException {
        final ImmutableMap.Builder<String, Node> nodeMap1 = ImmutableMap.builder();
        nodeMap1.put(DATABASE_REFERENCE,this.kMapping.apply(kvDatabaseEntity.getDatabaseReference()));
        nodeMap1.put(OBJECT,this.vMapping.apply(kvDatabaseEntity.getObject()));
        final NodeMap nodeMap = new NodeMap(nodeMap1.build());
        final Node node = new Node(nodeMap);
        return node;
    }
}
