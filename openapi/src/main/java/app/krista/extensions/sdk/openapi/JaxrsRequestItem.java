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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.annotations.JsonAdapter;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;

@JsonAdapter(JaxrsRequestItemDeserializer.class)
@JsonDeserialize(builder = JaxrsRequestItemBuilder.class)
@SuppressWarnings("rawtypes")
public class JaxrsRequestItem {

    private final String requestId;
    private final String path;
    private final SpecItems specItems;
    private final String inputName;
    private final String outputName;

    public JaxrsRequestItem(String requestId, String path, SpecItems specItems, String inputName, String outputName) {
        this.requestId = requestId;
        this.path = path;
        this.specItems = specItems;
        this.inputName = inputName;
        this.outputName = outputName;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getPath() {
        return path;
    }

    public PathItem.HttpMethod getHttpMethod() {
        return specItems.getHttpMethod();
    }

    public Operation getOperation() {
        return specItems.getOperation();
    }

    public PathItem getPathItem() {
        return specItems.getPathItem();
    }

    public Schema getResponseSchema() {
        return specItems.getResponseSchema();
    }

    public Schema getRequestSchema() {
        return specItems.getRequestSchema();
    }

    public String getInputName() {
        return inputName;
    }

    public String getOutputName() {
        return outputName;
    }


    public static class SpecItems {

        private final PathItem pathItem;
        private final PathItem.HttpMethod httpMethod;
        private final Operation operation;
        private final Schema responseSchema;
        private final Schema requestSchema;

        public SpecItems(PathItem pathItem, PathItem.HttpMethod httpMethod, Operation operation, Schema responseSchema, Schema requestSchema) {
            this.pathItem = pathItem;
            this.httpMethod = httpMethod;
            this.operation = operation;
            this.responseSchema = responseSchema;
            this.requestSchema = requestSchema;
        }

        public PathItem getPathItem() {
            return pathItem;
        }

        public PathItem.HttpMethod getHttpMethod() {
            return httpMethod;
        }

        public Operation getOperation() {
            return operation;
        }

        public Schema getResponseSchema() {
            return responseSchema;
        }

        public Schema getRequestSchema() {
            return requestSchema;
        }
    }

}