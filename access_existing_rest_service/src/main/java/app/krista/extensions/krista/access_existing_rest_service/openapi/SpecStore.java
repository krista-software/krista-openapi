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

import javax.inject.Inject;
import app.krista.extensions.krista.access_existing_rest_service.model.OpenApiContext;
import app.krista.extensions.util.KeyValueStore;
import com.google.gson.Gson;
import org.jvnet.hk2.annotations.Service;

@Service
public class SpecStore {

    private final KeyValueStore keyValueStore;
    private final Gson gson;
    private OpenApiContext specCache = null;

    @Inject
    public SpecStore(KeyValueStore keyValueStore, Gson gson) {
        this.keyValueStore = keyValueStore;
        this.gson = gson;
    }

    public OpenApiContext getSpec() {
        if (specCache == null) {
            synchronized (this) {
                String storedString = (String) keyValueStore.get(OpenApiContext.STORE_KEY);
                if (storedString == null) {
                    return null;
                }
                specCache = gson.fromJson(storedString, OpenApiContext.class);
            }
        }
        return specCache;

    }

    public void saveSpec(OpenApiContext spec) {
        synchronized (this) {
            keyValueStore.put(OpenApiContext.STORE_KEY, gson.toJson(spec));
            specCache = spec;
        }
    }

    public void deleteSpec() {
        synchronized (this) {
            specCache = null;
            keyValueStore.remove(OpenApiContext.STORE_KEY);
        }
    }

    public void clearCachedSpec() {
        synchronized (this) {
            specCache = null;
        }
    }

}
