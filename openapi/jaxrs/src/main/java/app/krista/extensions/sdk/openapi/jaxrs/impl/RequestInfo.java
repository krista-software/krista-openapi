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

package app.krista.extensions.sdk.openapi.jaxrs.impl;


import app.krista.extensions.sdk.openapi.util.Pair;

import java.util.List;
import java.util.Map;

public class RequestInfo {
    public final Map<String, Object> queryParameters;
    public final Map<String, Object> pathParameters;
    public final Map<String, Object> headers;
    public final Object body;
    public final String serverAddress;
    public final List<Pair<String, String>> cookies;

    public RequestInfo(Map<String, Object> queryParameters, Map<String, Object> pathParameters, Object body, Map<String, Object> headers, String serverAddress, List<Pair<String, String>> cookies) {
        this.queryParameters = queryParameters;
        this.pathParameters = pathParameters;
        this.body = body;
        this.headers = headers;
        this.serverAddress = serverAddress;
        this.cookies = cookies;
    }

}
