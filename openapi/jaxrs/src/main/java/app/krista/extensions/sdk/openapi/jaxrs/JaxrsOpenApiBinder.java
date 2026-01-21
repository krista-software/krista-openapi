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

package app.krista.extensions.sdk.openapi.jaxrs;

import app.krista.extensions.sdk.openapi.OpenApiFromOperationToBinding;
import app.krista.extensions.sdk.openapi.OpenApiOperation;
import app.krista.extensions.sdk.openapi.jaxrs.impl.JaxrsOpenApiToOperationFromJaxrsRequest;
import app.krista.extensions.sdk.openapi.spi.OpenApiBinder;
import app.krista.ksdk.Injectable;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

@Injectable
public interface JaxrsOpenApiBinder extends OpenApiBinder<Invocation, Object, Response> {

    @Override
    JaxrsOpenApiToOperationFromJaxrsRequest bindFromOpenApi(OpenApiOperation operation);

    @Override
    default OpenApiFromOperationToBinding<Invocation, Object, Response> bindToOpenApi(Invocation operation) {
        throw new UnsupportedOperationException();
    }
}
