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

package app.krista.extensions.krista.access_existing_rest_service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.inject.Inject;
import app.krista.extensions.krista.access_existing_rest_service.model.JaxrsRequestItem;
import app.krista.ksdk.files.FileHandle;
import app.krista.ksdk.files.FileRepository;
import app.krista.model.base.File;
import com.google.gson.Gson;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static app.krista.extensions.krista.access_existing_rest_service.Constants.*;

/**
 * Handles the processing of request bodies for an existing REST service, including handling files,
 * content types, and default values.
 */
@Service
public class RequestBodyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestBodyHandler.class);
    private final FileRepository fileRepository;
    private final Gson gson;

    /**
     * Constructs a new RequestBodyHandler with the specified FileRepository.
     *
     * @param fileRepository The repository used to access files.
     * @param gson           Gson instance
     */
    @Inject
    public RequestBodyHandler(FileRepository fileRepository, Gson gson) {
        this.fileRepository = fileRepository;
        this.gson = gson;
    }

    private static void normalFields(JaxrsRequestItem jaxrsRequestItem, FormDataMultiPart form,
            Map.Entry<String, Object> entry) {
        if (entry.getValue() != null) {
            Object value = entry.getValue();
            Schema<?> requestSchema =
                    (Schema<?>) jaxrsRequestItem.getRequestSchema().getProperties().get(entry.getKey());
            String type = requestSchema.getType();
            String stringValue;
            if (type.equalsIgnoreCase("integer")) {
                Double value1 = (Double) value;
                stringValue = Integer.toString(value1.intValue());
            } else {
                stringValue = value.toString();
            }
            form.field(entry.getKey(), stringValue);
        }
    }

    @SuppressWarnings("unchecked")
    public void handleMultipartFile(JaxrsRequestItem jaxrsRequestItem, FormDataMultiPart form, Object requestBody) {
        if (requestBody instanceof Map) {
            Map<String, Object> requestBodyMap = (Map<String, Object>) requestBody;
            for (Map.Entry<String, Object> entry : requestBodyMap.entrySet()) {
                if (entry.getValue() instanceof List) {
                    fileField(form, entry.getKey(), entry.getValue());
                } else {
                    normalFields(jaxrsRequestItem, form, entry);
                }
            }
        }
    }

    public Object handleFileRequestBody(Object requestBody) {
        return fileRequestBody(requestBody);
    }

    private Object fileRequestBody(Object requestBody) {
        Object requestBodyString;
        File file = null;
        if (requestBody instanceof File) {
            file = (File) requestBody;
        } else if (requestBody instanceof List && !((List<?>) requestBody).isEmpty()) {
            file = (File) ((List<?>) requestBody).get(0);
        } else {
            LOGGER.warn("File not found");
        }
        requestBodyString = getRequestBody(file);
        return requestBodyString;
    }

    // FIXME:- This should be removed. Default values should be handled by the 3rd party service.

    private Object getRequestBody(File file) {
        Object requestBodyString;
        try (FileHandle fileHandle = fileRepository.getFile(file)) {
            requestBodyString = fileHandle.getContent();
        } catch (IOException cause) {
            throw new IllegalStateException("Failed to get input stream " + cause.getMessage());
        }
        return requestBodyString;
    }

    /**
     * Checks if a given content type indicates a file upload.
     *
     * @param content The content to check for file types.
     * @return true if the content type corresponds to a file type, false otherwise.
     */
    public boolean isFileContentType(Content content) {
        return content.containsKey(APPLICATION_OCTET_STREAM)
                || content.containsKey(APPLICATION_PDF)
                || content.containsKey(IMAGE_GIF)
                || content.containsKey(IMAGE_JPEG)
                || content.containsKey(IMAGE_PNG)
                || content.containsKey(AUDIO_MPEG)
                || content.containsKey(AUDIO_WAV)
                || content.containsKey(VIDEO_AVI)
                || content.containsKey(VIDEO_MP4);
    }

    /**
     * Fills in default values in an object based on the provided schema.
     *
     * @param object The object to fill in with default values.
     * @param schema The schema containing default values.
     * @return The object with default values filled in, or null if not applicable.
     */
    public Object fillInDefaultValues(Object object, Schema<?> schema) {
        if (schema == null) {
            return object;
        }
        if (object == null) {
            return schema.getDefault();
        }
        switch (schema.getType()) {
            case "integer":
                return ((Double) object).longValue();
            case "number": {
                if ("int64".equals(schema.getFormat())) {
                    return ((Double) object).longValue();
                } else {
                    return object;
                }
            }
            case "string":
            case "array":
            case "boolean": {
                return object;
            }
            case Constants.OBJECT: {
                return handleObject(object, schema);
            }
            default:
                throw new IllegalArgumentException("Unknown Type: " + schema.getType());
        }

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map<String, Object> handleObject(Object object, Schema schema) {
        Objects.requireNonNull(schema.getProperties());
        List<String> required = Objects.requireNonNullElse(schema.getRequired(), List.of());
        Map<String, Object> result =
                new LinkedHashMap<>((Map<String, Object>) Objects.requireNonNullElse(object, Map.of()));
        for (Map.Entry<String, Schema> entry : (Set<Map.Entry<String, Schema>>) schema.getProperties().entrySet()) {
            String key = entry.getKey();
            Object value = result.get(key);
            value = fillInDefaultValues(value, entry.getValue());
            if (value == null && "array".equals(entry.getValue().getType()) && required.contains(key)) {
                value = List.of();
            }
            result.put(key, value);
        }
        return result;
    }

    public Object handleDefaultRequestBody(Object requestBody, JaxrsRequestItem jaxrsRequestItem) {
        requestBody = fillInDefaultValues(requestBody, jaxrsRequestItem.getRequestSchema());
        return (requestBody != null) ? gson.toJson(requestBody) : null;
    }

    public boolean isFileRequest(Object requestBody) {
        if (requestBody instanceof File) {
            return true;
        }
        if (requestBody instanceof List) {
            List<?> list = (List<?>) requestBody;
            if (!list.isEmpty()) {
                Object firstElement = list.get(0);
                if (firstElement instanceof List) {
                    return isFileRequest(firstElement);
                } else {
                    return firstElement instanceof File;
                }
            }
        } else if (requestBody instanceof Map) {
            return isFileRequest((Map<?, ?>) requestBody);
        }
        return false;
    }

    private boolean isFileRequest(Map<?, ?> requestBody) {
        for (Object value : requestBody.values()) {
            if (value instanceof List && isFileRequest(value)) {
                return true;
            }
        }
        return false;
    }

    private void fileField(FormDataMultiPart form, String fileName, Object value) {
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            for (Object item : list) {
                fileField(form, fileName, item);
            }
        } else if (value instanceof File) {
            File file = (File) value;
            try (FileHandle fileHandle = fileRepository.getFile(file)) {
                InputStream content = fileHandle.getContent();
                form.bodyPart(new StreamDataBodyPart(fileName, content, file.getFileName()));
            } catch (IOException cause) {
                throw new IllegalStateException("Failed to get input stream " + cause.getMessage());
            }
        }
    }

}
