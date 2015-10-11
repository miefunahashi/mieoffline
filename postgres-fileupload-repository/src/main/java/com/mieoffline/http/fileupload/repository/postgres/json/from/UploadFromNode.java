package com.mieoffline.http.fileupload.repository.postgres.json.from;

import com.google.common.collect.ImmutableList;
import com.mieoffline.http.fileupload.repository.postgres.model.Upload;
import com.mieoffline.http.fileupload.repository.postgres.UploadParts;
import com.mieoffline.http.fileupload.repository.postgres.json.JsonHelper;
import com.mieoffline.http.fileupload.repository.postgres.json.constants.UploadMappingConstants;
import com.mieoffline.json.MapFromNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;
import com.mieoffline.json.ObjectMapper;
import com.mieoffline.site.Value;

public class UploadFromNode implements MapFromNode<Upload> {
    private final MapFromNode<UploadParts> uploadPartsMapFromNode;
    private final JsonHelper jsonHelper;

    public UploadFromNode(MapFromNode<UploadParts> uploadPartsMapFromNode, JsonHelper jsonHelper) {
        this.uploadPartsMapFromNode = uploadPartsMapFromNode;
        this.jsonHelper = jsonHelper;
    }

    @Override
    public Upload apply(Node node) throws ObjectMapper.MappingException {
        NodeMap nodeMap = node.asMap().orElseThrow(() -> new ObjectMapper.MappingException("Missing object"));
        ImmutableList<Node> immutableList = nodeMap.asList(UploadMappingConstants.UPLOAD_PARTS).orElseThrow(() -> new ObjectMapper.MappingException("Missing upload parts"));
        ImmutableList.Builder<UploadParts> uploadPartsBuilder = ImmutableList.builder();
        for (Node node1 : immutableList) {
            uploadPartsBuilder.add(this.uploadPartsMapFromNode.apply(node1));
        }
        ImmutableList<UploadParts> uploads = uploadPartsBuilder.build();
        try {
            return new Upload.Builder()
                    .setHttpHeaders(this.jsonHelper.getMapeFromJsonNode(nodeMap.get(UploadMappingConstants.HTTP_HEADERS).orElseThrow(() -> new ObjectMapper.MappingException("Missing http headers"))))
                    .setProperties(this.jsonHelper.getMapeFromJsonNode(nodeMap.get(UploadMappingConstants.PROPERTIES).orElseThrow(() -> new ObjectMapper.MappingException("Missing http headers"))))
                    .setUploads(uploads)
                    .build();
        } catch (Value.BuilderIncompleteException e) {
            throw new ObjectMapper.MappingException("Failed to build Upload",e);
        }
    }
}
