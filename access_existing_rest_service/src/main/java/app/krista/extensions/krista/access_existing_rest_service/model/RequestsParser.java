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

import java.util.*;
import java.util.stream.Collectors;
import app.krista.extension.request.CatalogRequest;
import app.krista.extensions.krista.access_existing_rest_service.Constants;
import app.krista.extensions.krista.access_existing_rest_service.data.Pair;
import app.krista.extensions.krista.access_existing_rest_service.openapi.NamedFieldBuilder;
import app.krista.model.field.NamedField;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static app.krista.extensions.krista.access_existing_rest_service.Constants.*;

public class RequestsParser {

    private final Logger LOGGER = LoggerFactory.getLogger(RequestsParser.class);

    private final DomainInfo domainInfo;

    public RequestsParser(DomainInfo domainInfo) {
        this.domainInfo = domainInfo;
    }

    private CatalogRequest.Type guessCatalogRequestType(Operation operation, PathItem.HttpMethod method) {
        if (operation.getExtensions() != null) {
            Object type = operation.getExtensions().get(Constants.X_KRISTA_REQUEST_TYPE);
            if (type != null) {
                return CatalogRequest.Type.valueOf(type.toString());
            }
        }
        if (method == PathItem.HttpMethod.GET) {
            return CatalogRequest.Type.QUERY_SYSTEM;
        }
        return CatalogRequest.Type.CHANGE_SYSTEM;
    }

    private String getRequestId(Operation operation) {
        Map<String, Object> extensions = operation.getExtensions();
        if (extensions != null) {
            Object requestId = extensions.get(Constants.X_KRISTA_REQUEST_ID);
            if (requestId != null) {
                return requestId.toString();
            }
        }
        return "localDomainRequest_" + UUID.randomUUID();
    }

    private String guessRequestName(Operation operation) {
        String requestName = operation.getSummary();
        if (requestName == null) {
            requestName = operation.getOperationId();
        }
        if (requestName == null) {
            requestName = operation.getDescription();
        }
        return requestName;
    }

    private String guessDescription(Operation operation) {
        String description = operation.getDescription();
        if (description == null) {
            description = guessRequestName(operation);
        }
        return description;
    }

    private void addParameters(List<Parameter> parameters, List<NamedField> inputFields) {
        parameters = Objects.requireNonNullElse(parameters, List.of());
        for (Parameter parameter : parameters) {
            if (parameter.getSchema().getEnum() != null && parameter.getSchema().getEnum().size() == 1) {
                // will be handled by default parameter
                continue;
            }
            inputFields.add(namedFieldFromSchema(parameter.getSchema(), parameter.getName(), null).toNamedField());
        }
    }

    private ApiResponse findApiResponse(Operation operation) {
        ApiResponse apiResponse = null;
        for (String vr : List.of("200", "2XX", "201", "default")) {
            apiResponse = operation.getResponses().get(vr);
            if (apiResponse != null) {
                break;
            }
        }
        return apiResponse;
    }

    private boolean supportContentType(String mediaTypeName) {
        return mediaTypeName.equals(APPLICATION_JSON) || isFileType(mediaTypeName) ||
                mediaTypeName.equals(APPLICATION_FORM_URLENCODED) || mediaTypeName.equals(TEXT_PLAIN);
    }

    @SuppressWarnings({"rawtypes"})
    private NamedFieldBuilder objectField(Schema<?> schema, String name, String mediaTypeName) {
        if (schema.getProperties() == null) {
            return new NamedFieldBuilder().name(name).fieldType(Constants.UNKNOWN_ENTITY_LOGICAL_NAME);
        }
        List<NamedField> fields = new ArrayList<>();
        for (Map.Entry<String, Schema> entry : (schema.getProperties()).entrySet()) {
            NamedFieldBuilder namedFieldBuilder =
                    namedFieldFromSchema(entry.getValue(), entry.getKey(), mediaTypeName);
            namedFieldBuilder.required(schema.getRequired() != null && schema.getRequired().contains(entry.getKey()));
            fields.add(namedFieldBuilder.toNamedField());
        }
        NamedFieldBuilder result = new NamedFieldBuilder();
        result.name(name);
        result.fieldType(MULTI_FIELD_LOGICAL_NAME);
        result.attribute("fields", fields);
        return result;
    }

    private NamedFieldBuilder arrayField(Schema<?> schema, String name, String mediaTypeName) {
        Schema<?> items = schema.getItems();
        Objects.requireNonNull(items);
        if (items.getType() == null) {
            items.setType(Constants.OBJECT);
        }
        NamedField namedField = namedFieldFromSchema(items, "", mediaTypeName).toNamedField();
        NamedFieldBuilder result = new NamedFieldBuilder();
        result.name(name);
        result.fieldType(KRISTA_LIST_FIELD);
        Map<String, Object> fieldAttributes = Map.of(
                "fieldType", namedField.getFieldType(),
                "attributes", namedField.getAttributeValues()
        );
        result.attribute("field", fieldAttributes);
        return result;
    }

    private NamedFieldBuilder stringField(Schema<?> schema, String name, String mediaTypeName) {
        NamedFieldBuilder result = new NamedFieldBuilder();
        if (isBinary(schema) || isFileType(mediaTypeName)) {
            List<String> fileTypes = getFileTypesForMediaType(mediaTypeName);
            result.name(name == null ? "file" : name);
            result.fieldType(KRISTA_FILE_FIELD);
            result.attribute("fileTypes", fileTypes);
        } else if (schema.getEnum() != null && !schema.getEnum().isEmpty()) {
            result.name(name);
            result.fieldType(KRISTA_PICKONE_FIELD);
            result.attribute("options", schema.getEnum());
        } else {
            result.name(name);
            result.fieldType(KRISTA_TEXT_FIELD);
        }
        return result;
    }

    private boolean isBinary(Schema<?> schema) {
        return schema.getFormat() != null && schema.getFormat().equalsIgnoreCase("binary");
    }

    private NamedFieldBuilder numberField(String name) {
        NamedFieldBuilder result = new NamedFieldBuilder();
        result.name(name);
        result.fieldType(KRISTA_NUMBER_FIELD);
        return result;
    }

    private NamedFieldBuilder handleGenericSchema(Schema<?> schema, String name, String mediaTypeName) {
        NamedFieldBuilder result;
        switch (schema.getType()) {
            case "array":
                result = arrayField(schema, name, mediaTypeName);
                break;
            case Constants.OBJECT:
                result = objectField(schema, name, mediaTypeName);
                break;
            case "integer":
            case "number":
                result = numberField(name);
                break;
            case "string":
                result = stringField(schema, name, mediaTypeName);
                break;
            case "boolean":
                result = booleanField(name);
                break;
            default:
                throw new IllegalArgumentException("Unknown Type: " + schema.getType());
        }
        return result;
    }

    private NamedFieldBuilder booleanField(String name) {
        NamedFieldBuilder result = new NamedFieldBuilder();
        result.name(name);
        result.fieldType(KRISTA_SWITCH_FIELD);
        return result;
    }

    private List<String> getFileTypesForMediaType(String mediaTypeName) {
        List<String> fileTypes = new LinkedList<>();
        if (APPLICATION_PDF.equals(mediaTypeName)) {
            fileTypes.add(DOCUMENT);
        } else if (MULTIPART_FORM_DATA.equals(mediaTypeName) ||
                APPLICATION_OCTET_STREAM.equals(mediaTypeName)) {
            fileTypes.addAll(Arrays.asList(DOCUMENT, IMAGE_GALLERY, CAMERA, CONTACT, AUDIO));
        } else if (IMAGE_JPEG.equals(mediaTypeName) || IMAGE_PNG.equals(mediaTypeName) || IMAGE_GIF.equals(
                mediaTypeName)) {
            fileTypes.addAll(Arrays.asList(IMAGE_GALLERY, CAMERA));
        } else if (AUDIO_MPEG.equals(mediaTypeName) || AUDIO_WAV.equals(mediaTypeName)) {
            fileTypes.add(AUDIO);
        } else if (VIDEO_AVI.equals(mediaTypeName) || VIDEO_MP4.equals(mediaTypeName)) {
            fileTypes.addAll(Arrays.asList(AUDIO, CAMERA));
        }
        return fileTypes;
    }

    private String getAreaName(PathItem pathItem) {
        if (pathItem.getExtensions() != null) {
            Object areaName = pathItem.getExtensions().get(Constants.X_KRISTA_AREA_NAME);
            if (areaName != null) {
                return areaName.toString();
            }
        }
        return DEFAULT_AREA_NAME;
    }

    private boolean isFileType(String mediaTypeName) {
        return APPLICATION_PDF.equals(mediaTypeName)
                || MULTIPART_FORM_DATA.equals(mediaTypeName)
                || IMAGE_JPEG.equals(mediaTypeName)
                || IMAGE_PNG.equals(mediaTypeName)
                || IMAGE_GIF.equals(mediaTypeName)
                || AUDIO_MPEG.equals(mediaTypeName)
                || AUDIO_WAV.equals(mediaTypeName)
                || APPLICATION_XML.equals(mediaTypeName)
                || APPLICATION_OCTET_STREAM.equals(mediaTypeName)
                || VIDEO_MP4.equals(mediaTypeName)
                || VIDEO_AVI.equals(mediaTypeName);
    }

    private String getDefaultName(boolean isOutput) {
        return isOutput ? "Output" : "Input";
    }

    public Map<String, RequestInfo> getRequests(Paths paths) {
        Map<String, RequestInfo> result = new HashMap<>();
        for (Map.Entry<String, PathItem> path : paths.entrySet()) {
            PathItem pathItem = path.getValue();
            for (Map.Entry<PathItem.HttpMethod, Operation> entry : pathItem.readOperationsMap().entrySet()) {
                RequestInfo.Type requestType = pathItem.getExtensions() != null &&
                        pathItem.getExtensions().containsKey(X_KRISTA_ENTITY_REQUEST) ?
                        RequestInfo.Type.ENTITY : RequestInfo.Type.CATALOG;
                try {
                    Pair<JaxrsRequestItem, CatalogRequestInfo> requestItems =
                            getRequestItems(path, pathItem, entry.getValue(), entry.getKey());
                    if (requestItems == null) {
                        continue;
                    }
                    result.put(requestItems.getKey().getRequestId(), new RequestInfo(requestItems.getKey(),
                            requestItems.getValue(), requestType));

                } catch (RuntimeException cause) {
                    LOGGER.error("Could not parse {}:{} due to uncaught runtime error.", path.getKey(),
                            entry.getKey(), cause);
                }
            }
        }
        return result;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public NamedFieldBuilder namedFieldFromSchema(Schema schema, String name, String mediaTypeName) {
        if (schema == null || schema.getType() == null) {
            return new NamedFieldBuilder().name(name).fieldType(Constants.UNKNOWN_ENTITY_LOGICAL_NAME);
        }
        if (schema.getExtensions() != null) {
            Object o = schema.getExtensions().get(Constants.X_KRISTA_FIELD);
            if (o != null) {
                return handleKristaSchema(schema, name, mediaTypeName, (Map<String, Object>) o);
            }
        }
        return handleGenericSchema(schema, name, mediaTypeName);
    }

    // FIXME:- mediaTypeName does not belong here
    @SuppressWarnings({"unchecked"})
    private NamedFieldBuilder handleKristaSchema(Schema<?> schema, String name, String mediaTypeName,
            Map<String, Object> tyeInfo) {
        String logicalNameSuffix = (String) tyeInfo.get("type");
        String logicalName = "com.krista.fields." + logicalNameSuffix;
        NamedFieldBuilder result;
        switch (logicalName) {
            case Constants.TEXT_FIELD_LOGICAL_NAME:
            case Constants.PARAGRAPH_LOGICAL_NAME:
            case Constants.PERSON_LOGICAL_NAME:
            case Constants.PROPER_NOUN_LOGICAL_NAME:
            case Constants.IDENTIFIER_LOGICAL_NAME:
            case Constants.RICH_TEXT_LOGICAL_NAME:
            case Constants.LABEL_LOGICAL_NAME:
            case Constants.STREET_LOGICAL_NAME:
            case Constants.STREET2_LOGICAL_NAME:
            case Constants.CITY_LOGICAL_NAME:
            case Constants.STATE_LOGICAL_NAME:
            case Constants.POSTAL_CODE_LOGICAL_NAME:
            case Constants.COUNTRY_LOGICAL_NAME:
            case Constants.EMAIL_LOGICAL_NAME: {
                result = stringField(schema, name, mediaTypeName)
                        .fieldType(logicalName)
                        .attribute("maxLength", tyeInfo.get("maxLength"))
                        .attribute("minLength", tyeInfo.get("minLength"));
                break;
            }
            case Constants.PICK_ONE_LOGICAL_NAME: {
                result = stringField(schema, name, mediaTypeName);
                break;
            }
            case Constants.NUMBER_LOGICAL_NAME:
            case Constants.UNIT_LOGICAL_NAME:
            case Constants.CURRENCY_LOGICAL_NAME:
            case Constants.PERCENTAGE_LOGICAL_NAME: {
                result = numberField(name)
                        .fieldType(logicalName)
                        .attribute("maxValue", tyeInfo.get("maxValue"))
                        .attribute("minValue", tyeInfo.get("minValue"));
                break;
            }
            case Constants.SWITCH_LOGICAL_NAME: {
                result = booleanField(name)
                        .fieldType(logicalName);
                break;
            }
            case Constants.CHECKBOXES_LOGICAL_NAME: {
                result = stringField(schema, name, mediaTypeName)
                        .fieldType(logicalName);
                break;
            }
            case Constants.PHONE_LOGICAL_NAME:
            case Constants.DATE_RANGE_LOGICAL_NAME:
            case Constants.DATE_TIME_RANGE_LOGICAL_NAME: {
                result = new NamedFieldBuilder()
                        .name(name)
                        .fieldType(logicalName);
                break;
            }
            case Constants.DATE_LOGICAL_NAME:
            case Constants.TIME_LOGICAL_NAME:
            case Constants.RATING_LOGICAL_NAME: {
                result = numberField(name)
                        .fieldType(logicalName);
                break;
            }
            case Constants.UNKNOWN_ENTITY_LOGICAL_NAME:
            case Constants.FILE_LOGICAL_NAME:
            case Constants.ADDRESS_LOGICAL_NAME:
            case Constants.LOCATION_LOGICAL_NAME: {
                result = new NamedFieldBuilder().fieldType(logicalName);
                break;
            }
            case Constants.LIST_LOGICAL_NAME: {
                result = arrayField(schema, name, mediaTypeName)
                        .fieldType(logicalName);
                break;
            }
            case Constants.MULTI_FIELD_LOGICAL_NAME: {
                result = objectField(schema, name, mediaTypeName).fieldType(logicalName);
                break;
            }
            case Constants.CAMERA_LOGICAL_NAME:
                throw new UnsupportedOperationException("Camera is not supported yet");
            case Constants.ENTITY_REFERENCE_LOGICAL_NAME: {
                Map<String, Object> entityInfo = (Map<String, Object>) schema.getExtensions().get(X_KRISTA_ENTITY);
                result = new NamedFieldBuilder().fieldType(
                        domainInfo.getEntityFieldType((String) entityInfo.get("entityId")));
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown field: " + logicalName);
        }
        if (tyeInfo.get("isSecured") != null && tyeInfo.get("isSecured").equals(true)) {
            result.attribute("isSecured", true);
        }
        return result;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Pair<JaxrsRequestItem, CatalogRequestInfo> getRequestItems(Map.Entry<String, PathItem> path,
            PathItem pathItem, Operation operation, PathItem.HttpMethod method) {
        CatalogRequest.Type type = guessCatalogRequestType(operation, method);
        String requestId = getRequestId(operation);
        String requestName = guessRequestName(operation);
        String description = guessDescription(operation);

        // inputFields
        final List<NamedField> inputFields = new ArrayList<>();
        addParameters(pathItem.getParameters(), inputFields);
        addParameters(operation.getParameters(), inputFields);

        List<Parameter> defaultParameters = new ArrayList<>(getDefaultParameters(pathItem.getParameters()));
        defaultParameters.addAll(getDefaultParameters(operation.getParameters()));
        Schema responseSchema = null;
        Schema<?> requestSchema = null;
        Binding requestBinding = null;

        Pair<String, MediaType> validMediaType = findValidMediaType(operation);
        String requestContentType = null;
        if (validMediaType != null) {
            requestContentType = validMediaType.getKey();
            MediaType mediaType = validMediaType.getValue();
            requestSchema = mediaType.getSchema();
            NamedField namedField =
                    namedFieldFromSchema(mediaType.getSchema(),
                            guessName(mediaType.getSchema(), false),
                            requestContentType).toNamedField();

            if (namedField.getFieldType().equals(MULTI_FIELD_LOGICAL_NAME)) {
                List<NamedField> fields = (List<NamedField>) namedField.getAttributeValues().get("fields");

                List<String> fieldNames = fields.stream().map(NamedField::getName).collect(Collectors.toList());
                List<String> parameterNames = inputFields.stream().map(NamedField::getName)
                        .collect(Collectors.toList());
                if (requestContentType.equals(TEXT_PLAIN)) {
                    requestBinding = new Binding.WrapperBinding(namedField.getName());
                    inputFields.add(namedField);
                } else if (Collections.disjoint(fieldNames, parameterNames)) {
                    requestBinding = new Binding.DirectBinding(fieldNames);
                    inputFields.addAll(fields);
                } else {
                    requestBinding = new Binding.WrapperBinding(namedField.getName());
                    inputFields.add(namedField);
                }
            } else {
                requestBinding = new Binding.WrapperBinding(namedField.getName());
                inputFields.add(namedField);
            }

        }

        Binding responseBinding = null;
        ApiResponse apiResponse = findApiResponse(operation);
        if (apiResponse == null) {
            if (method.equals(PathItem.HttpMethod.POST) || method.equals(PathItem.HttpMethod.DELETE) || method.equals(
                    PathItem.HttpMethod.PATCH) || method.equals(PathItem.HttpMethod.PUT) || method.equals(
                    PathItem.HttpMethod.OPTIONS) || method.equals(PathItem.HttpMethod.HEAD)) {
                apiResponse = new ApiResponse();
            } else {
                LOGGER.error("200 response not found for {} {}", path.getKey(), method);
                return null;
            }
        }
        List<NamedField> outputFields = List.of();
        String mediaTypeName = findValidMediaType(apiResponse);
        if (mediaTypeName != null) {
            MediaType mediaType = apiResponse.getContent().get(mediaTypeName);
            responseSchema = mediaType.getSchema();
            if (!isEmptySchema(responseSchema)) {
                if (supportContentType(mediaTypeName)) {
                    NamedFieldBuilder namedFieldBuilder =
                            namedFieldFromSchema(responseSchema, guessName(responseSchema, true),
                                    mediaTypeName);
                    NamedField namedField = namedFieldBuilder.toNamedField();
                    if (namedField.getFieldType().equals(MULTI_FIELD_LOGICAL_NAME)) {
                        List<NamedField> fields =
                                (List<NamedField>) namedField.getAttributeValues().get("fields");
                        List<String> fieldNames = fields.stream().map(NamedField::getName)
                                .collect(Collectors.toList());
                        responseBinding = new Binding.DirectBinding(fieldNames);
                        outputFields = new ArrayList<>(fields);
                    } else if (TEXT_PLAIN.equals(mediaTypeName)) {
                        responseBinding = new Binding.WrapperBinding(namedField.getName());
                        outputFields = List.of(namedField);
                    } else {
                        responseBinding = new Binding.WrapperBinding(namedField.getName());
                        outputFields = List.of(namedField);
                    }
                } else {
                    LOGGER.error("Unhandled media type: {}", mediaTypeName);
                    return null;
                }
            }
        }
        JaxrsRequestItem.SpecItems specItems = new JaxrsRequestItem.SpecItems(pathItem, method, operation,
                responseSchema, requestSchema);
        JaxrsRequestItem jaxrsRequestItem = new JaxrsRequestItem(requestId, path.getKey(),
                specItems, requestContentType, requestBinding, responseBinding, defaultParameters);
        CatalogRequestInfo catalogRequestInfo =
                new CatalogRequestInfo(requestId, getAreaName(pathItem), requestName, description, type, inputFields,
                        outputFields);
        return Pair.of(jaxrsRequestItem, catalogRequestInfo);
    }

    private List<Parameter> getDefaultParameters(List<Parameter> parameters) {
        List<Parameter> result = new ArrayList<>();
        parameters = Objects.requireNonNullElse(parameters, List.of());
        for (Parameter parameter : parameters) {
            if (parameter.getSchema().getEnum() != null && parameter.getSchema().getEnum().size() == 1) {
                result.add(parameter);
            }
        }
        return result;
    }

    private String findValidMediaType(ApiResponse apiResponse) {
        if (apiResponse.getContent() != null) {
            for (String mimeType : apiResponse.getContent().keySet()) {
                if (isValidMimeType(mimeType)) {
                    return mimeType;
                }
            }
        }
        return null;
    }

    private Pair<String, MediaType> findValidMediaType(Operation operation) {
        if (operation.getRequestBody() != null) {
            for (Map.Entry<String, MediaType> entry : operation.getRequestBody().getContent().entrySet()) {
                String mediaTypeName = entry.getKey();
                MediaType mediaType = entry.getValue();
                if (supportContentType(mediaTypeName)) {
                    return Pair.of(mediaTypeName, mediaType);
                }
            }
            throw new IllegalArgumentException("No valid media type found for " + operation.getOperationId());
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    private boolean isEmptySchema(Schema schema) {
        if (schema == null) {
            return true;
        }
        if (Constants.OBJECT.equals(schema.getType())) {
            return schema.getProperties() == null;
        }
        return false;
    }

    private String guessName(Schema<?> schema, boolean isOutput) {
        String defaultReturn = getDefaultName(isOutput);
        if (schema instanceof ObjectSchema) {
            ObjectSchema objectSchema = (ObjectSchema) schema;
            String name = objectSchema.getName();
            if (name == null) {
                name = schema.getXml() != null ? schema.getXml().getName() : null;
            }
            if (name == null) {
                name = defaultReturn;
            }
            return name;

        }
        return defaultReturn;
    }

    private boolean isValidMimeType(String mimeType) {
        return Set.of(APPLICATION_JSON, APPLICATION_XML, TEXT_PLAIN, MULTIPART_FORM_DATA, APPLICATION_PDF,
                IMAGE_JPEG, IMAGE_PNG, IMAGE_GIF, AUDIO_MPEG, AUDIO_WAV, VIDEO_AVI, VIDEO_MP4,
                APPLICATION_OCTET_STREAM).contains(mimeType);
    }

}


