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

package app.krista.extensions.krista.access_existing_rest_service.fake;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import app.krista.extension.authorization.AuthorizationException;
import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.event.EventSubscription;
import app.krista.extension.event.WaitForEventListener;
import app.krista.extension.executor.*;
import app.krista.extension.request.CatalogRequest;
import app.krista.extension.request.ProtoRequest;
import app.krista.extension.request.ProtoResponse;
import app.krista.extension.request.RoutingInfo;
import app.krista.model.entity.EntityDefinition;
import app.krista.model.entity.EntityType;
import org.jvnet.hk2.annotations.ContractsProvided;
import org.jvnet.hk2.annotations.Service;

@Service
@ContractsProvided(Invoker.class)
@SuppressWarnings("deprecation")
public class FakeInvoker implements Invoker {

    private final Map<String, Object> attributes = new HashMap<>();

    @Override
    public String getInvokerId() {
        return  "INV_fake invoker id";
    }

    @Override
    public String getInvokerName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getWorkspaceId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Extension getExtension() {
        return new FakeExtension();
    }

    @Override
    public boolean isRemoved() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RoutingInfo getRoutingInfo() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<ProtoResponse> authorize(ProtoRequest request) {
        return Invoker.super.authorize(request);
    }

    @Override
    public RequestAuthenticator getRequestAuthenticator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Map<String, String> getCustomTabs() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Void> validateAttributes(final Map<String, Object> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Void> testConnection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, ? extends ExtensionCatalogRequest> getCatalogRequests() {
        return Invoker.super.getCatalogRequests();
    }

    @Override
    public void provisionCatalogRequest(CatalogRequest catalogRequest, Object logic) throws IOException {
        Invoker.super.provisionCatalogRequest(catalogRequest, logic);
    }

    @Override
    public boolean supportsCatalogRequestProvisioning() {
        return Invoker.super.supportsCatalogRequestProvisioning();
    }

    @Override
    public CompletableFuture<Map<String, Object>> handleCatalogRequest(final String s, final boolean b,
            final Map<String, Object> map) throws AuthorizationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasRuntimeDefinedCatalogRequests() {
        throw new UnsupportedOperationException();
    }

    @Override
    public InvokerEntityStore getEntityStore(final EntityType entityType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<ProtoResponse> handleApiRequest(final String s, final ProtoRequest protoRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasRuntimeDefinedApiRequests() {
        throw new UnsupportedOperationException();
    }

    @Override
    public InvokerEventHandler getEventHandler() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getBindings() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Void> registerEventListener(final WaitForEventListener waitForEventListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Void> unregisterEventListener(final String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<WaitForEventListener> listEventListeners() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<EventSubscription> listEventSubscriptions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deliverEvent(final Map<String, Object> map, final String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<EntityType> getEntityTypes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExtensionEntityDefinition getEntityDefinition(final EntityType entityType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<EntityType, ExtensionEntityDefinition> getEntityDefinitions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void provisionEntity(EntityDefinition entityDefinition) throws IOException {
        Invoker.super.provisionEntity(entityDefinition);
    }

    @Override
    public boolean supportsEntityProvisioning() {
        return Invoker.super.supportsEntityProvisioning();
    }

    @Override
    public CompletableFuture<Void> prepareChangeRoutingId(String newRoutingId) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<app.krista.extension.executor.ExtensionResponse> handleCatalogRequestV2(String catalogRequestName, boolean isTest, Map<String, Object> params) {
        throw new UnsupportedOperationException();
    }

}