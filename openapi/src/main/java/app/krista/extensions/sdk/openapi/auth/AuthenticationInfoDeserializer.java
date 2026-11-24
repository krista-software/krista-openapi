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

package app.krista.extensions.sdk.openapi.auth;

import com.google.gson.*;

public class AuthenticationInfoDeserializer<K> implements JsonDeserializer<AuthenticationInfo<K>> {

    @Override
    public AuthenticationInfo<K> deserialize(JsonElement json, java.lang.reflect.Type
            typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) {
            return null;
        }
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement typeElement = jsonObject.get("type");
        JsonElement dataElement = jsonObject.get("info");
        if (typeElement == null || typeElement.isJsonNull()) {
            throw new IllegalStateException("AuthenticationInfo type is not defined");
        }

        String type = typeElement.getAsString();
        if (type.equals(AuthenticationType.API_KEY.name())) {
            ApiKeyInfo apiKeyInfo =
                    context.deserialize(dataElement, ApiKeyInfo.class);
            @SuppressWarnings("unchecked")
            AuthenticationInfo<K> kAuthenticationInfo = (AuthenticationInfo<K>) new AuthenticationInfo<>(
                    AuthenticationType.API_KEY, apiKeyInfo);
            return kAuthenticationInfo;
        } else {
            throw new IllegalStateException("Unsupported operation specified");
        }
    }

}
