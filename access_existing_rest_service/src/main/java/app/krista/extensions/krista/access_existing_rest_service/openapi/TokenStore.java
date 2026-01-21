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

import app.krista.extensions.util.KeyValueStore;
import org.jvnet.hk2.annotations.Service;
import javax.inject.Inject;

@Service
public class TokenStore {

    private final KeyValueStore keyValueStore;

    @Inject
    public TokenStore(KeyValueStore keyValueStore) {
        this.keyValueStore = keyValueStore;
    }

    public void put(String key, String token) {
        keyValueStore.put(key, token);
    }

    public String get(String key) {
        return (String) keyValueStore.get(key);
    }

    public void remove(String key) {
        keyValueStore.remove(key);
    }

}
