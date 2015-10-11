package com.mieoffline.http.fileupload.repository.postgres.json.to;

import com.google.common.collect.ImmutableMap;
import com.mieoffline.http.fileupload.repository.postgres.UploadParts;
import com.mieoffline.http.fileupload.repository.postgres.json.JsonHelper;
import com.mieoffline.json.MapToNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;
import com.mieoffline.json.ObjectMapper;
import static com.mieoffline.http.fileupload.repository.postgres.json.constants.UploadPartsMappingConstants.*;
public class UploadPartsToNode implements MapToNode<UploadParts> {
    private final MapToNode<Boolean> booleanMapToNode;
    private final JsonHelper jsonHelper;

    public UploadPartsToNode(MapToNode<Boolean> booleanMapToNode, JsonHelper jsonHelper) {
        this.booleanMapToNode = booleanMapToNode;
        this.jsonHelper = jsonHelper;
    }

    @Override
    public Node apply(UploadParts uploadParts) throws ObjectMapper.MappingException {
        final ImmutableMap.Builder<String,Node> uploadPartsObjectNode = ImmutableMap.builder();
        uploadPartsObjectNode.put(SIZE, new Node(uploadParts.getSize()));
        uploadPartsObjectNode.put(REFERENCE_TO_FILE_UPLOAD_REQUEST_MISSING_DATA, new Node(uploadParts.getReferenceToFileUploadRequestMissingData()));
        uploadPartsObjectNode.put(NAME, new Node(uploadParts.getName()));
        uploadPartsObjectNode.put(IS_IMAGE, this.booleanMapToNode.apply(uploadParts.getIsImage()));
        uploadPartsObjectNode.put(FILENAME, new Node(uploadParts.getFilename()));
        uploadPartsObjectNode.put(FILE_HEADERS, this.jsonHelper.getJsonNodesFromMap(uploadParts.getFileHeaders()));
        uploadPartsObjectNode.put(CONTENT_TYPE, new Node(uploadParts.getContentType()));
        return new Node(new NodeMap(uploadPartsObjectNode.build()));
    }
}
