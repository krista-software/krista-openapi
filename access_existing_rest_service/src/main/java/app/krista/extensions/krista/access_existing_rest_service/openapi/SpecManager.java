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

import java.util.HashMap;
import java.util.Map;
import app.krista.extension.executor.Extension;
import app.krista.extension.executor.ExtensionCatalogRequest;
import app.krista.extension.executor.Invoker;
import app.krista.extension.executor.impl.DefaultExtensionCatalogRequest;
import app.krista.extension.request.CatalogRequest;
import app.krista.extensions.krista.access_existing_rest_service.model.CatalogRequestInfo;
import app.krista.extensions.krista.access_existing_rest_service.model.DomainInfo;
import app.krista.extensions.krista.access_existing_rest_service.model.OpenApiContext;
import app.krista.extensions.krista.access_existing_rest_service.model.RequestInfo;
import com.google.gson.Gson;
import org.glassfish.hk2.api.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpecManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecManager.class);

    private final ServiceLocator serviceLocator;
    private final SpecStore specStore;
    private final Gson gson;
    private final Invoker invoker;
    private final OpenApiContext openApiContext;

    public SpecManager(ServiceLocator serviceLocator, SpecStore specStore, Gson gson, Invoker invoker,
            OpenApiContext openApiContext) {
        this.serviceLocator = serviceLocator;
        this.specStore = specStore;
        this.gson = gson;
        this.invoker = invoker;
        this.openApiContext = openApiContext;
    }

    public Map<String, ExtensionCatalogRequest> getCatalogRequestsMap() {
        if (openApiContext == null) {
            return Map.of();
        }
        Map<String, ExtensionCatalogRequest> result = new HashMap<>();
        DomainInfo domainInfo = openApiContext.getDomainInfo();
        for (Map.Entry<String, RequestInfo> entry : openApiContext.getRequestInfoMap().entrySet()) {
            if (!entry.getValue().isCatalogRequest()) {
                continue;
            }
            CatalogRequestInfo requestItem = entry.getValue().getCatalogRequestInfo();
            result.put(entry.getKey(), getDefaultExtensionCatalogRequest(domainInfo, requestItem));
        }
        return result;
    }

    private DefaultExtensionCatalogRequest<Extension> getDefaultExtensionCatalogRequest(DomainInfo domainInfo,
            CatalogRequestInfo requestItem) {
        return new DefaultExtensionCatalogRequest<>(invoker.getExtension(),
                domainInfo.toExtensionDomain(), requestItem.getRequestId(), requestItem.getAreaName(),
                requestItem.getRequestName(), requestItem.getDescription(), requestItem.getType(),
                requestItem.getInputFields(), requestItem.getOutputFields());
    }

    /**
     * @param catalogRequest catalog request
     * @param arguments      arguments
     * @return result of catalog request execution
     */
    public Map<String, Object> executeCatalogRequest(CatalogRequest catalogRequest, Map<String, Object> arguments) {
        try {
            RequestExecutor requestExecutor = new RequestExecutor(specStore.getSpec());
            serviceLocator.inject(requestExecutor);
            Map<String, Object> result = requestExecutor.executeCatalogRequest(catalogRequest, arguments);
            String json = gson.toJson(result);
            LOGGER.info("Catalog request response to platform: {}", json);
            return result;
        } catch (Exception cause) {
            sneakilyLog(cause);
            throw cause;
        }
    }

    private static void sneakilyLog(Exception cause) {
        LOGGER.debug("Catalog request failed", cause);
    }

}
