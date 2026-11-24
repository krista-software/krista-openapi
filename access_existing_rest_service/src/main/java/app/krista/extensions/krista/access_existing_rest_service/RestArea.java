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

package app.krista.extensions.krista.access_existing_rest_service;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.*;
import app.krista.extensions.krista.access_existing_rest_service.impl.TokenImpl;
import app.krista.ksdk.context.ConversationContext;
import javax.inject.Inject;

@Domain(id = "catEntryDomain_c2394eb0-02d9-4916-a3dc-909b1c631ea0",
        name = "SDK",
        ecosystemId = "catEntryEcosystem_d3b05047-07b0-4b06-95a3-9fb8f7f608d9",
        ecosystemName = "Krista",
        ecosystemVersion = "9efe1be2-51b9-4689-ab28-653c1a912268")
public class RestArea {

    private final TokenImpl tokenImpl;
    private final ConversationContext context;
    @Inject
    public RestArea(TokenImpl tokenImpl, ConversationContext context) {
        this.context = context;
        this.tokenImpl = tokenImpl;
    }

    @CatalogRequest(
            id = "localDomainRequest_dfb247a0-4869-4883-964d-6e2c04dc2624",
            name = "Delete Token",
            description = "Delete Token",
            area = "rest",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Text(name = "Status", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String deleteToken() {
        String executionId = context.getExecution().getExecutionId();
        return tokenImpl.deleteToken(executionId);
    }

    @CatalogRequest(
            id = "localDomainRequest_f55d782c-1fe5-4e70-bb26-c7e247cfca54",
            name = "Get Token",
            description = "Get the stored token",
            area = "rest",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Text(name = "Token", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String getToken() {
        String executionId = context.getExecution().getExecutionId();
        return tokenImpl.getToken(executionId);
    }

    @CatalogRequest(
            id = "localDomainRequest_5c43dfda-a0ec-4334-98b6-aa8c5c3c8c28",
            name = "Store Token",
            description = "With this request you can store the token and this will be used instead of Setup page token",
            area = "rest",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Text(name = "Status", required = false, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {})
    public String storeToken(
            @Field.Text(name = "Token Value", required = true, attributes = {@Attribute(name = "visualWidth", value = "S")}, options = {}) String tokenValue) {
        String executionId = context.getExecution().getExecutionId();
        return tokenImpl.storeToken(tokenValue, executionId);
    }

}