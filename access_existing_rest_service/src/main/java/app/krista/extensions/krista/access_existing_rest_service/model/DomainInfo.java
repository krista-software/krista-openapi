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

import app.krista.extension.executor.Extension;
import app.krista.extensions.krista.access_existing_rest_service.Constants;

public final class DomainInfo {

    private final String ecosystemId;
    private final String ecosystemName;
    private final String version;
    private final String domainId;
    private final String domainName;
    private final String catalogId;

    public DomainInfo(String ecosystemId, String ecosystemName, String version, String domainId, String domainName,
            String catalogId) {
        this.ecosystemId = ecosystemId;
        this.ecosystemName = ecosystemName;
        this.version = version;
        this.domainId = domainId;
        this.domainName = domainName;
        this.catalogId = catalogId;
    }

    public String getEcosystemId() {
        return ecosystemId;
    }

    public String getEcosystemName() {
        return ecosystemName;
    }

    public String getVersion() {
        return version;
    }

    public String getDomainId() {
        return domainId;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public Extension.Domain toExtensionDomain() {
        return new Extension.Domain(ecosystemId, ecosystemName, version, domainId, domainName);
    }

    public String getEntityFieldType(String entityId) {
        return String.format("com.krista.fields.entity.%s.%s.%s.%s", ecosystemId, domainId, entityId,
                Constants.EXTENSION_DESCRIPTION_ID);
    }

}
