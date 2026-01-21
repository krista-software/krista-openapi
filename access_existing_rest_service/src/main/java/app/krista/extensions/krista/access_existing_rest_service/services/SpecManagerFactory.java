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

package app.krista.extensions.krista.access_existing_rest_service.services;

import javax.inject.Inject;
import app.krista.extension.executor.Invoker;
import app.krista.extensions.krista.access_existing_rest_service.model.OpenApiContext;
import app.krista.extensions.krista.access_existing_rest_service.openapi.SpecManager;
import app.krista.extensions.krista.access_existing_rest_service.openapi.SpecStore;
import com.google.gson.Gson;
import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.annotations.Service;

@Service
public class SpecManagerFactory {

    @Inject
    private ServiceLocator serviceLocator;

    public SpecManager create() {
        SpecStore specStore = serviceLocator.getService(SpecStore.class);
        OpenApiContext openApiContext = specStore.getSpec();
        Gson gson = serviceLocator.getService(Gson.class);
        Invoker invoker = serviceLocator.getService(Invoker.class);
        return new SpecManager(serviceLocator, specStore, gson, invoker, openApiContext);
    }

}
