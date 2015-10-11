package com.mieoffline.http.fileupload.repository.postgres.json.from;

import com.mieoffline.http.fileupload.repository.postgres.model.Album;
import com.mieoffline.http.fileupload.repository.postgres.json.constants.AlbumMappingConstants;
import com.mieoffline.http.fileupload.repository.postgres.json.constants.NodeMappingConstants;
import com.mieoffline.json.MapFromNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;
import com.mieoffline.json.ObjectMapper;
import com.mieoffline.site.Value;

public class AlbumFromNode implements MapFromNode<Album> {


    @Override
    public Album apply(Node node) throws ObjectMapper.MappingException {
        NodeMap nodeMap = node.asMap().orElseThrow(() -> new ObjectMapper.MappingException(NodeMappingConstants.MISSING_JSON_OBJECT));
        try {
            return new Album.Builder()
                    .setDecription(nodeMap.asString(AlbumMappingConstants.DESCRIPTION).orElseThrow(() -> new ObjectMapper.MappingException("Missing description in album")))
                    .setName(nodeMap.asString(AlbumMappingConstants.NAME).orElseThrow(() -> new ObjectMapper.MappingException("Missing name in album")))
                    .build();
        } catch (Value.BuilderIncompleteException e) {
            throw new ObjectMapper.MappingException("Unable to build Album", e);
        }
    }
}
