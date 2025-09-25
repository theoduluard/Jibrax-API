package com;

import com.rest.HtmlResource;
import com.rest.SwaggerResource;
import com.rest.TeamResource;
import com.rest.UserResource;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<>();

        resources.add(ObjectMapperProvider.class);
        resources.add(OpenApiResource.class);
        resources.add(SwaggerResource.class);
        resources.add(UserResource.class);
        resources.add(TeamResource.class);
        resources.add(HtmlResource.class);

        return resources;
    }
}