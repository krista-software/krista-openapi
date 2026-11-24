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
import java.util.HashMap;
import java.util.Map;
import app.krista.ksdk.files.FileHandle;
import app.krista.ksdk.files.FileRepository;
import app.krista.model.base.File;

/**
 * A fake implementation of FileRepository for testing purposes.
 */
public class FakeFileRepository implements FileRepository {

    private Map<String, FakeFileHandle> fileMap = new HashMap<>();

    @Override
    public FileHandle addFile(FileHandle fileHandle) throws IOException {
        fileMap.put(fileHandle.getFileName(), (FakeFileHandle) fileHandle);
        return fileHandle;
    }

    @Override
    public void removeFile(File file) {
        fileMap.remove(file.getFileName());
    }

    @Override
    public FileHandle getFile(File file) {
        return fileMap.get(file.getFileName());
    }

    public FileHandle getFileByName(String fileName) {
        return fileMap.get(fileName);
    }

    public FileHandle getOrCreateFileByName(String fileName) {
        return fileMap.computeIfAbsent(fileName, k -> new FakeFileHandle(new File(0, fileName, "", "")));
    }

    @Override
    public FileHandle createNewFileByName(String fileName) {
        FakeFileHandle fileHandle = new FakeFileHandle(new File(0, fileName, "", ""));
        fileMap.put(fileName, fileHandle);
        return fileHandle;
    }

    @Override
    public FileHandle createNewFile() {
        FakeFileHandle fileHandle = new FakeFileHandle(new File(0, "newFile", "", ""));
        fileMap.put("newFile", fileHandle);
        return fileHandle;
    }

    @Override
    public FileHandle createTemporaryFile() {
        FakeFileHandle fileHandle = new FakeFileHandle(new File(0, "tempFile", "", ""));
        fileMap.put("tempFile", fileHandle);
        return fileHandle;
    }

    @Override
    public java.util.Set<String> getBlackListedFileExtensions() {
        return java.util.Set.of();
    }

}