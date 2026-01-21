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

package app.krista.extensions.krista.access_existing_rest_service.data;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Pair<K, V> {

    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <K, V> Set<Pair<K, V>> setOf(Map<K, V> map) {
        Set<Pair<K, V>> set = new LinkedHashSet<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            set.add(Pair.of(entry.getKey(), entry.getValue()));
        }

        return set;
    }

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    public static <K, V> Pair<K, V> create(Map.Entry<K, V> entry) {
        return Pair.of(entry.getKey(), entry.getValue());
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pair) {
            Pair<?, ?> that = (Pair<?, ?>) o;
            return Objects.equals(key, that.key) && Objects.equals(value, that.value);
        }
        return false;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

}
