package com.mieoffline.server.postgres;

import com.mieoffline.functional.MieRunnable;
import com.mieoffline.server.services.BaseServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class TomcatService implements MieRunnable<TomcatService.TomcatServiceException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TomcatService.class);
    private final HttpServlet httpServlet;
    private final int port;

    public TomcatService(HttpServlet baseServlet, int port) {
        this.httpServlet = baseServlet;
        this.port = port;
    }

    @Override
    public Void apply(Void avoid) throws TomcatServiceException {
        final Tomcat tomcat = new Tomcat();

        final Path tempDirectory = getTempPath();


        tomcat.setPort(this.port);

        final StandardContext rootCtx = (StandardContext) tomcat.addContext("", tempDirectory.toString());


        final Wrapper wrapper = tomcat.addServlet("", BaseServlet.class.getName(), this.httpServlet);
        wrapper.setMultipartConfigElement(new MultipartConfigElement("", 100000000, 100000000, 1000000));

        rootCtx.addServletMapping("/*", BaseServlet.class.getName());

        rootCtx.setAllowCasualMultipartParsing(true);


        try {
            tomcat.start();

        } catch (LifecycleException e) {
            LOGGER.debug("Error starting", e);
        }
        final Server server = tomcat.getServer();
        server.await();
        return null;
    }

    public static class TomcatServiceException extends Exception {

        /**
		 * 
		 */
		private static final long serialVersionUID = 4289733901339536443L;

		public TomcatServiceException(String s, IOException e) {
            super(s, e);
        }
    }

    private Path getTempPath() throws TomcatServiceException {
        final Path tempDirectory;
        try {
            tempDirectory = Files.createTempDirectory(null);
        } catch (IOException e) {
            throw new TomcatServiceException("Could not create a temporary directory", e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (Files.exists(tempDirectory)) {
                    try {
                        Files.walkFileTree(tempDirectory, new SimpleFileVisitor<Path>() {
                            @Override
                            public FileVisitResult postVisitDirectory(Path directory, IOException exc)
                                    throws IOException {
                                Files.deleteIfExists(directory);
                                return super.postVisitDirectory(directory, exc);
                            }

                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                                    throws IOException {
                                Files.deleteIfExists(file);
                                return super.visitFile(file, attrs);

                            }
                        });
                        Files.deleteIfExists(tempDirectory);
                        LOGGER.debug("Cleaned up");
                    } catch (IOException e) {
                        LOGGER.error("Error tidying up", e);
                    }
                }
            }

        });
        return tempDirectory;
    }

}
