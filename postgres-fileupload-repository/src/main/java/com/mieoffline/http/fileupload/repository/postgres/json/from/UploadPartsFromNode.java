package com.mieoffline.http.fileupload.repository.postgres.json.from;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mieoffline.http.fileupload.repository.postgres.UploadParts;
import com.mieoffline.http.fileupload.repository.postgres.json.JsonHelper;
import com.mieoffline.http.fileupload.repository.postgres.json.constants.NodeMappingConstants;
import com.mieoffline.json.MapFromNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;
import com.mieoffline.json.ObjectMapper;

import static com.mieoffline.http.fileupload.repository.postgres.json.constants.UploadPartsMappingConstants.*;


public class UploadPartsFromNode implements MapFromNode<UploadParts> {
    private final MapFromNode<Boolean> booleanMapFromNode;
    private final JsonHelper jsonHelper;

    public UploadPartsFromNode(MapFromNode<Boolean> booleanMapFromNode, JsonHelper jsonHelper) {
        this.booleanMapFromNode = booleanMapFromNode;
        this.jsonHelper = jsonHelper;
    }


    @Override
    public UploadParts apply(Node node) throws ObjectMapper.MappingException {
        NodeMap nodeMap = node.asMap().orElseThrow(() -> new ObjectMapper.MappingException(NodeMappingConstants.MISSING_JSON_OBJECT));
        final String size = nodeMap.asString(SIZE).orElseThrow(() -> new ObjectMapper.MappingException("Missing size"));
        final String referenceToFileUploadRequestMissingData = nodeMap.asString(REFERENCE_TO_FILE_UPLOAD_REQUEST_MISSING_DATA).orElseThrow(() -> new ObjectMapper.MappingException("Missing referent to file upload request"));
        final String name = nodeMap.asString(NAME).orElseThrow(() -> new ObjectMapper.MappingException("Missing name"));
        final Boolean isImage = this.booleanMapFromNode.apply(nodeMap.get(IS_IMAGE).orElseThrow(() -> new ObjectMapper.MappingException("Missing is image")));
        final String filename = nodeMap.asString(FILENAME).orElseThrow(() -> new ObjectMapper.MappingException("Missing filename"));
        final ImmutableMap<String, ImmutableList<String>> fileHeaders = this.jsonHelper.getMapeFromJsonNode(nodeMap.get(FILE_HEADERS).orElseThrow(() -> new ObjectMapper.MappingException("Missing file headers")));
        final String contentType = nodeMap.asString(CONTENT_TYPE).orElseThrow(() -> new ObjectMapper.MappingException("Missing content type"));
        return new UploadParts.Builder()
                .setContentType(contentType)
                .setFileHeaders(fileHeaders)
                .setFilename(filename)
                .setIsImage(isImage)
                .setName(name)
                .setReferenceToFileUploadRequestMissingData(referenceToFileUploadRequestMissingData)
                .setSize(size)
                .build();
    }
}
