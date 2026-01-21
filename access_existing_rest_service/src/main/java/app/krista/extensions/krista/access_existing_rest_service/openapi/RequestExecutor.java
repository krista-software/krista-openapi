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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import app.krista.extension.request.CatalogRequest;
import app.krista.extensions.krista.access_existing_rest_service.Constants;
import app.krista.extensions.krista.access_existing_rest_service.RequestBodyHandler;
import app.krista.extensions.krista.access_existing_rest_service.auth.AuthenticationInfo;
import app.krista.extensions.krista.access_existing_rest_service.auth.Authenticator;
import app.krista.extensions.krista.access_existing_rest_service.auth.AuthenticatorFactory;
import app.krista.extensions.krista.access_existing_rest_service.data.RequestInfo;
import app.krista.extensions.krista.access_existing_rest_service.model.Binding;
import app.krista.extensions.krista.access_existing_rest_service.model.JaxrsRequestItem;
import app.krista.extensions.krista.access_existing_rest_service.model.OpenApiContext;
import app.krista.extensions.krista.access_existing_rest_service.services.InvokerAttributes;
import app.krista.ksdk.files.FileHandle;
import com.google.gson.Gson;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static app.krista.extensions.krista.access_existing_rest_service.Constants.MULTIPART_FORM_DATA;
import static app.krista.extensions.krista.access_existing_rest_service.Constants.*;
import static javax.ws.rs.core.HttpHeaders.*;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.MediaType.*;
import static javax.ws.rs.core.Response.Status.Family.*;

public class RequestExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecManager.class);

    private final OpenApiContext openApiContext;
    @Inject
    private JaxrsClient jaxrsClient;
    @Inject
    private Gson gson;
    @Inject
    private AuthenticatorFactory authenticatorFactory;
    @Inject
    private InvokerAttributes invokerAttributes;
    @Inject
    private RequestBodyHandler requestBodyHandler;
    @Inject
    private ResponseBodyHandler responseBodyHandler;

    public RequestExecutor(OpenApiContext openApiContext) {
        this.openApiContext = openApiContext;
    }

    public Map<String, Object> executeCatalogRequest(CatalogRequest catalogRequest,
            Map<String, Object> arguments) {
        OpenApiContext currentSpec = openApiContext;

        Objects.requireNonNull(currentSpec);
        Objects.requireNonNull(invokerAttributes.getServerAddress(), SERVER_ADDRESS);

        JaxrsRequestItem jaxrsRequestItem =
                currentSpec.getRequestInfoMap().get(catalogRequest.getRequestId()).getJaxrsRequestItem();
        RequestInfo.Builder requestBuilder = new RequestInfo.Builder();

        fillInParameters(arguments, jaxrsRequestItem, requestBuilder);

        RequestBodyItem result = createRequestBodyItem(arguments, jaxrsRequestItem, jaxrsRequestItem.getOperation());

        List<AuthenticationInfo<Object>> authenticationInfoList = currentSpec.getAuthenticationInfoList();
        if (authenticationInfoList != null) {
            for (AuthenticationInfo<?> authenticationInfo : authenticationInfoList) {
                Authenticator authenticator = authenticatorFactory.create(authenticationInfo);
                authenticator.authenticate(authenticationInfo, requestBuilder);
            }
        }

        RequestInfo requestInfo = requestBuilder
                .body(result.requestBodyString == null ? result.form : result.requestBodyString)
                .serverAddress(invokerAttributes.getServerAddress())
                .pathTemplate(jaxrsRequestItem.getPath())
                .httpMethod(jaxrsRequestItem.getHttpMethod().name())
                .mediaType(jaxrsRequestItem.getRequestContentType())
                .build();
        String responseString;
        try (Response response = jaxrsClient.callSync(requestInfo)) {
            if (!SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
                responseString = response.readEntity(String.class);
                String userFriendlyError = getUserFriendlyErrorMessage(catalogRequest.getRequestName(),
                        response.getStatus(), response.getStatusInfo().getReasonPhrase(), responseString);
                LOGGER.error("API Request failed - Request: '{}', Status: {}, Response: {}",
                        catalogRequest.getRequestName(), response.getStatus(), responseString);
                throw new IllegalStateException(userFriendlyError);
            }
            return processResponse(jaxrsRequestItem, response);
        }
    }

    private void fillInParameters(Map<String, Object> arguments, JaxrsRequestItem jaxrsRequestItem,
            RequestInfo.Builder requestBuilder) {
        Operation operation = jaxrsRequestItem.getOperation();
        List<Parameter> defaultParameters = jaxrsRequestItem.getDefaultParameters();
        for (Parameter parameter : defaultParameters) {
            Object jaxrsValue = parameter.getSchema().getEnum().get(0);
            addParameter(requestBuilder, parameter, jaxrsValue);
        }
        if (operation.getParameters() == null) {
            return;
        }
        for (Parameter parameter : operation.getParameters()) {
            if (arguments.get(parameter.getName()) == null) {
                continue;
            }
            Object jaxrsValue = toJaxrsValue(arguments.get(parameter.getName()), parameter.getSchema());
            addParameter(requestBuilder, parameter, jaxrsValue);
        }
    }

    private RequestBodyItem createRequestBodyItem(Map<String, Object> arguments, JaxrsRequestItem jaxrsRequestItem,
            Operation operation) {
        Binding requestBinding = jaxrsRequestItem.getRequestBinding();
        Object requestBody;
        if (requestBinding == null) {
            return new RequestBodyItem(null, null);
        }
        if (requestBinding instanceof Binding.DirectBinding) {
            Map<String, Object> result = new HashMap<>();
            for (String fieldName : ((Binding.DirectBinding) requestBinding).getFieldNames()) {
                if (arguments.get(fieldName) != null) {
                    result.put(fieldName, arguments.get(fieldName));
                }
            }
            requestBody = result;
        } else if (requestBinding instanceof Binding.WrapperBinding) {
            requestBody = arguments.get(((Binding.WrapperBinding) requestBinding).getWrapperName());
        } else {
            throw new IllegalStateException("Configuration error: Unknown request binding type '" + requestBinding + "'. This indicates an issue with the OpenAPI specification processing. Please check your OpenAPI specification or contact support.");
        }
        Object requestBodyString = null;
        FormDataMultiPart form = null;
        if (operation.getRequestBody() != null) {
            Content content = operation.getRequestBody().getContent();
            if (content.containsKey(MULTIPART_FORM_DATA)) {
                form = new FormDataMultiPart();
                requestBody = requestBodyHandler.fillInDefaultValues(requestBody, jaxrsRequestItem.getRequestSchema());
                if (requestBodyHandler.isFileRequest(requestBody)) {
                    requestBodyHandler.handleMultipartFile(jaxrsRequestItem, form, requestBody);
                } else {
                    requestBodyString = requestBody != null ? gson.toJson(requestBody) : null;
                }
            } else if (requestBodyHandler.isFileContentType(content)) {
                requestBodyString = requestBodyHandler.handleFileRequestBody(requestBody);
            } else if (TEXT_PLAIN.equals(jaxrsRequestItem.getRequestContentType())) {
                requestBodyString = requestBody;
            } else {
                requestBodyString = requestBodyHandler.handleDefaultRequestBody(requestBody, jaxrsRequestItem);
            }
        }
        return new RequestBodyItem(requestBodyString, form);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> processResponse(JaxrsRequestItem jaxrsRequestItem, Response response) {
        String responseString;
        Binding responseBinding = jaxrsRequestItem.getResponseBinding();
        if (responseBinding != null) {
            Object out;
            String contentType = response.getHeaderString(CONTENT_TYPE);
            Objects.requireNonNull(contentType, CONTENT_TYPE);
            if (contentType.startsWith(APPLICATION_JSON)) {
                responseString = response.readEntity(String.class);
                out = gson.fromJson(responseString, Object.class);
            } else if (responseBodyHandler.isFileType(contentType)) {
                InputStream inputStream = response.readEntity(InputStream.class);
                FileHandle file = getFileHandle(contentType, inputStream);
                Objects.requireNonNull(file, "Failed to get file handle");
                try {
                    out = file.getFile();
                } catch (IOException e) {
                    throw new IllegalStateException("File processing error: Unable to retrieve the file from the API response. This may be due to a corrupted file or network issue. Please try the request again. Technical details: " + e.getMessage(), e);
                }
            } else if (contentType.startsWith(MULTIPART_FORM_DATA)) {
                out = handleMultiForm(response);
            } else if (contentType.startsWith(TEXT_PLAIN) || contentType.startsWith(TEXT_HTML)) {
                responseString = response.readEntity(String.class);
                out = responseString;
            } else {
                throw new IllegalStateException("Response format error: The API returned data in an unsupported format ('" + contentType + "'). This extension supports JSON, plain text, HTML, files, and multipart responses. Please check your API specification or contact the API provider.");
            }
            if (responseBinding instanceof Binding.DirectBinding) {
                return (Map<String, Object>) out;
            } else if (responseBinding instanceof Binding.WrapperBinding) {
                return Map.of(((Binding.WrapperBinding) responseBinding).getWrapperName(), out);
            } else {
                throw new IllegalStateException("Configuration error: Unknown response binding type '" + responseBinding + "'. This indicates an issue with the OpenAPI specification processing. Please check your OpenAPI specification or contact support.");
            }
        } else {
            return Map.of();
        }
    }

    private static void addParameter(RequestInfo.Builder requestBuilder, Parameter parameter, Object jaxrsValue) {
        if (Objects.equals(parameter.getIn(), Constants.QUERY)) {
            requestBuilder.queryParameter(parameter.getName(), jaxrsValue);
        } else if (Objects.equals(parameter.getIn(), Constants.PATH)) {
            requestBuilder.pathParameter(parameter.getName(), jaxrsValue);
        } else if (Objects.equals(parameter.getIn(), Constants.HEADER)) {
            requestBuilder.header(parameter.getName(), jaxrsValue);
        } else {
            LOGGER.warn("Unknown parameter: {}", parameter.getIn());
        }
    }

    private Object toJaxrsValue(Object kristaValue, Schema<?> schema) {
        String parameterType = schema.getType();
        if ("integer".equals(parameterType) || "number".equals(parameterType) && "int64".equals(schema.getFormat())) {
            if (kristaValue instanceof Integer) {
                return kristaValue;
            } else if (kristaValue instanceof Double) {
                return ((Double) kristaValue).intValue();
            }
        }
        return kristaValue;
    }

    private FileHandle getFileHandle(String contentType, InputStream inputStream) {
        FileHandle file = responseBodyHandler.getFileHandle(contentType);
        try (file) {
            if (file != null) {
                file.setContent(inputStream);
                file.getContent();
            }
        } catch (IOException cause) {
            throw new IllegalStateException("File processing error: Unable to process the file from the API response. This may be due to a network issue or corrupted file data. Please try the request again. Technical details: " + cause.getMessage());
        }
        return file;
    }

    Map<String, Object> handleMultiForm(Response response) {
        Map<String, Object> multipartData = new HashMap<>();
        MultiPart multiPart = response.readEntity(MultiPart.class);
        for (BodyPart bodyPart : multiPart.getBodyParts()) {
            ContentDisposition contentDisposition = bodyPart.getContentDisposition();
            if (contentDisposition != null) {
                Map<String, String> innerParameter = contentDisposition.getParameters();
                String fieldName = innerParameter.get("name");
                if (bodyPart.getMediaType() != null && responseBodyHandler.isFileType(
                        bodyPart.getMediaType().toString())) {
                    InputStream fileInputStream = bodyPart.getEntityAs(InputStream.class);
                    FileHandle fileHandle = getFileHandle(bodyPart.getMediaType().toString(), fileInputStream);
                    Objects.requireNonNull(fileHandle, "Failed to get file handle");
                    try {
                        multipartData.put(fieldName, fileHandle.getFile());
                    } catch (IOException e) {
                        throw new IllegalStateException("File processing error: Unable to retrieve the file from the multipart response. This may be due to a corrupted file or network issue. Please try the request again. Technical details: " + e.getMessage(), e);
                    }
                } else {
                    String fieldValue = bodyPart.getEntityAs(String.class);
                    multipartData.put(fieldName, gson.fromJson(fieldValue, Object.class));
                }
            }
        }
        return multipartData;
    }

    private String getUserFriendlyErrorMessage(String requestName, int statusCode, String reasonPhrase, String responseBody) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("API request '").append(requestName).append("' failed. ");

        switch (statusCode) {
            case 400:
                errorMessage.append("Bad Request: The request contains invalid data or parameters. Please check your input values and try again.");
                break;
            case 401:
                errorMessage.append("Authentication Failed: Your API credentials are invalid or expired. Please verify your API key, username/password, or stored token in the extension configuration.");
                break;
            case 403:
                errorMessage.append("Access Denied: You don't have permission to access this resource. Please check your account permissions or contact your administrator.");
                break;
            case 404:
                errorMessage.append("Resource Not Found: The requested endpoint or resource doesn't exist. Please verify the API URL and endpoint path.");
                break;
            case 429:
                errorMessage.append("Rate Limit Exceeded: Too many requests have been made. Please wait a moment and try again.");
                break;
            case 500:
                errorMessage.append("Server Error: The remote server encountered an internal error. Please try again later or contact the API provider.");
                break;
            case 502:
            case 503:
            case 504:
                errorMessage.append("Service Unavailable: The remote service is temporarily unavailable. Please try again later.");
                break;
            default:
                errorMessage.append("Request failed with status ").append(statusCode).append(" (").append(reasonPhrase).append(").");
        }

        if (responseBody != null && !responseBody.trim().isEmpty() && responseBody.length() < 200) {
            errorMessage.append(" Server response: ").append(responseBody.trim());
        }

        return errorMessage.toString();
    }

    private static class RequestBodyItem {

        public final Object requestBodyString;
        public final FormDataMultiPart form;

        public RequestBodyItem(Object requestBodyString, FormDataMultiPart form) {
            this.requestBodyString = requestBodyString;
            this.form = form;
        }

    }

}
