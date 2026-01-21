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

package app.krista.extensions.krista.access_existing_rest_service.openapi;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import app.krista.extensions.krista.access_existing_rest_service.data.RequestInfo;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JaxrsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JaxrsClient.class);

    /** Shared HTTP client instance for connection pooling and reuse. */
    private final Client client;

    public JaxrsClient() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(MultiPartFeature.class);
        this.client = ClientBuilder.newClient(clientConfig);
    }

    public Response callSync(RequestInfo requestInfo) {
        Objects.requireNonNull(requestInfo);
        LOGGER.debug("RequestInfo: {}", requestInfo);
        String httpMethod = requestInfo.getHttpMethod();
        WebTarget webTarget = client.target(requestInfo.getServerAddress()).path(requestInfo.getPathTemplate());
        for (Map.Entry<String, Object> queryParameter : requestInfo.getQueryParameters().entrySet()) {
            webTarget = webTarget.queryParam(queryParameter.getKey(), queryParameter.getValue());
        }
        for (Map.Entry<String, Object> pathParams : requestInfo.getPathParameters().entrySet()) {
            webTarget = webTarget.resolveTemplate(pathParams.getKey(), pathParams.getValue());
        }
        if (requestInfo.getBody() != null) {
            if (requestInfo.getBody() instanceof InputStream) {
                Invocation.Builder builder = getBuilder(requestInfo, webTarget, MediaType.APPLICATION_OCTET_STREAM);
                return builder.method(httpMethod,
                        Entity.entity(requestInfo.getBody(), MediaType.APPLICATION_OCTET_STREAM));
            }
            if (requestInfo.getBody() instanceof FormDataMultiPart) {
                Invocation.Builder builder = getBuilder(requestInfo, webTarget, null);
                FormDataMultiPart body = (FormDataMultiPart) requestInfo.getBody();
                return builder.method(httpMethod, Entity.entity(body, body.getMediaType()));
            }
            Invocation.Builder builder = getBuilder(requestInfo, webTarget, requestInfo.getMediaType());
            return builder.method(httpMethod, Entity.entity(requestInfo.getBody(), requestInfo.getMediaType()));
        }
        Invocation.Builder builder = getBuilder(requestInfo, webTarget, requestInfo.getMediaType());
        return builder.build(httpMethod).invoke();
    }

    private static Invocation.Builder getBuilder(RequestInfo requestInfo, WebTarget webTarget, String mediaType) {
        if (mediaType == null) {
            mediaType = "*/*";
        }
        Invocation.Builder builder = webTarget.request(mediaType);
        for (Map.Entry<String, Object> header : requestInfo.getHeaders().entrySet()) {
            builder.header(header.getKey(), header.getValue());
        }
        for (Map.Entry<String, String> cookie : requestInfo.getCookies().entrySet()) {
            builder.cookie(new Cookie(cookie.getKey(), cookie.getValue()));
        }
        return builder;
    }

}
