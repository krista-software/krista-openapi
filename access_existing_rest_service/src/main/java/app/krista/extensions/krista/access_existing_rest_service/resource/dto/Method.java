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

package app.krista.extensions.krista.access_existing_rest_service.resource.dto;

public final class Method {

    public final String requestId;
    public final String requestName;
    public final String endpoint;
    public final String httpMethod;
    public final String description;

    public Method(final String requestId, final String requestName, final String endpoint, final String httpMethod,
            final String description) {
        this.requestId = requestId;
        this.requestName = requestName;
        this.endpoint = endpoint;
        this.httpMethod = httpMethod;
        this.description = description;
    }

}
