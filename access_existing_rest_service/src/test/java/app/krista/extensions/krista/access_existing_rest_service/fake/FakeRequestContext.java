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

import app.krista.extension.request.Request;
import app.krista.ksdk.context.ExecutionMode;
import app.krista.ksdk.context.RequestContext;
import org.jvnet.hk2.annotations.ContractsProvided;
import org.jvnet.hk2.annotations.Service;

@Service
@ContractsProvided(RequestContext.class)
public class FakeRequestContext implements RequestContext {

    @Override
    public String getWorkspaceId() {
        return "fake_workspace_id";
    }

    @Override
    public String getInvokerId() {
        return "fake_invoker_id";
    }

    @Override
    public String getProxyInvokerId() {
        return "fake_proxy_invoker_id";
    }

    @Override
    public Request getRequest() {
        return null;
    }

    @Override
    public boolean invokeAsUser() {
        return false;
    }

    @Override
    public ExecutionMode getExecutionMode() {
        return ExecutionMode.LIVE;
    }

}
