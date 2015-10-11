package com.mieoffline.http.fileupload.repository.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUtils  {
    public boolean isNext(ResultSet result) throws SQLUtilsException {
        try {
            return result.next();
        } catch (SQLException e) {
            throw new SQLUtilsException("Unable to get next entry in database", e);
        }
    }

    public PreparedStatement getPreparedStatement(Connection conn, String readGivenId) throws SQLUtilsException {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(readGivenId);
        } catch (SQLException e) {
            throw new SQLUtilsException("Cannot create query",e);
        }
        return ps;
    }
    public static class SQLUtilsException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = -23603553455850135L;

		public SQLUtilsException(String s, Exception e) {
            super(s,e);
        }
    }

}
