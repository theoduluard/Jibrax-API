package com.rest;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.logging.Logger;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/front")
public class HtmlResource {

    private static final Logger logger = Logger.getLogger(HtmlResource.class.getName());

    @GET
    @Path("{path:.*}")
    public byte[] Get(@PathParam("path") String path) {
        try {
            return Files.readAllBytes(FileSystems.getDefault().getPath("src/main/webapp/"+path));
        } catch (IOException e) {
            return null;
        }
    }
}