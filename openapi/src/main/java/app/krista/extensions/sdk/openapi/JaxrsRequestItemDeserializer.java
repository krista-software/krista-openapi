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

package app.krista.extensions.sdk.openapi;

import app.krista.extensions.sdk.openapi.util.GsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import io.swagger.v3.core.util.Json;

import java.lang.reflect.Type;

public class JaxrsRequestItemDeserializer implements JsonDeserializer<JaxrsRequestItem>, JsonSerializer<JaxrsRequestItem> {
    private static final ObjectMapper objectMapper = Json.mapper().copy();
    private static final Gson gson = GsonFactory.gson;

    @Override
    public JaxrsRequestItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return objectMapper.readValue(gson.toJson(json), JaxrsRequestItem.class);
        } catch (JsonProcessingException cause) {
            throw new JsonParseException(cause);
        }
    }

    @Override
    public JsonElement serialize(JaxrsRequestItem src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            String s = objectMapper.writeValueAsString(src);
            return gson.fromJson(s, JsonElement.class);
        } catch (JsonProcessingException cause) {
            throw new IllegalStateException(cause);
        }

    }
}