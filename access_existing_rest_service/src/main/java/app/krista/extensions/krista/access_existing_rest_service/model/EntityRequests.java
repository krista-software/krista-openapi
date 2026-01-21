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

package app.krista.extensions.krista.access_existing_rest_service.model;

public class EntityRequests {

    public final String create;
    public final String update;
    public final String get;
    public final String search;
    public final String delete;
    public final String lookup;
    public final String supports;
    public final String contains;
    public final String count;

    public EntityRequests(String create, String update, String get, String search, String delete, String lookup,
            String supports, String contains, String count) {
        this.create = create;
        this.update = update;
        this.get = get;
        this.search = search;
        this.delete = delete;
        this.lookup = lookup;
        this.supports = supports;
        this.contains = contains;
        this.count = count;
    }

    @SuppressWarnings("unused")
    public static class Builder {

        private String create;
        private String update;
        private String get;
        private String search;
        private String delete;
        private String lookup;
        private String supports;
        private String contains;
        private String count;

        public Builder create(String create) {
            this.create = create;
            return this;
        }

        public Builder update(String update) {
            this.update = update;
            return this;
        }

        public Builder get(String get) {
            this.get = get;
            return this;
        }

        public Builder search(String search) {
            this.search = search;
            return this;
        }

        public Builder delete(String delete) {
            this.delete = delete;
            return this;
        }

        public Builder lookup(String lookup) {
            this.lookup = lookup;
            return this;
        }

        public Builder supports(String supports) {
            this.supports = supports;
            return this;
        }

        public Builder contains(String contains) {
            this.contains = contains;
            return this;
        }

        public Builder count(String count) {
            this.count = count;
            return this;
        }

        public EntityRequests build() {
            return new EntityRequests(create, update, get, search, delete, lookup, supports, contains, count);
        }

    }

}
