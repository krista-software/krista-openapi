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

import java.util.*;
import javax.inject.Inject;
import app.krista.extensions.krista.access_existing_rest_service.Constants;
import app.krista.extensions.krista.access_existing_rest_service.auth.AuthenticationInfo;
import app.krista.extensions.krista.access_existing_rest_service.auth.AuthenticationInfoFactory;
import app.krista.extensions.krista.access_existing_rest_service.error.KnownException;
import app.krista.extensions.krista.access_existing_rest_service.model.*;
import app.krista.model.entity.EntityAttributeField;
import app.krista.model.entity.EntityDefinition;
import app.krista.model.field.NamedField;
import app.krista.model.field.NamedValuedField;
import com.google.gson.Gson;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static app.krista.extensions.krista.access_existing_rest_service.Constants.*;

@Service
public class SpecParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecParser.class);
    private final Gson gson;

    @Inject
    public SpecParser(Gson gson) {
        this.gson = gson;
    }

    public OpenApiContext parseSpec(String swaggerString) {
        OpenAPI openAPI = readContents(swaggerString);
        verifyCompatible(openAPI);
        Paths paths = openAPI.getPaths();
        List<AuthenticationInfo<Object>> authenticationInfoList = AuthenticationInfoFactory.create(openAPI);
        DomainInfo domainInfo = getDomainInfo(openAPI);
        Map<String, RequestInfo> requests = new RequestsParser(domainInfo).getRequests(paths);
        EntityRequests entityRequests = getEntityRequests(paths);
        if (requests.isEmpty()) {
            throw new KnownException("OpenAPI specification error: No valid API endpoints were found in your specification. Please ensure your OpenAPI specification contains at least one path with a supported HTTP method (GET, POST, PUT, DELETE, PATCH) and proper operation definitions.");
        }
        Info info = openAPI.getInfo();
        SpecInfo specInfo = new SpecInfo(info.getTitle(), info.getDescription(), info.getVersion());
        return new OpenApiContext(requests, specInfo, authenticationInfoList, domainInfo,
                getEntities(openAPI), entityRequests);
    }

    public OpenAPI readContents(String openApiSpec) {
        OpenAPIParser parser = new OpenAPIParser();
        SwaggerParseResult result = parser.readContents(openApiSpec, null, createDefaultParseOptions());
        if (!result.getMessages().isEmpty()) {
            String errorMessage = String.join("\n", result.getMessages());
            if (result.getOpenAPI() == null || result.getOpenAPI().getPaths() == null ||
                    result.getOpenAPI().getPaths().isEmpty()) {
                throw new KnownException("OpenAPI specification validation failed: Your specification contains critical errors that prevent processing. Please fix the following issues and try again:\n" + errorMessage + "\n\nTip: Use an OpenAPI validator tool to check your specification before uploading.", new Exception(errorMessage));
            } else {
                LOGGER.warn("OpenAPI specification contains warnings (processing will continue): {}", errorMessage);
            }
        }
        return result.getOpenAPI();
    }

    private void verifyCompatible(OpenAPI openAPI) {
        Map<String, Object> extensions = openAPI.getExtensions();
        if (extensions == null) {
            return;
        }
        Object version = extensions.get(Constants.X_KRISTA_VERSION);
        if (version == null) {
            return;
        }
        if (!Constants.compatibleVersions.contains(version.toString())) {
            throw new KnownException("Version compatibility error: Your OpenAPI specification was created for extension version '" + version + "', but this extension supports versions " + Constants.compatibleVersions + ". Please either update your extension to a compatible version or modify your OpenAPI specification to match the current extension version.");
        }
    }

    private DomainInfo getDomainInfo(OpenAPI openAPI) {
        Map<String, Object> extensions = openAPI.getExtensions();
        if (extensions == null) {
            return Constants.DEFAULT_DOMAIN_INFO;
        }
        Object domainInfo = extensions.get(Constants.X_KRISTA_DOMAIN);
        if (domainInfo == null) {
            return Constants.DEFAULT_DOMAIN_INFO;
        }
        return gson.fromJson(gson.toJson(domainInfo), DomainInfo.class);
    }

    private EntityRequests getEntityRequests(Paths paths) {
        EntityRequests.Builder builder = new EntityRequests.Builder();
        for (Map.Entry<String, PathItem> path : paths.entrySet()) {
            PathItem pathItem = path.getValue();
            if (pathItem.getExtensions() != null &&
                    pathItem.getExtensions().containsKey(X_KRISTA_ENTITY_REQUEST)) {
                String requestType = (String) pathItem.getExtensions().get(X_KRISTA_ENTITY_REQUEST);
                switch (requestType) {
                    case "get":
                        builder.get(path.getKey());
                        break;
                    case "create":
                        builder.create(path.getKey());
                        break;
                    case "update":
                        builder.update(path.getKey());
                        break;
                    case "delete":
                        builder.delete(path.getKey());
                        break;
                    case "search":
                        builder.search(path.getKey());
                        break;
                    case "lookup":
                        builder.lookup(path.getKey());
                        break;
                    case "supports":
                        builder.supports(path.getKey());
                        break;
                    case "contains":
                        builder.contains(path.getKey());
                        break;
                    case "count":
                        builder.count(path.getKey());
                        break;
                    default:
                        // ignore
                        LOGGER.error("Unknown entity request type: {}", requestType);
                }
            }
        }
        return builder.build();

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<EntityItem> getEntities(OpenAPI openAPI) {
        if (openAPI.getComponents() == null || openAPI.getComponents().getSchemas() == null) {
            return Collections.emptyList();
        }
        List<EntityItem> result = new ArrayList<>();
        for (Map.Entry<String, Schema> entry : openAPI.getComponents().getSchemas().entrySet()) {
            Schema entitySchema = entry.getValue();
            if (entitySchema.getExtensions() != null &&
                    entitySchema.getExtensions().containsKey(Constants.X_KRISTA_ENTITY)) {
                Map<String, Object> m =
                        (Map<String, Object>) entitySchema.getExtensions().get(Constants.X_KRISTA_ENTITY);
                EntityInfo entityInfo = gson.fromJson(gson.toJson(m), EntityInfo.class);
                EntityItem entityItem =
                        new EntityItem(entityInfo, entitySchema, getFieldMap(entitySchema, getDomainInfo(openAPI)));
                result.add(entityItem);
            }
        }
        return result;
    }

    private static ParseOptions createDefaultParseOptions() {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);
        parseOptions.setResolveRequestBody(true);
        return parseOptions;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map<String, EntityAttributeField> getFieldMap(Schema schema, DomainInfo domainInfo) {
        Map<String, EntityAttributeField> result = new HashMap<>();
        Map<String, Schema> properties = (Map<String, Schema>) schema.getProperties();
        for (Map.Entry<String, Schema> entry : properties.entrySet()) {
            Schema s = entry.getValue();
            NamedFieldBuilder namedFieldBuilder =
                    new RequestsParser(domainInfo).namedFieldFromSchema(s, entry.getKey(), null);
            if (schema.getRequired() != null && schema.getRequired().contains(entry.getKey())) {
                namedFieldBuilder.required(true);
            }
            if (s.getExtensions() != null) {
                Map<String, Object> fieldProperties =
                        (Map<String, Object>) s.getExtensions()
                                .getOrDefault(X_KRISTA_ENTITY_FIELD_PROPERTIES, Map.of());
                boolean includeInSummary =
                        (boolean) fieldProperties.getOrDefault(INCLUDE_IN_SUMMARY, false);
                namedFieldBuilder.option(Constants.INCLUDE_IN_SUMMARY,
                        new NamedValuedField(EntityDefinition.Constants.OPTION_INCLUDE_IN_SUMMARY, "Boolean",
                                includeInSummary, new LinkedHashMap<>(), new LinkedHashMap<>()));
                List<String> availableSearches =
                        (List<String>) fieldProperties.getOrDefault(Constants.AVAILABLE_SEARCHES, List.of());
                namedFieldBuilder.option(EntityDefinition.Constants.OPTION_SEARCHABLE, new NamedValuedField(
                        EntityDefinition.Constants.OPTION_SEARCHABLE, "Boolean",
                        availableSearches.contains("full_text"), new LinkedHashMap<>(), new LinkedHashMap<>()));
                namedFieldBuilder.option(IS_IN_UNIQUE, new NamedValuedField("isInUnique", "Boolean",
                        fieldProperties.getOrDefault(IS_IN_UNIQUE, false), new LinkedHashMap<>(),
                        new LinkedHashMap<>()));
            }
            NamedField namedField = namedFieldBuilder.toNamedField();
            EntityAttributeField entityAttributeField = new EntityAttributeField(entry.getKey(),
                    namedField.getFieldType(), namedField.getAttributeValues(), namedField.getOptions(), "extension",
                    Set.of());
            result.put(entry.getKey(), entityAttributeField);
        }

        return result;
    }

}
