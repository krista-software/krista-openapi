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

import app.krista.extension.request.Request;
import app.krista.ksdk.context.ExecutionMode;
import app.krista.ksdk.context.RequestContext;

public class RequestContextImpl implements RequestContext {

    private final String workspaceId;
    private final String invokerId;
    private final Request request;
    private final boolean invokeAsUser;
    private final String proxyInvokerId;
    private final ExecutionMode executionMode;

    private RequestContextImpl(String workspaceId, String invokerId, Request request, boolean invokeAsUser,
            String proxyInvokerId, ExecutionMode executionMode) {
        this.workspaceId = workspaceId;
        this.invokerId = invokerId;
        this.request = request;
        this.invokeAsUser = invokeAsUser;
        this.proxyInvokerId = proxyInvokerId;
        this.executionMode = executionMode;
    }

    public static RequestContext createModeSpecificContext(RequestContext requestContext, ExecutionMode executionMode) {
        return new RequestContextImpl(requestContext.getWorkspaceId(), requestContext.getInvokerId(),
                requestContext.getRequest(), requestContext.invokeAsUser(), requestContext.getProxyInvokerId(),
                executionMode);
    }

    @Override
    public String getWorkspaceId() {
        return workspaceId;
    }

    @Override
    public String getInvokerId() {
        return invokerId;
    }

    @Override
    public String getProxyInvokerId() {
        return proxyInvokerId;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public boolean invokeAsUser() {
        return invokeAsUser;
    }

    @Override
    public ExecutionMode getExecutionMode() {
        return executionMode;
    }

}
