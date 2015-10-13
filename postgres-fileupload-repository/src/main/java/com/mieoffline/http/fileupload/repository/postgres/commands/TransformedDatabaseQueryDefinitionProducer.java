package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.mieoffline.functional.Producer;
import com.mieoffline.http.fileuploadrepository.model.FileUpload;

/**
 * Created by mark on 11/10/15.
 */
public class TransformedDatabaseQueryDefinitionProducer implements Producer<DatabaseQueryDefinition<Long, FileUpload>, Throwable> {
    final Producer<DatabaseQueryDefinition<Long, FileUpload>, Throwable> original;

    public TransformedDatabaseQueryDefinitionProducer(Producer<DatabaseQueryDefinition<Long, FileUpload>, Throwable> original) {
        this.original = original;
    }

    @Override
    public DatabaseQueryDefinition<Long, FileUpload> apply(Void aVoid) throws Throwable {
        return null;
    }
}
