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

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import app.krista.extension.executor.Extension;
import app.krista.extension.executor.ExtensionApiRequest;
import app.krista.extension.executor.ExtensionCatalogRequest;
import app.krista.extension.executor.ExtensionEntityDefinition;
import app.krista.extensions.krista.access_existing_rest_service.model.DomainInfo;
import app.krista.model.entity.EntityDefinition;
import app.krista.model.entity.EntityType;
import app.krista.model.field.FieldDefinition;
import app.krista.model.field.NamedField;

import static app.krista.extensions.krista.access_existing_rest_service.Constants.*;

@SuppressWarnings("unused")
public class FakeExtension implements Extension {

    @Override
    public List<Domain> getImplementedDomains() {
        DomainInfo defaultDomainInfo = DEFAULT_DOMAIN_INFO;
        return List.of(
                new Domain(defaultDomainInfo.getEcosystemId(), defaultDomainInfo.getEcosystemName(),
                        defaultDomainInfo.getVersion(), defaultDomainInfo.getDomainId(),
                        defaultDomainInfo.getDomainName())
        );
    }

    @Override
    public List<Domain> getReferencedDomains() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("deprecation")
    public InputStream getResource(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, NamedField> getAttributeFields() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Set<String>> getAuthorizationMethods() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, ? extends ExtensionCatalogRequest> getCatalogRequests() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExtensionEntityDefinition getEntityDefinition(EntityType entityType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, ? extends ExtensionApiRequest> getApiRequests() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Dependency> getDependencies() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<? extends EntityDefinition> getEntityDefinitions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<FieldDefinition> getFieldDefinitions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getExtensionId() {
        return "fake-extension-id";
    }

    @Override
    public Map<String, ? extends app.krista.extension.executor.ExtensionSubCatalogRequest> getSubCatalogRequests() {
        return Map.of();
    }

}
