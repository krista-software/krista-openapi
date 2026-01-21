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

package app.krista.extensions.krista.access_existing_rest_service.auth;

import app.krista.extensions.krista.access_existing_rest_service.data.RequestInfo;
import com.kristasoft.common.jaxrs.impl.BasicAuthorizationHeader;

public class BasicAuthAuthenticator implements Authenticator {
    private final String authorizationHeader;

    public BasicAuthAuthenticator(String username, String password) {
        this.authorizationHeader = BasicAuthorizationHeader.create(username, password).getAuthorization();
    }

    @Override
    public void authenticate(AuthenticationInfo<?> authenticationInfo, RequestInfo.Builder requestInfoBuilder) {
        requestInfoBuilder.header("Authorization", "Basic " + this.authorizationHeader);
    }

}