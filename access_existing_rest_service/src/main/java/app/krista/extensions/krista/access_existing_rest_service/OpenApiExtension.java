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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import app.krista.extension.executor.ExtensionCatalogRequest;
import app.krista.extension.executor.impl.DefaultExtensionEntityDefinition;
import app.krista.extension.impl.anno.Extension;
import app.krista.extension.impl.anno.Field;
import app.krista.extension.impl.anno.InvokerRequest;
import app.krista.extension.impl.anno.Java;
import app.krista.extension.request.CatalogRequest;
import app.krista.extensions.krista.access_existing_rest_service.model.OpenApiContext;
import app.krista.extensions.krista.access_existing_rest_service.openapi.SpecManager;
import app.krista.extensions.krista.access_existing_rest_service.openapi.SpecStore;
import app.krista.extensions.krista.access_existing_rest_service.services.EntityRequestsManager;
import app.krista.extensions.krista.access_existing_rest_service.services.SpecManagerFactory;
import app.krista.ksdk.context.ExecutionMode;
import app.krista.ksdk.context.RequestContext;
import com.kristasoft.common.holders.ThreadLocalProxy;
import com.kristasoft.common.jaxrs.impl.BasicAuthorizationHeader;

import static app.krista.extensions.krista.access_existing_rest_service.Constants.*;

@Field.PickOne(name = Constants.AUTH_TYPE, values = {API_KEY, BASIC_AUTH})
@Field.Text(value = Constants.SERVER_ADDRESS)
@Field.Text(value = Constants.TEST_PATH, required = false)
@Field.Text(value = Constants.API_KEY, required = false, isSecured = true)
@Field.Text(value = Constants.USERNAME, required = false)
@Field.Text(value = Constants.PASSWORD, required = false, isSecured = true)
@Java(version = Java.Version.JAVA_21)
@Extension(name = "OpenAPI", version = "1.0.10", description = "Access Existing REST Services",
        implementingDomainIds = {"catEntryDomain_c2394eb0-02d9-4916-a3dc-909b1c631ea0"})
public class OpenApiExtension {

    private final SpecManagerFactory specManagerFactory;
    private final RequestContext requestContext;
    private final SpecStore specStore;
    private final EntityRequestsManager entityRequestsManager;

    @Inject
    public OpenApiExtension(SpecManagerFactory specManagerFactory, RequestContext requestContext, SpecStore specStore,
            EntityRequestsManager entityRequestsManager) {
        this.specManagerFactory = specManagerFactory;
        this.requestContext = requestContext;
        this.specStore = specStore;
        this.entityRequestsManager = entityRequestsManager;
    }

    @SuppressWarnings("unused")
    @InvokerRequest(InvokerRequest.Type.GET_CATALOG_REQUESTS)
    public Map<String, ExtensionCatalogRequest> getCatalogRequest() {
        return getSpecManager().getCatalogRequestsMap();
    }

    private SpecManager getSpecManager() {
        return specManagerFactory.create();
    }

    @SuppressWarnings("unused")
    @InvokerRequest(InvokerRequest.Type.RUNTIME_CATALOG_REQUEST)
    public Map<String, Object> executeCatalogRequest(CatalogRequest catalogRequest, Map<String, Object> arguments) {
        return getSpecManager().executeCatalogRequest(catalogRequest, arguments);
    }

    @InvokerRequest(InvokerRequest.Type.GET_DEFINITIONS)
    public List<DefaultExtensionEntityDefinition<?>> getEntityDefinitions() {
        return entityRequestsManager.getEntityDefinitions();
    }

    @SuppressWarnings("unused")
    @InvokerRequest(InvokerRequest.Type.CUSTOM_TABS)
    public Map<String, String> getCustomTabs() {
        return Map.of(
                "Open API", "rest/client/",
                "Documentation", "rest/docs/"
        );
    }

    @SuppressWarnings("unused")
    @InvokerRequest(InvokerRequest.Type.VALIDATE_ATTRIBUTES)
    public void validateAttributes(Map<String, Object> attributes) {
        String serverAddress = (String) attributes.get(SERVER_ADDRESS);
        String testPath = (String) attributes.get(TEST_PATH);
        String username = (String) attributes.get(USERNAME);
        String password = (String) attributes.get(PASSWORD);
        String apiKey = (String) attributes.get(API_KEY);
        String authType = (String) attributes.get(AUTH_TYPE);

        Objects.requireNonNull(serverAddress, Constants.SERVER_ADDRESS);
        if (testPath == null || testPath.isBlank()) {
            return;
        }

        WebTarget webTarget = ClientBuilder.newClient().target(serverAddress).path(testPath);
        Invocation.Builder invocationBuilder = webTarget.request();

        if (BASIC_AUTH.equals(authType)) {
            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                throw new IllegalStateException("You have selected Basic Auth. Please provide a non-empty Username and Password.");
            }
            String authorization = BasicAuthorizationHeader
                    .create(username, password).getAuthorization();
            invocationBuilder.header("Authorization", "Basic " + authorization);
        }

        Invocation invocation = invocationBuilder.build(HttpMethod.GET);
        try (Response response = invocation.invoke()) {
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                throw new IllegalStateException("The server didn't respond with 200");
            }
        }
    }

    // This sets hasRuntimeDefinedCatalogRequests to true
    @SuppressWarnings("unused")
    @InvokerRequest(InvokerRequest.Type.CATALOG_REQUEST_PROVISION)
    public void provisionCatalogRequest(CatalogRequest catalogRequest, Object logic) {
        // This is a no-op
    }

    @InvokerRequest(InvokerRequest.Type.INVOKER_LOADED)
    public void onInvokerLoad() {
        ThreadLocal<RequestContext> threadLocal = ThreadLocalProxy.getThreadLocal(RequestContext.class);
        RequestContext liveContext = RequestContextImpl.createModeSpecificContext(requestContext, ExecutionMode.LIVE);
        RequestContext draftContext = RequestContextImpl.createModeSpecificContext(requestContext, ExecutionMode.DRAFT);
        RequestContext testContext = RequestContextImpl.createModeSpecificContext(requestContext, ExecutionMode.TEST);
        threadLocal.set(liveContext);
        OpenApiContext currentSpec = specStore.getSpec();
        if (currentSpec != null) {
            threadLocal.set(draftContext);
            if (specStore.getSpec() == null) {
                specStore.saveSpec(currentSpec);
                threadLocal.set(testContext);
                specStore.saveSpec(currentSpec);
            }
        }
    }

}