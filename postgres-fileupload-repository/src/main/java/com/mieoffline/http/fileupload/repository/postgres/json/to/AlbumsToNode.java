package com.mieoffline.http.fileupload.repository.postgres.json.to;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mieoffline.http.fileupload.repository.postgres.model.Album;
import com.mieoffline.http.fileupload.repository.postgres.Albums;
import com.mieoffline.http.fileupload.repository.postgres.json.constants.AlbumsMappingConstants;
import com.mieoffline.json.MapToNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;
import com.mieoffline.json.ObjectMapper;

public class AlbumsToNode implements MapToNode<Albums> {
    final MapToNode<Album> mapToNodeAlbum;

    public AlbumsToNode(MapToNode<Album> mapToNodeAlbum) {
        this.mapToNodeAlbum = mapToNodeAlbum;
    }

    @Override
    public Node apply(Albums albums) throws ObjectMapper.MappingException {
        final ImmutableList.Builder<Node> nodeBuilder = ImmutableList.builder();
        ImmutableSet<Album> albumsList = albums.getAlbums();
        for(Album album: albumsList) {
            nodeBuilder.add(this.mapToNodeAlbum.apply(album));
        }
        return new Node(new NodeMap(ImmutableMap.of(AlbumsMappingConstants.ALBUMS, new Node(nodeBuilder.build()))));
    }
}
