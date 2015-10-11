package com.mieoffline.http.fileupload.repository.postgres.json.from;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mieoffline.http.fileupload.repository.postgres.model.Album;
import com.mieoffline.http.fileupload.repository.postgres.Albums;
import com.mieoffline.http.fileupload.repository.postgres.json.constants.AlbumsMappingConstants;
import com.mieoffline.http.fileupload.repository.postgres.json.constants.NodeMappingConstants;
import com.mieoffline.json.MapFromNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.ObjectMapper;
import com.mieoffline.site.Value;

public class AlbumsFromNode implements MapFromNode<Albums> {

    private final MapFromNode<Album> albumFromNode;

    public AlbumsFromNode(MapFromNode<Album> albumFromNode) {
        this.albumFromNode = albumFromNode;
    }

    @Override
    public Albums apply(Node node) throws ObjectMapper.MappingException {
        final ImmutableList<Node> immutableList = node.asMap()
                .orElseThrow(() -> new ObjectMapper.MappingException(NodeMappingConstants.MISSING_JSON_OBJECT))
                .asList(AlbumsMappingConstants.ALBUMS).orElseThrow(() -> new ObjectMapper.MappingException("Missing album list"));
        final ImmutableSet.Builder<Album> albumsBuilder = ImmutableSet.builder();
        for (final Node n : immutableList) {
            albumsBuilder.add(this.albumFromNode.apply(n));
        }
        ImmutableSet<Album> albums = null;
        try {
            return new Albums.Builder()
                    .setAlbums(albums)
                    .build();
        } catch (Value.BuilderIncompleteException e) {
            throw new ObjectMapper.MappingException("Unable to create albums", e);
        }
    }
}
