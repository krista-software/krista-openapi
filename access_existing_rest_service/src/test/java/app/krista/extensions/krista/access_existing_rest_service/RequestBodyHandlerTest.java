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

import app.krista.ksdk.files.FileRepository;
import com.google.gson.Gson;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RequestBodyHandlerTest {

    private RequestBodyHandler requestBodyHandler;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private Gson gson;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        requestBodyHandler = new RequestBodyHandler(fileRepository, gson);
    }

    @Test
    public void testIsFileContentType() {
        Content content = mock(Content.class);
        when(content.containsKey(Constants.APPLICATION_OCTET_STREAM)).thenReturn(true);

        boolean result = requestBodyHandler.isFileContentType(content);

        verify(content).containsKey(Constants.APPLICATION_OCTET_STREAM);
        verifyNoMoreInteractions(content);
        verifyNoInteractions(fileRepository, gson);
        assert result;
    }

}