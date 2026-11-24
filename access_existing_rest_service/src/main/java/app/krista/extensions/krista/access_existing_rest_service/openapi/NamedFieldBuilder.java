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

package app.krista.extensions.krista.access_existing_rest_service.openapi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import app.krista.model.field.NamedField;
import app.krista.model.field.NamedValuedField;

public class NamedFieldBuilder {

    private final Map<String, Object> attributeValues = new HashMap<>();
    private final Map<String, NamedValuedField> options = new HashMap<>();
    private String name;
    private String fieldType;

    public NamedFieldBuilder name(String name) {
        this.name = name;
        return this;
    }

    public NamedFieldBuilder fieldType(String fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public NamedFieldBuilder attribute(String name, Object value) {
        if (value == null) {
            this.attributeValues.remove(name);
            return this;
        }
        this.attributeValues.put(name, value);
        return this;
    }

    public NamedFieldBuilder required(boolean required) {
        NamedValuedField namedValuedField = new NamedValuedField("required", "Boolean", required, Map.of(), Map.of());
        return option("required", namedValuedField);
    }

    public NamedFieldBuilder option(String name, NamedValuedField option) {
        this.options.put(name, option);
        return this;
    }

    public NamedField toNamedField() {
        return new NamedField(name, fieldType, Collections.unmodifiableMap(attributeValues),
                Collections.unmodifiableMap(options));
    }

}
