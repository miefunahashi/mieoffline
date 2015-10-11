package com.mieoffline.server.postgres.controllers.base;


import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.mieoffline.functional.Producer;
import com.mieoffline.server.postgres.model.DatabaseProperties;
import org.postgresql.Driver;

import java.sql.SQLException;
import java.util.Properties;

public class BoneCPConnectionPoolSupplier implements Producer<BoneCP, BoneCPConnectionPoolSupplier.BoneCPConnectionPoolSupplierException> {
	private final DatabaseProperties databaseProperties = new DatabaseProperties();
    public static final String JDBC_POSTGRESQL_LOCALHOST_POSTGRES = "jdbc:postgresql://%s/postgres";

    @Override
    public BoneCP apply(Void aVoid) throws BoneCPConnectionPoolSupplierException {
        try {
            Class.forName(Driver.class.getName());
        } catch (ClassNotFoundException e) {
            throw new BoneCPConnectionPoolSupplierException("Could not find Postgres Driver class", e);
        }
        BoneCPConfig config = new BoneCPConfig();    // create a new configuration object
        config.setJdbcUrl(String.format(JDBC_POSTGRESQL_LOCALHOST_POSTGRES, this.databaseProperties.hostname));    // set the JDBC url
        final Properties driverProperties = new Properties();
        driverProperties.setProperty("loglevel", "2");
      
        config.setDriverProperties(driverProperties);
        config.setUsername(this.databaseProperties.username);
        config.setPassword(this.databaseProperties.password);
        
        try {
            return new BoneCP(config);    // setup the connection pool
        } catch (SQLException e) {
            throw new BoneCPConnectionPoolSupplierException("Could not configure postgres connection", e);
        }
    }

    public static class BoneCPConnectionPoolSupplierException extends Exception {
        /**
		 * 
		 */
		private static final long serialVersionUID = -1275500100962772545L;

		public BoneCPConnectionPoolSupplierException(String s, Throwable e) {
            super(s, e);
        }
    }
}
