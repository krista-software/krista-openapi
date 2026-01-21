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

package app.krista.extensions.sdk.openapi.jaxrs.impl;

import app.krista.extensions.sdk.openapi.OpenApiInputValue;
import app.krista.extensions.sdk.openapi.OpenApiOperation;
import app.krista.extensions.sdk.openapi.OpenApiOutputValue;
import app.krista.extensions.sdk.openapi.OpenApiToOperationFromBinding;
import app.krista.extensions.sdk.openapi.util.Pair;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class JaxrsOpenApiToOperationFromJaxrsRequest implements OpenApiToOperationFromBinding<Invocation, Object, Response> {
    private final OpenApiOperation operation;
    private final String httpMethod;


    public JaxrsOpenApiToOperationFromJaxrsRequest(OpenApiOperation operation, String httpMethod) {
        this.operation = operation;
        this.httpMethod = httpMethod;
    }

    @Override
    public Invocation getBinding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public OpenApiOperation getOperation() {
        return operation;
    }

    @Override
    public OpenApiInputValue mapInput(Object bindingInput) {
        return new OpenApiInputValue(bindingInput);
    }

    @Override
    public Response mapOutput(OpenApiOutputValue openApiOutput) {
        return Response.ok(openApiOutput).build();
    }

    @Override
    public CompletableFuture<Response> callAsync(Object bindingInput) {
        return CompletableFuture.supplyAsync(() -> callSync(bindingInput));
    }

    private static Invocation.Builder getBuilder(RequestInfo requestInfo, WebTarget webTarget, String mediaType) {
        if (mediaType == null) {
            mediaType = "*/*";
        }
        Invocation.Builder builder = webTarget.request(mediaType);
        for (Map.Entry<String, Object> header : requestInfo.headers.entrySet()) {
            builder.header(header.getKey(), header.getValue());
        }
        for (Pair<String, String> cookie : requestInfo.cookies) {
            builder.cookie(new Cookie(cookie.getKey(), cookie.getValue()));
        }
        return builder;
    }

    @Override
    public Response callSync(Object bindingInput) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(MultiPartFeature.class);
        Objects.requireNonNull(bindingInput);
        RequestInfo requestInfo = (RequestInfo) bindingInput;
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget webTarget = client.target(requestInfo.serverAddress).path(operation.getPathTemplate());
        for (Map.Entry<String, Object> queryParameter : requestInfo.queryParameters.entrySet()) {
            webTarget = webTarget.queryParam(queryParameter.getKey(), queryParameter.getValue());
        }
        for (Map.Entry<String, Object> pathParams : requestInfo.pathParameters.entrySet()) {
            webTarget = webTarget.resolveTemplate(pathParams.getKey(), pathParams.getValue());
        }
        if (requestInfo.body != null) {
            if (requestInfo.body instanceof InputStream) {
                Invocation.Builder builder = getBuilder(requestInfo, webTarget, MediaType.APPLICATION_OCTET_STREAM);
                return builder.method(httpMethod, Entity.entity(requestInfo.body, MediaType.APPLICATION_OCTET_STREAM));
            }
            if (requestInfo.body instanceof FormDataMultiPart) {
                Invocation.Builder builder = getBuilder(requestInfo, webTarget, null);
                FormDataMultiPart body = (FormDataMultiPart) requestInfo.body;
                return builder.method(httpMethod, Entity.entity(body, body.getMediaType()));
            }
            Invocation.Builder builder = getBuilder(requestInfo, webTarget, MediaType.APPLICATION_JSON);
            return builder.method(httpMethod, Entity.entity(requestInfo.body, MediaType.APPLICATION_JSON));
        }
        Invocation.Builder builder = getBuilder(requestInfo, webTarget, MediaType.APPLICATION_JSON);
        return builder.build(httpMethod).invoke();
    }


}
