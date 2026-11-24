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

package app.krista.extensions.krista.access_existing_rest_service.auth;

import java.util.LinkedList;
import java.util.List;
import app.krista.extensions.krista.access_existing_rest_service.error.KnownException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;

public final class AuthenticationInfoFactory {

    private AuthenticationInfoFactory() {
    }

    @SuppressWarnings("unchecked")
    public static <T> List<AuthenticationInfo<T>> create(OpenAPI openAPI) {
        List<AuthenticationInfo<T>> authenticationInfos = new LinkedList<>();
        if (openAPI.getComponents() != null && openAPI.getComponents().getSecuritySchemes() != null &&
                !openAPI.getComponents().getSecuritySchemes().isEmpty()) {
            for (SecurityScheme securityScheme : openAPI.getComponents().getSecuritySchemes().values()) {
                if (SecurityScheme.Type.APIKEY.equals(securityScheme.getType())) {
                    SecurityScheme.In in = securityScheme.getIn();
                    ApiKeyInfo apiKeyInfo = new ApiKeyInfo(securityScheme.getName(), in);
                    authenticationInfos.add(
                            (AuthenticationInfo<T>) new AuthenticationInfo<>(AuthenticationType.API_KEY, apiKeyInfo));
                } else if (SecurityScheme.Type.HTTP.equals(securityScheme.getType()) &&
                        "basic".equals(securityScheme.getScheme())) {
                    authenticationInfos.add(new AuthenticationInfo<>(AuthenticationType.BASIC, null));
                }
            }
            if (authenticationInfos.isEmpty()) {
                throw new KnownException("Only Basic and API Key Authentication is supported");
            }
            return authenticationInfos;
        }
        return List.of();
    }

}
