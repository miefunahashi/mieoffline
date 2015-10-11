package com.mieoffline.server.postgres;

import com.google.common.collect.Lists;
import com.jolbox.bonecp.BoneCP;
import com.markoffline.site.database.model.IDatabase;
import com.mieoffline.http.fileupload.repository.postgres.commands.DatabaseRunnableQuery;
import com.mieoffline.http.fileupload.repository.postgres.commands.QueryParamterModel;
import com.mieoffline.http.fileupload.repository.postgres.constants.AlbumConstants;
import com.mieoffline.http.fileupload.repository.postgres.constants.AlbumItemConstants;
import com.mieoffline.http.fileupload.repository.postgres.constants.FileUploadPartConstants;
import com.mieoffline.http.fileupload.repository.postgres.constants.FileUploadsConstants;
import com.mieoffline.server.postgres.config.BaseServletConfig;
import com.mieoffline.server.postgres.controllers.base.BoneCPConnectionPoolSupplier;
import com.mieoffline.server.postgres.controllers.base.BoneCPConnectionPoolSupplier.BoneCPConnectionPoolSupplierException;
import com.mieoffline.server.services.BaseServlet;
import com.mieoffline.site.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class MieCMS {
	private static final Logger LOGGER = LoggerFactory.getLogger(MieCMS.class);

	public static void main(String args[]) throws MyApplicationException {

		try (final BoneCP config = new BoneCPConnectionPoolSupplier().apply(null)) {

			try (final Database connectionSupplier = new Database(config)) {
				deleteExistingTables(connectionSupplier);
				createNewTables(connectionSupplier);
				final BaseServletConfig baseServletConfig = new BaseServletConfig();
				final BaseServlet apply = getApply(connectionSupplier, baseServletConfig);
				startTomcat(apply);
			}
		} catch (SQLException e) {
			throw new MyApplicationException("Could not setup database", e);
		} catch (BoneCPConnectionPoolSupplierException e1) {
			throw new MyApplicationException("Could not setup database", e1);
		}

	}

	private static BaseServlet getApply(Database connectionSupplier, BaseServletConfig baseServletConfig)
			throws MyApplicationException {
		try {
			return baseServletConfig.apply(connectionSupplier);
		} catch (BaseServletConfig.BaseServletException e) {
			throw new MyApplicationException("Could not configure servlet", e);
		}
	}

	private static void startTomcat(BaseServlet apply) throws MyApplicationException {
		try {
			new TomcatService(apply, 9090).apply(null);
		} catch (TomcatService.TomcatServiceException e) {
			throw new MyApplicationException("Error running tomcat", e);
		}
	}

	private static void createNewTables(IDatabase connectionSupplier) {

		Lists.newArrayList(FileUploadsConstants.CREATE_QUERY, FileUploadPartConstants.CREATE_QUERY,
				AlbumConstants.CREATE_QUERY, AlbumItemConstants.CREATE_QUERY).stream().forEach((value) -> {
					try {
						new DatabaseRunnableQuery(connectionSupplier, new QueryParamterModel.Builder().setQuery(value).setReturnGeneratedKeys(false).build()).apply(null);
					} catch (DatabaseRunnableQuery.DatabaseRunnableQueryException | Value.BuilderIncompleteException e) {
						LOGGER.error("Delete failed: possibly okay",e);
						throw new IllegalStateException();
					}
				});
	}

	private static void deleteExistingTables(Database connectionSupplier) {

		Lists.newArrayList(AlbumItemConstants.DELETE_QUERY, AlbumConstants.DELETE_QUERY,
				FileUploadPartConstants.DELETE_QUERY, FileUploadsConstants.DELETE_QUERY).stream().forEach((value) -> {
					try {
						new DatabaseRunnableQuery(connectionSupplier,  new QueryParamterModel.Builder().setQuery(value).setReturnGeneratedKeys(false).build()).apply(null);
					} catch (DatabaseRunnableQuery.DatabaseRunnableQueryException| Value.BuilderIncompleteException e) {
						LOGGER.warn("Delete failed: possibly okay",e);
					}
				});

	}

	public static class MyApplicationException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -696714636415901985L;

		public MyApplicationException(String s, Throwable e) {
			super(s, e);
		}
	}

}