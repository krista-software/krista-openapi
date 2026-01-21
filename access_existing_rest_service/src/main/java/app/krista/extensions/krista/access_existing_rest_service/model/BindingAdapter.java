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

import java.lang.reflect.Type;
import com.google.gson.*;

public class BindingAdapter implements JsonDeserializer<Binding>, JsonSerializer<Binding> {

    @Override
    public Binding deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
            JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        if (type.equals("WRAPPER")) {
            return context.deserialize(json, Binding.WrapperBinding.class);
        } else if (type.equals("DIRECT")) {
            return context.deserialize(json, Binding.DirectBinding.class);
        } else {
            throw new com.google.gson.JsonParseException("Unknown type: " + type);
        }
    }

    @Override
    public JsonElement serialize(Binding binding, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(binding);
    }

}
