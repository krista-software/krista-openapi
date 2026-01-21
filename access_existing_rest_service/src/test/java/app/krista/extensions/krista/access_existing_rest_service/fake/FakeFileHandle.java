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

import java.io.IOException;
import java.io.InputStream;
import app.krista.extension.authorization.AuthorizationException;
import app.krista.ksdk.files.FileHandle;
import app.krista.model.base.File;

public class FakeFileHandle implements FileHandle {

    private File file;
    private InputStream content;

    public FakeFileHandle(File file) {
        this.file = file;
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public String getFileName() {
        return this.file.getFileName();
    }

    @Override
    public InputStream getContent() throws AuthorizationException, IOException {
        return this.content;
    }

    @Override
    public void setContent(InputStream content) throws AuthorizationException, IOException {
        this.content = content;
    }

    @Override
    public void delete() throws AuthorizationException {
        this.file = null;
        this.content = null;
    }

    @Override
    public void close() {
        try {
            if (this.content != null) {
                this.content.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateExpiry(Integer expirySeconds) {
        // No-op for fake implementation
    }

}