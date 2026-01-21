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

package app.krista.extensions.krista.access_existing_rest_service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.impl.anno.EntityRequest;
import app.krista.extensions.krista.access_existing_rest_service.auth.AuthenticationInfo;
import app.krista.extensions.krista.access_existing_rest_service.auth.Authenticator;
import app.krista.extensions.krista.access_existing_rest_service.auth.AuthenticatorFactory;
import app.krista.extensions.krista.access_existing_rest_service.data.RequestInfo;
import app.krista.extensions.krista.access_existing_rest_service.model.OpenApiContext;
import app.krista.extensions.krista.access_existing_rest_service.openapi.JaxrsClient;
import app.krista.extensions.krista.access_existing_rest_service.openapi.SpecStore;
import app.krista.extensions.krista.access_existing_rest_service.services.InvokerAttributes;
import app.krista.model.base.EntityValue;
import app.krista.model.entity.EntityType;
import com.google.gson.Gson;
import org.jvnet.hk2.annotations.Service;

@SuppressWarnings({"unused", "rawtypes"})
@Service
public class EntityRequests {

    private final SpecStore specStore;
    private final InvokerAttributes invokerAttributes;
    private final AuthenticatorFactory authenticatorFactory;
    private final JaxrsClient jaxrsClient;
    private final Gson gson;

    @Inject
    public EntityRequests(SpecStore specStore, InvokerAttributes invokerAttributes,
            AuthenticatorFactory authenticatorFactory, JaxrsClient jaxrsClient, Gson gson) {
        this.specStore = specStore;
        this.invokerAttributes = invokerAttributes;
        this.authenticatorFactory = authenticatorFactory;
        this.jaxrsClient = jaxrsClient;
        this.gson = gson;
    }

    @EntityRequest(EntityRequest.Type.CREATE)
    public Object create(EntityType entityType, Map<String, Object> fields) throws IOException {
        RequestInfo.Builder requestInfoBuilder = getBaseRequestBuilder();
        OpenApiContext context = specStore.getSpec();
        requestInfoBuilder.pathTemplate(context.getEntityRequests().create);
        Map<String, Object> body = Map.of("fields", fields, "entityType", entityType);
        requestInfoBuilder.body(body);
        try (Response response = jaxrsClient.callSync(requestInfoBuilder.build())) {
            if (response.getStatus() != 200) {
                throw new IllegalStateException("Failed to create entity: " + response.readEntity(String.class));
            }
        }
        return new EntityValue(entityType.getEntityName(), fields);
    }

    private RequestInfo.Builder getBaseRequestBuilder() {
        RequestInfo.Builder requestBuilder = new RequestInfo.Builder();
        requestBuilder.httpMethod("POST");
        OpenApiContext context = specStore.getSpec();
        requestBuilder.serverAddress(invokerAttributes.getServerAddress());
        List<AuthenticationInfo<Object>> authenticationInfoList = context.getAuthenticationInfoList();
        if (authenticationInfoList != null) {
            for (AuthenticationInfo<?> authenticationInfo : authenticationInfoList) {
                Authenticator authenticator = authenticatorFactory.create(authenticationInfo);
                authenticator.authenticate(authenticationInfo, requestBuilder);
            }
        }
        return requestBuilder;
    }

    @SuppressWarnings("unchecked")
    @EntityRequest(EntityRequest.Type.GET)
    public Object get(EntityType entityType, String primaryKey) {
        RequestInfo.Builder requestInfoBuilder = getBaseRequestBuilder();
        OpenApiContext context = specStore.getSpec();
        requestInfoBuilder.pathTemplate(context.getEntityRequests().get);
        Map<String, Object> body = Map.of("primaryKey", primaryKey, "entityType", entityType);
        requestInfoBuilder.body(body);
        try (Response response = jaxrsClient.callSync(requestInfoBuilder.build())) {
            if (response.getStatus() != 200) {
                throw new IllegalStateException("Failed to get entity: " + response.readEntity(String.class));
            }
            String s = response.readEntity(String.class);
            Map<String, Object> m = (Map<String, Object>) gson.fromJson(s, Map.class);
            return new EntityValue(entityType.getEntityName(), m);
        }
    }

    @EntityRequest(EntityRequest.Type.SEARCH)
    public List search(EntityType entityType, SearchQuery searchQuery, Long pageIndex, Integer pageSize) {
        RequestInfo.Builder requestInfoBuilder = getBaseRequestBuilder();
        OpenApiContext context = specStore.getSpec();
        requestInfoBuilder.pathTemplate(context.getEntityRequests().search);
        Map<String, Object> body =
                Map.of("searchQuery", searchQuery, "entityType", entityType, "pageIndex", pageIndex, "pageSize",
                        pageSize);
        requestInfoBuilder.body(body);
        try (Response response = jaxrsClient.callSync(requestInfoBuilder.build())) {
            if (response.getStatus() != 200) {
                throw new IllegalStateException("Failed to search entity: " + response.readEntity(String.class));
            }
            String s = response.readEntity(String.class);
            return gson.fromJson(s, List.class);
        }
    }

    // This method can not be mapped from studio
    @EntityRequest(EntityRequest.Type.DELETE)
    public void delete(EntityType entityType, String primaryKey) {
        throw new UnsupportedOperationException("Delete is not supported");
    }

    @SuppressWarnings("unchecked")
    @EntityRequest(EntityRequest.Type.UPDATE)
    public EntityValue update(EntityType entityType, EntityValue entityValue) {
        RequestInfo.Builder requestInfoBuilder = getBaseRequestBuilder();
        OpenApiContext context = specStore.getSpec();
        requestInfoBuilder.pathTemplate(context.getEntityRequests().update);
        Map<String, Object> body = Map.of("fields", entityValue.getFields(), "entityType", entityType);
        requestInfoBuilder.body(body);
        try (Response response = jaxrsClient.callSync(requestInfoBuilder.build())) {
            if (response.getStatus() != 200) {
                throw new IllegalStateException("Failed to update entity: " + response.readEntity(String.class));
            }
            String s = response.readEntity(String.class);
            Map<String, Object> m = (Map<String, Object>) gson.fromJson(s, Map.class);
            return new EntityValue(entityType.getEntityName(), m);
        }
    }

}
