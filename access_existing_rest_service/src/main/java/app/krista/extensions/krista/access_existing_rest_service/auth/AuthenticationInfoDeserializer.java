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

import com.google.gson.*;

public class AuthenticationInfoDeserializer<K> implements JsonDeserializer<AuthenticationInfo<K>> {

    @Override
    public AuthenticationInfo<K> deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) {
            return null;
        }

        JsonObject jsonObject = json.getAsJsonObject();
        String type = extractAuthType(jsonObject);
        JsonElement dataElement = jsonObject.get("info");

        switch (type) {
            case "API_KEY":
                return deserializeToAuthenticationInfo(dataElement, AuthenticationType.API_KEY, context);
            case "BASIC":
                return deserializeToAuthenticationInfo(dataElement, AuthenticationType.BASIC, context);
            default:
                throw new IllegalStateException("Authentication configuration error: The authentication type '" + type + "' is not supported. Please use either 'API_KEY' or 'BASIC' authentication in your extension configuration.");
        }
    }

    private String extractAuthType(JsonObject jsonObject) {
        JsonElement typeElement = jsonObject.get("type");
        if (typeElement == null || typeElement.isJsonNull()) {
            throw new IllegalStateException("Authentication configuration error: No authentication type specified. Please configure either 'API_KEY' or 'BASIC' authentication in your extension setup page.");
        }
        return typeElement.getAsString();
    }

    @SuppressWarnings("unchecked")
    private <T> AuthenticationInfo<T> deserializeToAuthenticationInfo(JsonElement dataElement, AuthenticationType authType, JsonDeserializationContext context) {
        ApiKeyInfo apiKeyInfo = context.deserialize(dataElement, ApiKeyInfo.class);
        return (AuthenticationInfo<T>) new AuthenticationInfo<>(authType, apiKeyInfo);
    }

}
