package com.mieoffline.http.fileupload.repository.postgres;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mieoffline.http.fileuploadrepository.model.Headers;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Utils {

    public final Function<ImmutableMap<String, ImmutableList<String>>,Headers> mapToHeaders = new Function<ImmutableMap<String, ImmutableList<String>>, Headers>() {
        @Override
        public Headers apply(ImmutableMap<String, ImmutableList<String>> stringImmutableListImmutableMap) {

            ImmutableMap.Builder<CharSequence, List<CharSequence>> avroMap = ImmutableMap.builder();
            for (Map.Entry<String, ImmutableList<String>> entry : stringImmutableListImmutableMap.entrySet()) {
                avroMap.put(entry.getKey(), ImmutableList.<CharSequence>copyOf(entry.getValue()));
            }
            return Headers.newBuilder().setHeaders(avroMap.build()).build();
        }
    };
    public final Function<Headers,ImmutableMap<String, ImmutableList<String>>> headersToMap = new Function<Headers,ImmutableMap<String, ImmutableList<String>>>() {
        @Override
        public ImmutableMap<String, ImmutableList<String>> apply(Headers headers) {
            Map<CharSequence, List<CharSequence>> headers1 = headers.getHeaders();
            ImmutableMap.Builder<String, ImmutableList<String>> avroMap = ImmutableMap.builder();
            for (Map.Entry<CharSequence, List<CharSequence>> entry : headers1.entrySet()) {
                ImmutableList.Builder<String> stringBuilder = ImmutableList.builder();
                for(CharSequence s: entry.getValue()) {
                    stringBuilder.add(s.toString());
                }
                avroMap.put(entry.getKey().toString(), stringBuilder.build());
            }
            return avroMap.build();
        }
    };
}
