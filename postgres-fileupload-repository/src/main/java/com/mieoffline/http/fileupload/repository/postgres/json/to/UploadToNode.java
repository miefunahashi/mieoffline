package com.mieoffline.http.fileupload.repository.postgres.json.to;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mieoffline.http.fileupload.repository.postgres.model.Upload;
import com.mieoffline.http.fileupload.repository.postgres.UploadParts;
import com.mieoffline.http.fileupload.repository.postgres.json.JsonHelper;
import com.mieoffline.http.fileupload.repository.postgres.json.constants.UploadMappingConstants;
import com.mieoffline.json.MapToNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;
import com.mieoffline.json.ObjectMapper;

public class UploadToNode implements MapToNode<Upload> {
    private final MapToNode<UploadParts> uploadPartsMapToNode;
    private final JsonHelper jsonHelper;

    public UploadToNode(MapToNode<UploadParts> uploadPartsMapToNode, JsonHelper jsonHelper) {
        this.uploadPartsMapToNode = uploadPartsMapToNode;
        this.jsonHelper = jsonHelper;
    }

    @Override
    public Node apply(Upload upload) throws ObjectMapper.MappingException {
        final ImmutableMap.Builder<String, Node> uploadNodeMap = ImmutableMap.builder();
        final ImmutableList.Builder<Node> uploadPartsNodes = ImmutableList.builder();
        ImmutableList<UploadParts> parts = upload.getParts();
        for (UploadParts uploadParts : parts) {
            uploadPartsNodes.add(this.uploadPartsMapToNode.apply(uploadParts));
        }
        uploadNodeMap.put(UploadMappingConstants.HTTP_HEADERS, this.jsonHelper.getJsonNodesFromMap(upload.getHttpHeaders()));
        uploadNodeMap.put(UploadMappingConstants.PROPERTIES, this.jsonHelper.getJsonNodesFromMap(upload.getProperties()));
        uploadNodeMap.put(UploadMappingConstants.UPLOAD_PARTS, new Node(uploadPartsNodes.build()));
        return new Node(new NodeMap(uploadNodeMap.build()));
    }
}
