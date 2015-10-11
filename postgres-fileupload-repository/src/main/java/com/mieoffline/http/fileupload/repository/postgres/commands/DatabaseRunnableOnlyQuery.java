package com.mieoffline.http.fileupload.repository.postgres.commands;

import com.mieoffline.functional.Producer;

public class DatabaseRunnableOnlyQuery implements Producer<DatabaseQueryDefinition<Void,Void>,Throwable> {
  
    private final String createQuery;

    public DatabaseRunnableOnlyQuery(String createQuery) {
  
        this.createQuery = createQuery;
    }


    @Override
    public DatabaseQueryDefinition<Void, Void> apply(Void aVoid) throws Throwable {

        return new DatabaseQueryDefinition.Builder<Void,Void>().setQuery(this.createQuery).setFunction(DatabaseQueryDefinition.NULL_DATABASE_QUERY_FUNCTION).build();
    }


    public static class AlbumCreateException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = -6091800876065066505L;

		public AlbumCreateException(String s, Exception e) {
            super(s, e);
        }
    }

}
