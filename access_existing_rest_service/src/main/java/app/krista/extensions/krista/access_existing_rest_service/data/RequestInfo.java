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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestInfo {

    private final Map<String, Object> queryParameters;
    private final Map<String, Object> pathParameters;
    private final Map<String, Object> headers;
    private final Map<String, String> cookies;
    private final Object body;
    private final String serverAddress;
    private final String pathTemplate;
    private final String httpMethod;
    private final String mediaType;

    public RequestInfo(Map<String, Object> queryParameters, Map<String, Object> pathParameters,
            Map<String, Object> headers, Map<String, String> cookies, Object body, String serverAddress,
            String pathTemplate, String httpMethod, String mediaType) {
        this.queryParameters = queryParameters;
        this.pathParameters = pathParameters;
        this.body = body;
        this.headers = headers;
        this.cookies = cookies;
        this.serverAddress = serverAddress;
        this.pathTemplate = pathTemplate;
        this.httpMethod = httpMethod;
        this.mediaType = mediaType;
    }

    public Map<String, Object> getQueryParameters() {
        return queryParameters;
    }

    public Map<String, Object> getPathParameters() {
        return pathParameters;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Object getBody() {
        return body;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getPathTemplate() {
        return pathTemplate;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getMediaType() {
        return mediaType;
    }

    @Override
    public String toString() {
        return "RequestInfo{" +
                "queryParameters=" + queryParameters +
                ", pathParameters=" + pathParameters +
                ", headers=" + headers +
                ", cookies=" + cookies +
                ", body=" + body +
                ", serverAddress='" + serverAddress + '\'' +
                ", pathTemplate='" + pathTemplate + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", mediaType='" + mediaType + '\'' +
                '}';
    }

    public static class Builder {

        private final Map<String, Object> queryParameters = new HashMap<>();
        private final Map<String, Object> pathParameters = new HashMap<>();
        private final Map<String, Object> headers = new HashMap<>();
        private final Map<String, String> cookies = new HashMap<>();
        private Object body;
        private String serverAddress;
        private String pathTemplate;
        private String httpMethod;
        private String mediaType;

        public Builder queryParameter(String name, Object value) {
            this.queryParameters.put(name, value);
            return this;
        }

        public Builder pathParameter(String name, Object value) {
            this.pathParameters.put(name, value);
            return this;
        }

        public Builder header(String name, Object value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder cookie(String name, String value) {
            this.cookies.put(name, value);
            return this;
        }

        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        public Builder serverAddress(String serverAddress) {
            this.serverAddress = serverAddress;
            return this;
        }

        public Builder pathTemplate(String pathTemplate) {
            this.pathTemplate = pathTemplate;
            return this;
        }

        public Builder httpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder mediaType(String mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public RequestInfo build() {
            return new RequestInfo(Collections.unmodifiableMap(queryParameters),
                    Collections.unmodifiableMap(pathParameters), Collections.unmodifiableMap(headers),
                    Collections.unmodifiableMap(cookies), body, serverAddress, pathTemplate, httpMethod, mediaType);
        }

    }

}