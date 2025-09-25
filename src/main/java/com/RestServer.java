package com;

import io.undertow.Undertow;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;

import java.util.logging.Logger;

public class RestServer {

    private static final Logger log = Logger.getLogger(RestServer.class.getName());

    public static void main(String[] args) {
        UndertowJaxrsServer server = new UndertowJaxrsServer();

        RestApplication restApplication = new RestApplication();

        server.deploy(restApplication);

        server.start(
                Undertow.builder().addHttpListener(8080, "localhost")
        );
    }
}
