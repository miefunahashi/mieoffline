package com.mieoffline.http.fileupload.repository.postgres.json.to;

import com.google.common.collect.ImmutableMap;
import com.mieoffline.http.fileupload.repository.postgres.model.Album;
import com.mieoffline.http.fileupload.repository.postgres.json.constants.AlbumMappingConstants;
import com.mieoffline.json.MapToNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;
import com.mieoffline.json.ObjectMapper;

public class AlbumToNode implements MapToNode<Album> {
    @Override
    public Node apply(Album album) throws ObjectMapper.MappingException {
        return new Node(new NodeMap(ImmutableMap.of(AlbumMappingConstants.NAME, new Node(album.getName()), AlbumMappingConstants.DESCRIPTION, new Node(album.getDescription()))));
    }
}
