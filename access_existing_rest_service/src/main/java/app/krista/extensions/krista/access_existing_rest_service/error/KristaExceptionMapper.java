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

package app.krista.extensions.krista.access_existing_rest_service.error;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import com.google.gson.Gson;

public class KristaExceptionMapper implements ExceptionMapper<Exception> {

    private final Gson gson;

    @Inject
    public KristaExceptionMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Response toResponse(Exception exception) {
        String stackTrace = getStackTrace(exception);
        if (exception instanceof KnownException) {
            return handleKnownException((KnownException) exception, stackTrace);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(stackTrace)
                .build();
    }

    private String getStackTrace(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }

    private Response handleKnownException(KnownException exception, String stackTrace) {
        ExceptionMessageDto exceptionMessageDto = new ExceptionMessageDto(exception.getMessage(), stackTrace);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(gson.toJson(exceptionMessageDto))
                .build();
    }

}