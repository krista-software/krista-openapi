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

package app.krista.extensions.sdk.openapi.impl;

import app.krista.extensions.sdk.openapi.KnownException;
import app.krista.extensions.sdk.openapi.KristaOpenAPIParser;
import app.krista.extensions.sdk.openapi.OpenApiOperationBinding;
import app.krista.extensions.sdk.openapi.spi.OpenApiBinder;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.jvnet.hk2.annotations.ContractsProvided;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

@Service
@ContractsProvided(KristaOpenAPIParser.class)
public class KristaOpenAPIParserImpl implements KristaOpenAPIParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(KristaOpenAPIParserImpl.class);
    private final OpenAPIParser parser;
    private final ParseOptions defaultParseOptions;

    private static ParseOptions createDefaultParseOptions() {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);
        parseOptions.setResolveRequestBody(true);
        return parseOptions;
    }

    @Inject
    public KristaOpenAPIParserImpl() {
        this(new OpenAPIParser(), createDefaultParseOptions());
    }

    public KristaOpenAPIParserImpl(OpenAPIParser parser, ParseOptions defaultParseOptions) {
        this.parser = parser;
        this.defaultParseOptions = defaultParseOptions;
    }

    @Override
    public OpenAPI parseSpec(String openApiSpec) {
        SwaggerParseResult result = parser.readContents(openApiSpec, null, defaultParseOptions);
        if (!result.getMessages().isEmpty()) {
            String errorMessage = String.join("\n", result.getMessages());
            if (result.getOpenAPI() == null || result.getOpenAPI().getPaths() == null ||
                    result.getOpenAPI().getPaths().isEmpty()) {
                throw new KnownException("Incorrect specification", new Exception(errorMessage));
            } else {
                LOGGER.warn("The spec contains some error: {}", errorMessage);
            }
        }
        return result.getOpenAPI();
    }

    @Override
    public <A, B, C, D, E, F, G> List<OpenApiOperationBinding<A, D, E, F, G>> parseAndBindAll(
            String openApiSpec, OpenApiBinder<A, B, C> binder) {
        return List.of();
    }

}
