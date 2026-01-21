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

package app.krista.extensions.krista.access_existing_rest_service.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import app.krista.extension.executor.Invoker;
import app.krista.extension.executor.impl.DefaultExtensionEntityDefinition;
import app.krista.extensions.krista.access_existing_rest_service.model.DomainInfo;
import app.krista.extensions.krista.access_existing_rest_service.model.EntityInfo;
import app.krista.extensions.krista.access_existing_rest_service.model.EntityItem;
import app.krista.extensions.krista.access_existing_rest_service.model.OpenApiContext;
import app.krista.extensions.krista.access_existing_rest_service.openapi.SpecStore;
import app.krista.model.entity.EntityType;
import app.krista.model.entity.impl.CatalogRegistryReference;
import app.krista.model.entity.impl.DomainRegistryReference;
import app.krista.model.entity.impl.InvokerRegistryReference;
import org.jvnet.hk2.annotations.Service;

@Service
public class EntityRequestsManager {

    private final SpecStore specStore;
    private final Invoker invoker;

    @Inject
    public EntityRequestsManager(SpecStore specStore, Invoker invoker) {
        this.specStore = specStore;
        this.invoker = invoker;
    }

    public List<DefaultExtensionEntityDefinition<?>> getEntityDefinitions() {
        OpenApiContext openApiContext = specStore.getSpec();
        if (openApiContext == null) {
            return List.of();
        }
        DomainInfo domainInfo = openApiContext.getDomainInfo();
        List<DefaultExtensionEntityDefinition<?>> result = new ArrayList<>();
        for (EntityItem entityItem : openApiContext.getEntities()) {
            EntityInfo entityInfo = entityItem.getEntityInfo();
            EntityType entityType = new EntityType(entityInfo.getTitle(), entityInfo.getEntityId(),
                    new RegistryReferenceImpl(domainInfo.getCatalogId(),
                            domainInfo.getDomainId(), invoker.getInvokerId(), domainInfo.getEcosystemId()));
            result.add(new DefaultExtensionEntityDefinition<>(invoker.getExtension(),
                    entityType,
                    entityInfo.getPrimaryKey(),
                    Set.of(),
                    "extension",
                    Map.of(),
                    entityItem.getFieldMap(),
                    domainInfo.toExtensionDomain()
            ));
        }
        return result;
    }

    public static class RegistryReferenceImpl
            implements InvokerRegistryReference, DomainRegistryReference, EntityType.RegistryReference,
            CatalogRegistryReference {

        private final String catalogId;
        private final String domainId;
        private final String invokerId;
        private final String ecosystemId;

        public RegistryReferenceImpl(String catalogId, String domainId, String invokerId,
                String ecosystemId) {
            this.catalogId = catalogId;
            this.domainId = domainId;
            this.invokerId = invokerId;
            this.ecosystemId = ecosystemId;
        }

        @Override
        public String getCatalogId() {
            return catalogId;
        }

        @Override
        public String getEcosystemId() {
            return ecosystemId;
        }

        @Override
        public String getDomainId() {
            return domainId;
        }

        @Override
        public String getInvokerId() {
            return invokerId;
        }

    }

}
