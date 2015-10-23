package com.mieoffline.http.fileupload.repository.postgres.json.to;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.mieoffline.http.fileuploadrepository.model.FileUploadWithoutData;
import com.mieoffline.json.MapToNode;
import com.mieoffline.json.Node;
import com.mieoffline.json.NodeMap;
import com.mieoffline.json.ObjectMapper;

import java.util.Map;
import java.util.Optional;

public class FileUploadWithoutDataToNode implements MapToNode<FileUploadWithoutData> {
    @Override
    public Node apply(FileUploadWithoutData fileUpload) throws ObjectMapper.MappingException {


        final ImmutableMap.Builder<String, Node> builder = ImmutableMap.<String, Node>builder();
        builder.put("filname", new Node(fileUpload.getFilename()));
        final Optional<String> contentType1 = fileUpload.getContentType();
        if (contentType1.isPresent()) {
            builder.put("contentType", new Node(contentType1.get()));
        }
        final Function<ImmutableList<String>, Node> function = new Function<ImmutableList<String>, Node>() {
            @Override
            public Node apply(ImmutableList<String> o) {
                return new Node(ImmutableList.copyOf(Iterables.transform(o, new Function<String, Node>() {
                    @Override
                    public Node apply(String s) {
                        return new Node(s);
                    }
                })));
            }
        };
        final Map<String, Node> map = Maps.transformValues(fileUpload.getFileHeaders(), function);
        final ImmutableMap<String, Node> fileHeaders = ImmutableMap.copyOf(map);
        builder.put("fileHeaders", new Node(new NodeMap(fileHeaders)));
        final Optional<String> name = fileUpload.getName();
        if(name.isPresent()) {
            builder.put("name", new Node(name.get()));
        }
        builder.put("id", new Node(String.valueOf(fileUpload.getReferenceToUpload())));

        final Optional<Long> size = fileUpload.getSize();
        if(size.isPresent()) {
            builder.put("size", new Node(String.valueOf(size.get())));
        }
        return new Node(new NodeMap(builder.build()));
    }
}
