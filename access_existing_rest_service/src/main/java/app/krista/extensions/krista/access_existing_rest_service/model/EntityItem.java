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

import java.util.Map;
import app.krista.model.entity.EntityAttributeField;
import io.swagger.v3.oas.models.media.Schema;

public class EntityItem {

    private final EntityInfo entityInfo;
    private final Schema<?> schema;
    private final Map<String, EntityAttributeField> fieldMap;

    public EntityItem(EntityInfo entityInfo, Schema<?> schema, Map<String, EntityAttributeField> fieldMap) {
        this.entityInfo = entityInfo;
        this.schema = schema;
        this.fieldMap = fieldMap;
    }

    public String getPrimaryKey() {
        return entityInfo.getPrimaryKey();
    }

    public String getEntityId() {
        return entityInfo.getEntityId();
    }

    public EntityInfo getEntityInfo() {
        return entityInfo;
    }

    public Schema<?> getSchema() {
        return schema;
    }

    public Map<String, EntityAttributeField> getFieldMap() {
        return fieldMap;
    }

}

