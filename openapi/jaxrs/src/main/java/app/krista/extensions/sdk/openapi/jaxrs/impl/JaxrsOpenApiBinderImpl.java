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

package app.krista.extensions.sdk.openapi.jaxrs.impl;

import app.krista.extensions.sdk.openapi.OpenApiOperation;
import app.krista.extensions.sdk.openapi.jaxrs.JaxrsOpenApiBinder;
import com.google.gson.Gson;
import org.jvnet.hk2.annotations.ContractsProvided;
import org.jvnet.hk2.annotations.Service;
import javax.inject.Inject;

@Service
@ContractsProvided(JaxrsOpenApiBinder.class)
public class JaxrsOpenApiBinderImpl implements JaxrsOpenApiBinder {

    private final Gson gson;

    @Inject
    public JaxrsOpenApiBinderImpl(Gson gson) {
        this.gson = gson;
    }

    @Override
    public JaxrsOpenApiToOperationFromJaxrsRequest bindFromOpenApi(OpenApiOperation operation) {
        //parse open api operation
        String httpMethod = operation.getHttpMethod().name();
        return new JaxrsOpenApiToOperationFromJaxrsRequest(operation, httpMethod);
    }

}
