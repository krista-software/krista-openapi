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

package app.krista.extensions.krista.access_existing_rest_service.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import app.krista.extensions.krista.access_existing_rest_service.auth.AuthenticationInfo;

public class OpenApiContext {

    public static final String STORE_KEY = "OpenApiContext";
    private final Map<String, RequestInfo> requestInfoMap;
    private final SpecInfo specInfo;
    private final List<AuthenticationInfo<Object>> authenticationInfoList;
    private final DomainInfo domainInfo;
    private final List<EntityItem> entities;
    private final EntityRequests entityRequests;

    public OpenApiContext(Map<String, RequestInfo> requestInfoMap, SpecInfo specInfo,
            List<AuthenticationInfo<Object>> authenticationInfoList,
            DomainInfo domainInfo, List<EntityItem> entities, EntityRequests entityRequests) {
        this.requestInfoMap = requestInfoMap;
        this.specInfo = specInfo;
        this.authenticationInfoList = authenticationInfoList;
        this.domainInfo = domainInfo;
        this.entities = entities;
        this.entityRequests = entityRequests;
    }

    public SpecInfo getSpecInfo() {
        return specInfo;
    }

    public Set<String> getRequestIds() {
        return requestInfoMap.keySet();
    }

    public Map<String, RequestInfo> getRequestInfoMap() {
        return requestInfoMap;
    }

    public List<AuthenticationInfo<Object>> getAuthenticationInfoList() {
        return authenticationInfoList;
    }

    public DomainInfo getDomainInfo() {
        return domainInfo;
    }

    public List<EntityItem> getEntities() {
        return entities;
    }

    public EntityRequests getEntityRequests() {
        return entityRequests;
    }

}
