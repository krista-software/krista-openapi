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

public class RequestInfo {

    private final JaxrsRequestItem jaxrsRequestItem;
    private final CatalogRequestInfo catalogRequestInfo;
    private final Type type;

    public RequestInfo(JaxrsRequestItem jaxrsRequestItem, CatalogRequestInfo catalogRequestInfo, Type type) {
        this.jaxrsRequestItem = jaxrsRequestItem;
        this.catalogRequestInfo = catalogRequestInfo;
        this.type = type;
    }

    public JaxrsRequestItem getJaxrsRequestItem() {
        return jaxrsRequestItem;
    }

    public CatalogRequestInfo getCatalogRequestInfo() {
        return catalogRequestInfo;
    }

    public Type getType() {
        return type;
    }

    public boolean isCatalogRequest() {
        return type == Type.CATALOG;
    }

    public enum Type {
        CATALOG, ENTITY
    }

}
