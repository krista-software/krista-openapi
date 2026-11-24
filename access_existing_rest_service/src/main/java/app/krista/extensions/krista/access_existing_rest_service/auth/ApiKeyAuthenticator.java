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

import java.util.Objects;
import app.krista.extensions.krista.access_existing_rest_service.Constants;
import app.krista.extensions.krista.access_existing_rest_service.data.RequestInfo;

public class ApiKeyAuthenticator implements Authenticator {

    private final String apiKeyValue;

    public ApiKeyAuthenticator(String apiKeyValue) {
        this.apiKeyValue = apiKeyValue;
    }

    @Override
    public void authenticate(AuthenticationInfo<?> authenticationInfo, RequestInfo.Builder requestInfoBuilder) {
        AuthenticationInfo<ApiKeyInfo> apiKeyAuthenticationInfo = authenticationInfo.castInfo(ApiKeyInfo.class);
        ApiKeyInfo info = apiKeyAuthenticationInfo.info;
        Objects.requireNonNull(apiKeyValue, Constants.API_KEY);
        switch (info.in) {
            case QUERY:
                requestInfoBuilder.queryParameter(info.key, apiKeyValue);
                break;
            case COOKIE:
                requestInfoBuilder.cookie(info.key, apiKeyValue);
                break;
            case HEADER:
                requestInfoBuilder.header(info.key, apiKeyValue);
                break;
            default:
                throw new IllegalStateException("API Key configuration error: The API key location '" + info.in + "' is not supported. API keys can only be placed in 'query', 'header', or 'cookie' parameters. Please check your OpenAPI specification or extension configuration.");
        }
    }

}
