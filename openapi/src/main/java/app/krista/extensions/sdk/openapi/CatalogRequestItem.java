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

import app.krista.extension.request.CatalogRequest;
import app.krista.model.field.NamedField;

import java.util.List;

public class CatalogRequestItem {

    private final String requestId;
    private final List<NamedField> inputFields;

    private final List<NamedField> outputFields;

    private final CatalogRequest.Type type;
    private final String requestName;
    private final String description;


    public CatalogRequestItem(String requestId, List<NamedField> inputFields, List<NamedField> outputFields, CatalogRequest.Type type, String requestName, String description) {
        this.requestId = requestId;
        this.inputFields = inputFields;
        this.outputFields = outputFields;
        this.type = type;
        this.requestName = requestName;
        this.description = description;
    }

    public String getRequestId() {
        return requestId;
    }

    public List<NamedField> getInputFields() {
        return inputFields;
    }

    public List<NamedField> getOutputFields() {
        return outputFields;
    }

    public CatalogRequest.Type getType() {
        return type;
    }

    public String getRequestName() {
        return requestName;
    }

    public String getDescription() {
        return description;
    }
}
