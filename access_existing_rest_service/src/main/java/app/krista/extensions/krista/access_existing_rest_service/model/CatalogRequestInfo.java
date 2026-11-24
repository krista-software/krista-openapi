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

import app.krista.extension.request.CatalogRequest;
import app.krista.model.field.NamedField;
import java.util.List;

public class CatalogRequestInfo {

    private final String requestId;
    private final String areaName;
    private final String requestName;
    private final String description;
    private final CatalogRequest.Type type;
    private final List<NamedField> inputFields;
    private final List<NamedField> outputFields;

    public CatalogRequestInfo(String requestId, String areaName, String requestName, String description,
            CatalogRequest.Type type, List<NamedField> inputFields, List<NamedField> outputFields) {
        this.requestId = requestId;
        this.areaName = areaName;
        this.requestName = requestName;
        this.description = description;
        this.type = type;
        this.inputFields = inputFields;
        this.outputFields = outputFields;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getRequestName() {
        return requestName;
    }

    public String getDescription() {
        return description;
    }

    public CatalogRequest.Type getType() {
        return type;
    }

    public List<NamedField> getInputFields() {
        return inputFields;
    }

    public List<NamedField> getOutputFields() {
        return outputFields;
    }

}
