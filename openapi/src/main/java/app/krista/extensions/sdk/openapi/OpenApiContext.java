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

package app.krista.extensions.sdk.openapi;

import app.krista.extension.executor.ExtensionCatalogRequest;
import app.krista.extension.executor.Invoker;
import app.krista.extension.executor.impl.DefaultExtensionCatalogRequest;
import app.krista.extension.request.CatalogRequest;
import app.krista.extensions.sdk.openapi.auth.AuthenticationInfo;
import app.krista.extensions.sdk.openapi.services.InvokerContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OpenApiContext {

    public static final String STORE_KEY = "OpenApiContext2";
    private final Map<String, JaxrsRequestItem> jaxrsRequests;
    private final Map<String, CatalogRequestItem> catalogRequests;
    private final String swaggerString;
    private final SpecInfo specInfo;
    private final List<AuthenticationInfo<Object>> authenticationInfos;

    public OpenApiContext(Map<String, JaxrsRequestItem> jaxrsRequests, Map<String, CatalogRequestItem> catalogRequests,
                          SpecInfo specInfo, String swaggerString, List<AuthenticationInfo<Object>> authenticationInfos) {
        this.jaxrsRequests = jaxrsRequests;
        this.catalogRequests = catalogRequests;
        this.specInfo = specInfo;
        this.swaggerString = swaggerString;
        this.authenticationInfos = authenticationInfos;
    }

    public Map<String, JaxrsRequestItem> getJaxrsRequests() {
        return jaxrsRequests;
    }

    public String getSwaggerString() {
        return swaggerString;
    }

    public SpecInfo getSpecInfo() {
        return specInfo;
    }

    public Set<String> getRequestIds() {
        return jaxrsRequests.keySet();
    }

    public CatalogRequest getCatalogRequest(OpenApiOperation openApiOperation, InvokerContext invokerContext) {
        for (Map.Entry<String, JaxrsRequestItem> entry : jaxrsRequests.entrySet()) {
            if (entry.getValue().getOperation().equals(openApiOperation.getOperation()) &&
                    entry.getValue().getPathItem().equals(openApiOperation.getPathItem())) {
                return getCatalogRequestsMap(invokerContext).get(entry.getKey());
            }
        }
        throw new IllegalArgumentException("Unknown operation :" + openApiOperation);
    }

    public OpenApiOperation getOpenApiOperation(CatalogRequest catalogRequest) {
        JaxrsRequestItem jaxrsRequestItem = jaxrsRequests.get(catalogRequest.getRequestId());
        return OpenApiOperation.create(jaxrsRequestItem);
    }

    public Map<String, CatalogRequestItem> getCatalogRequests() {
        return catalogRequests;
    }

    public List<AuthenticationInfo<Object>> getAuthenticationInfo() {
        return authenticationInfos;
    }

    public Map<String, ExtensionCatalogRequest> getCatalogRequestsMap(InvokerContext invokerContext) {
        Invoker invoker = invokerContext.getInvoker();
        Map<String, ExtensionCatalogRequest> result = new HashMap<>();
        for (Map.Entry<String, CatalogRequestItem> entry : getCatalogRequests().entrySet()) {
            CatalogRequestItem requestItem = entry.getValue();
            String requestId = requestItem.getRequestId();
            result.put(entry.getKey(), new DefaultExtensionCatalogRequest<>(invoker.getExtension(),
                    invoker.getExtension().getImplementedDomains().get(0),
                    requestId, invokerContext.getAreaName(),
                    requestItem.getRequestName(),
                    requestItem.getDescription(),
                    requestItem.getType(),
                    requestItem.getInputFields(),
                    requestItem.getOutputFields()));
        }
        return result;
    }

}
