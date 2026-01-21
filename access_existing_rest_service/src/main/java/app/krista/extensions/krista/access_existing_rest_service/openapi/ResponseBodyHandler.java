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

package app.krista.extensions.krista.access_existing_rest_service.openapi;

import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import app.krista.ksdk.files.FileHandle;
import app.krista.ksdk.files.FileRepository;
import org.jvnet.hk2.annotations.Service;

import static app.krista.extensions.krista.access_existing_rest_service.Constants.*;

/**
 * Handles the processing of response bodies from an existing REST service,
 * including checking file types and creating file handles for specific content types.
 */
@Service
public class ResponseBodyHandler {

    private final FileRepository fileRepository;

    /**
     * Constructs a new ResponseBodyHandler with the specified FileRepository.
     *
     * @param fileRepository The repository used to create new files.
     */
    @Inject
    public ResponseBodyHandler(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * Checks if a given MIME type corresponds to a supported file type.
     *
     * @param mimeType The MIME type to check.
     * @return true if the MIME type represents a supported file type, false otherwise.
     */
    public boolean isFileType(String mimeType) {
        return Set.of(APPLICATION_PDF,
                IMAGE_JPEG, IMAGE_PNG, IMAGE_GIF,
                AUDIO_MPEG, AUDIO_WAV, VIDEO_AVI, VIDEO_MP4,
                APPLICATION_OCTET_STREAM).contains(mimeType);
    }

    /**
     * Creates a new FileHandle based on the content type.
     *
     * @param contentType The content type for which a FileHandle should be created.
     * @return A new FileHandle or null if the content type is not supported.
     */
    public FileHandle getFileHandle(String contentType) {
        switch (contentType) {
            case TEXT_PLAIN:
                return fileRepository.createNewFileByName(UUID.randomUUID() + ".txt");
            case APPLICATION_PDF:
                return fileRepository.createNewFileByName(UUID.randomUUID() + ".pdf");
            case IMAGE_GIF:
                return fileRepository.createNewFileByName(UUID.randomUUID() + ".gif");
            case IMAGE_PNG:
                return fileRepository.createNewFileByName(UUID.randomUUID() + ".png");
            case IMAGE_JPEG:
                return fileRepository.createNewFileByName(UUID.randomUUID() + ".jpeg");
            case AUDIO_MPEG:
                return fileRepository.createNewFileByName(UUID.randomUUID() + ".mpeg");
            case AUDIO_WAV:
                return fileRepository.createNewFileByName(UUID.randomUUID() + ".wav");
            case APPLICATION_OCTET_STREAM:
                return fileRepository.createNewFileByName(UUID.randomUUID() + ".bin");
            case APPLICATION_XML:
                return fileRepository.createNewFileByName(UUID.randomUUID() + ".xml");
            case VIDEO_AVI:
                return fileRepository.createNewFileByName(UUID.randomUUID() + ".avi");
            case VIDEO_MP4:
                return fileRepository.createNewFileByName(UUID.randomUUID() + ".mp4");
            default:
                return null;
        }
    }

}
