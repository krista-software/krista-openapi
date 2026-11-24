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

package app.krista.extensions.sdk.openapi.krista.impl;

import app.krista.extensions.sdk.openapi.OpenApiData;
import app.krista.extensions.sdk.openapi.OpenApiInputValue;
import app.krista.model.field.NamedField;
import com.google.gson.Gson;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KristaOpenApiFieldMapper {

    private final Gson gson;

    @Inject
    public KristaOpenApiFieldMapper(Gson gson) {
        this.gson = gson;
    }

    public OpenApiData fromFields(List<NamedField> fieldTypes, Map<String, Object> fieldValues) {
        Map<String, Object> result = new HashMap<>();
        for (NamedField namedField : fieldTypes) {
            result.put(namedField.getName(), toValue(fieldValues.get(namedField.getName())));
        }
        return new OpenApiInputValue(result);
    }

    private Object toValue(Object fieldValue) {
        return fieldValue;
    }


    public Map<String, Object> toFields(OpenApiData openApiValue) {
        String value = (String) openApiValue.getValue();
        @SuppressWarnings("unchecked")
        Map<String, Object> map = gson.fromJson(value, Map.class);
        return map;
    }

}
