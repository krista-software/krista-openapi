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

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import java.util.Objects;

public class OpenApiOperation {

    private final PathItem pathItem;
    private final String pathTemplate;
    private final Operation operation;
    private final String inputName;
    private final String outputName;
    private final PathItem.HttpMethod httpMethod;

    public OpenApiOperation(PathItem pathItem, String pathTemplate, Operation operation, String inputName,
            String outputName, PathItem.HttpMethod httpMethod) {
        this.pathItem = pathItem;
        this.pathTemplate = pathTemplate;
        this.operation = operation;
        this.inputName = inputName;
        this.outputName = outputName;
        this.httpMethod = httpMethod;
    }

    public PathItem getPathItem() {
        return pathItem;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getPathTemplate() {
        return pathTemplate;
    }

    public String getInputName() {
        return inputName;
    }

    public String getOutputName() {
        return outputName;
    }

    public PathItem.HttpMethod getHttpMethod() {
        return httpMethod;
    }

    @Override
    public String toString() {
        return "OpenApiOperation{" +
                "path=" + pathItem +
                ", pathTemplate='" + pathTemplate + '\'' +
                ", operation=" + operation +
                ", inputName='" + inputName + '\'' +
                ", outputName='" + outputName + '\'' +
                '}';
    }

    public static OpenApiOperation create(JaxrsRequestItem jaxrsRequestItem) {
        Objects.requireNonNull(jaxrsRequestItem);
        return new OpenApiOperation(jaxrsRequestItem.getPathItem(), jaxrsRequestItem.getPath(),
                jaxrsRequestItem.getOperation(), jaxrsRequestItem.getInputName(), jaxrsRequestItem.getOutputName(),
                jaxrsRequestItem.getHttpMethod());
    }

}
