/*
 * Openapi Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.krista.access_existing_rest_service.resource;

import java.io.InputStream;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.api.ServiceLocator;

@Path("client")
public class ClientResource {

    private final ServiceLocator serviceLocator;

    @Inject
    public ClientResource(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Path("api")
    public ClientApiResource getClientApiResource() {
        return serviceLocator.create(ClientApiResource.class);
    }

    @GET
    @Path("{subPath:.*}")
    public Response getClientResource(@PathParam("subPath") String subPath) {
        String filePath = "ui/" + (subPath.isEmpty() ? "index.html" : subPath);
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1);
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(filePath);
        if (resourceAsStream == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (extension.equals("js")) {
            return Response.ok(resourceAsStream).header("Content-Type", "application/javascript").build();
        }
        if (extension.equals("css")) {
            return Response.ok(resourceAsStream).header("Content-Type", "text/css").build();
        }
        if (extension.equals("html")) {
            return Response.ok(resourceAsStream).header("Content-Type", "text/html").build();
        }
        if (extension.equals("ico")) {
            return Response.ok(resourceAsStream).header("Content-Type", "image/x-icon").build();
        }
        return Response.ok(resourceAsStream).build();
    }

}
