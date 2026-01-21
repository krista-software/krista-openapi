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

package app.krista.extensions.krista.access_existing_rest_service.json;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.core.util.Json;

public class JacksonTypeAdapter implements TypeAdapterFactory {

    private static final ObjectMapper objectMapper = Json.mapper().copy();

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType().getName().startsWith("io.swagger.v3.oas.models") && !type.getRawType().isEnum()) {
            return new TypeAdapter<>() {
                @Override
                public void write(JsonWriter out, Object value) throws IOException {
                    out.value(objectMapper.writeValueAsString(value));
                }

                @Override
                public T read(JsonReader in) throws IOException {
                    return (T) objectMapper.readValue(in.nextString(), type.getRawType());
                }
            };
        }
        return null;
    }

}
