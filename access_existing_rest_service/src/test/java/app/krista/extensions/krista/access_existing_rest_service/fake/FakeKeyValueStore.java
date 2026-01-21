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

package app.krista.extensions.krista.access_existing_rest_service.fake;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import app.krista.extensions.util.KeyValueStore;
import org.jvnet.hk2.annotations.ContractsProvided;
import org.jvnet.hk2.annotations.Service;

@Service
@ContractsProvided(KeyValueStore.class)
public class FakeKeyValueStore implements KeyValueStore {

    Map<String, Object> r = new HashMap<>();

    @Override
    public Object get(String s) {
        return r.get(s);
    }

    @Override
    public void remove(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void put(String s, Object o) {
        r.put(s, o);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getKeys() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        throw new UnsupportedOperationException();
    }

}