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

import javax.ws.rs.core.Response;
import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.*;

public class KristaExceptionMapperTest {

    private final KristaExceptionMapper kristaExceptionMapper = new KristaExceptionMapper(new Gson());

    @Test
    public void checkResponseStatus() {
        Response response = kristaExceptionMapper.toResponse(new Exception());
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    public void checkKnownException() {
        Response response = kristaExceptionMapper.toResponse(new KnownException("test", new Exception()));
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        String entity = (String) response.getEntity();
        ExceptionMessageDto exceptionMessageDto = new Gson().fromJson(entity, ExceptionMessageDto.class);
        assertEquals("test", exceptionMessageDto.errorMessage);
    }

}