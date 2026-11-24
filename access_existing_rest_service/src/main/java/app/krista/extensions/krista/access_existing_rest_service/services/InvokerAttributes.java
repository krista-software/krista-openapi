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

package app.krista.extensions.krista.access_existing_rest_service.services;

import javax.inject.Inject;
import app.krista.extension.executor.Invoker;
import app.krista.extensions.krista.access_existing_rest_service.Constants;
import org.jvnet.hk2.annotations.Service;

@Service
public class InvokerAttributes {

    private final Invoker invoker;

    @Inject
    public InvokerAttributes(Invoker invoker) {
        this.invoker = invoker;
    }

    public String getUserName() {
        return (String) invoker.getAttributes().get(Constants.USERNAME);
    }
    public String getPassword() {
        return (String) invoker.getAttributes().get(Constants.PASSWORD);
    }

    public String getServerAddress() {
        return (String) invoker.getAttributes().get(Constants.SERVER_ADDRESS);
    }

    public String getApiKey() {
        return (String) invoker.getAttributes().get(Constants.API_KEY);
    }

    public String getTestPath() {
        return (String) invoker.getAttributes().get(Constants.TEST_PATH);
    }

}
