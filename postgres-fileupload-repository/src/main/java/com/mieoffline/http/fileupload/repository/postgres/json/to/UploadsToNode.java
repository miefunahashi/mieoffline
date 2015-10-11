package com.mieoffline.http.fileupload.repository.postgres.json.to;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mieoffline.http.fileupload.repository.postgres.model.Upload;
import com.mieoffline.http.fileupload.repository.postgres.Uploads;
import com.mieoffline.http.fileupload.repository.postgres.json.constants.UploadsMappingConstants;
import com.mieoffline.json.MapToNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;
import com.mieoffline.json.ObjectMapper;


public class UploadsToNode implements MapToNode<Uploads> {
    private final MapToNode<Upload> uploadMapToNode;

    public UploadsToNode(MapToNode<Upload> uploadMapToNode) {
        this.uploadMapToNode = uploadMapToNode;
    }

    @Override
    public Node apply(Uploads uploads) throws ObjectMapper.MappingException {
        final ImmutableList.Builder<Node> immutableList = ImmutableList.builder();
        for (Upload upload : uploads.getUploads()) {
            try {
                immutableList.add(this.uploadMapToNode.apply(upload));
            } catch (ObjectMapper.MappingException e) {
                throw new ObjectMapper.MappingException("Error creating node", e);
            }
        }
        ImmutableList<Node> build = immutableList.build();
        return new Node(new NodeMap(ImmutableMap.<String, Node>of(UploadsMappingConstants.UPLOADS, new Node(build))));
    }
}
