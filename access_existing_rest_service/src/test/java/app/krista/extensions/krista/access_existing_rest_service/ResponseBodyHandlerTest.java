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

import app.krista.extensions.krista.access_existing_rest_service.fake.FakeFileRepository;
import app.krista.extensions.krista.access_existing_rest_service.openapi.ResponseBodyHandler;
import app.krista.ksdk.files.FileHandle;
import app.krista.ksdk.files.FileRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseBodyHandlerTest {

    private ResponseBodyHandler responseBodyHandler;

    @Before
    public void setUp() {
        FileRepository fileRepository = new FakeFileRepository();
        responseBodyHandler = new ResponseBodyHandler(fileRepository);
    }

    @Test
    public void isFileTypeTest() {
        assertTrue(responseBodyHandler.isFileType("application/pdf"));
        assertTrue(responseBodyHandler.isFileType("image/jpeg"));
        assertTrue(responseBodyHandler.isFileType("image/png"));
        assertTrue(responseBodyHandler.isFileType("image/gif"));
        assertTrue(responseBodyHandler.isFileType("audio/mpeg"));
        assertTrue(responseBodyHandler.isFileType("audio/wav"));
        assertTrue(responseBodyHandler.isFileType("video/avi"));
        assertTrue(responseBodyHandler.isFileType("video/mp4"));
        assertTrue(responseBodyHandler.isFileType("application/octet-stream"));
        assertFalse(responseBodyHandler.isFileType("text/html"));
    }

    @Test
    public void getFileHandleTest() {
        FileHandle fileHandle = responseBodyHandler.getFileHandle("application/pdf");
        assertNotNull(fileHandle);
        assertTrue(fileHandle.getFileName().endsWith(".pdf"));

        fileHandle = responseBodyHandler.getFileHandle("image/jpeg");
        assertNotNull(fileHandle);
        assertTrue(fileHandle.getFileName().endsWith(".jpeg"));

        fileHandle = responseBodyHandler.getFileHandle("image/png");
        assertNotNull(fileHandle);
        assertTrue(fileHandle.getFileName().endsWith(".png"));

        fileHandle = responseBodyHandler.getFileHandle("image/gif");
        assertNotNull(fileHandle);
        assertTrue(fileHandle.getFileName().endsWith(".gif"));

        fileHandle = responseBodyHandler.getFileHandle("audio/mpeg");
        assertNotNull(fileHandle);
        assertTrue(fileHandle.getFileName().endsWith(".mpeg"));

        fileHandle = responseBodyHandler.getFileHandle("audio/wav");
        assertNotNull(fileHandle);
        assertTrue(fileHandle.getFileName().endsWith(".wav"));

        fileHandle = responseBodyHandler.getFileHandle("video/avi");
        assertNotNull(fileHandle);
        assertTrue(fileHandle.getFileName().endsWith(".avi"));

        fileHandle = responseBodyHandler.getFileHandle("video/mp4");
        assertNotNull(fileHandle);
        assertTrue(fileHandle.getFileName().endsWith(".mp4"));

        fileHandle = responseBodyHandler.getFileHandle("application/octet-stream");
        assertNotNull(fileHandle);
        assertTrue(fileHandle.getFileName().endsWith(".bin"));

        fileHandle = responseBodyHandler.getFileHandle("text/html");
        assertNull(fileHandle);
    }

}