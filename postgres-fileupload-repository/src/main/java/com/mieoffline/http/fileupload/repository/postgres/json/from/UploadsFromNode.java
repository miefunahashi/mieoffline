package com.mieoffline.http.fileupload.repository.postgres.json.from;

import com.google.common.collect.ImmutableList;
import com.mieoffline.http.fileupload.repository.postgres.model.Upload;
import com.mieoffline.http.fileupload.repository.postgres.Uploads;
import com.mieoffline.http.fileupload.repository.postgres.json.constants.NodeMappingConstants;
import com.mieoffline.http.fileupload.repository.postgres.json.constants.UploadsMappingConstants;
import com.mieoffline.json.MapFromNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;
import com.mieoffline.json.ObjectMapper;
import com.mieoffline.site.Value;

public class UploadsFromNode implements MapFromNode<Uploads> {

    private final MapFromNode<Upload> uploadMapFromNode;

    public UploadsFromNode(MapFromNode<Upload> uploadMapFromNode) {
        this.uploadMapFromNode = uploadMapFromNode;
    }

    @Override
    public Uploads apply(Node node) throws ObjectMapper.MappingException {
        final NodeMap stringNodeImmutableMap = node.asMap().orElseThrow(() -> new ObjectMapper.MappingException(NodeMappingConstants.MISSING_JSON_OBJECT));
        ImmutableList<Node> immutableList = stringNodeImmutableMap.asList(UploadsMappingConstants.UPLOADS).orElseThrow(() -> new ObjectMapper.MappingException("Missing uploads"));
        final ImmutableList.Builder<Upload> uploadsBuilder = ImmutableList.builder();
        for (Node node1 : immutableList) {
            uploadsBuilder.add(this.uploadMapFromNode.apply(node1));
        }
        try {
            return new Uploads.Builder()
                    .setUploads(uploadsBuilder.build())
                    .build();
        } catch (Value.BuilderIncompleteException e) {
            throw new ObjectMapper.MappingException("Failed to build uploads",e);
        }
    }
}