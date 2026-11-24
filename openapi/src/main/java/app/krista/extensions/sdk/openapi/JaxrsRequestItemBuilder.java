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

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;

@JsonPOJOBuilder(withPrefix = "set")
@SuppressWarnings({"unused", "rawtypes"})
public class JaxrsRequestItemBuilder {

    private String requestId;
    private String path;
    private PathItem pathItem;
    private PathItem.HttpMethod httpMethod;
    private Operation operation;
    private Schema responseSchema;
    private Schema requestSchema;
    private String inputName;
    private String outputName;

    public JaxrsRequestItemBuilder() {
        // Empty constructor for Jackson
    }

    public JaxrsRequestItemBuilder setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public JaxrsRequestItemBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public JaxrsRequestItemBuilder setPathItem(PathItem pathItem) {
        this.pathItem = pathItem;
        return this;
    }

    public JaxrsRequestItemBuilder setHttpMethod(PathItem.HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public void setResponseSchema(Schema responseSchema) {
        this.responseSchema = responseSchema;
    }

    public void setRequestSchema(Schema requestSchema) {
        this.requestSchema = requestSchema;
    }

    public JaxrsRequestItemBuilder setOperation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }

    public JaxrsRequestItem build() {
        JaxrsRequestItem.SpecItems specItems = new JaxrsRequestItem.SpecItems(pathItem, httpMethod, operation, responseSchema, requestSchema);
        return new JaxrsRequestItem(requestId, path, specItems, inputName, outputName);
    }

}