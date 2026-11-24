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

import java.util.List;
import com.google.gson.annotations.JsonAdapter;

@JsonAdapter(BindingAdapter.class)
public abstract class Binding {

    private final Type type;

    public Binding(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        WRAPPER, DIRECT
    }

    public static class WrapperBinding extends Binding {

        private final String wrapperName;

        public WrapperBinding(String wrapperName) {
            super(Type.WRAPPER);
            this.wrapperName = wrapperName;
        }

        public String getWrapperName() {
            return wrapperName;
        }

    }

    public static class DirectBinding extends Binding {

        private final List<String> fieldNames;

        public DirectBinding(List<String> fieldNames) {
            super(Type.DIRECT);
            this.fieldNames = fieldNames;
        }

        public List<String> getFieldNames() {
            return fieldNames;
        }

    }

}
