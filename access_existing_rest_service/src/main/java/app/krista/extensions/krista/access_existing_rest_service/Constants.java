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

import java.util.List;
import javax.ws.rs.core.MediaType;
import app.krista.extensions.krista.access_existing_rest_service.model.DomainInfo;

public final class Constants {

    public static final String SERVER_ADDRESS = "Server address";
    public static final String TEST_PATH = "Test URL Path";
    public static final String API_KEY = "API Key";
    public static final String BASIC_AUTH = "Basic Auth";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    public static final String AUTH_TYPE = "AuthType";
    public static final String X_KRISTA_ENTITY = "x-krista-entity";
    public static final String KRISTA_TEXT_FIELD = "com.krista.fields.Text";
    public static final String KRISTA_NUMBER_FIELD = "com.krista.fields.Number";
    public static final String KRISTA_SWITCH_FIELD = "com.krista.fields.Switch";
    public static final String KRISTA_PICKONE_FIELD = "com.krista.fields.PickOne";
    public static final String KRISTA_LIST_FIELD = "com.krista.fields.List";
    public static final String KRISTA_FILE_FIELD = "com.krista.fields.File";
    public static final String APPLICATION_PDF = "application/pdf";
    public static final String MULTIPART_FORM_DATA = MediaType.MULTIPART_FORM_DATA;
    public static final String APPLICATION_OCTET_STREAM = MediaType.APPLICATION_OCTET_STREAM;
    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String IMAGE_PNG = "image/png";
    public static final String IMAGE_GIF = "image/gif";
    public static final String AUDIO_MPEG = "audio/mpeg";
    public static final String AUDIO_WAV = "audio/wav";
    public static final String VIDEO_MP4 = "video/mp4";
    public static final String VIDEO_AVI = "video/avi";
    public static final String APPLICATION_XML = MediaType.APPLICATION_XML;
    public static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;
    public static final String APPLICATION_FORM_URLENCODED = MediaType.APPLICATION_FORM_URLENCODED;
    public static final String TEXT_PLAIN = MediaType.TEXT_PLAIN;
    public static final String DOCUMENT = "Document";
    public static final String IMAGE_GALLERY = "Image gallery";
    public static final String CAMERA = "Camera";
    public static final String CONTACT = "Contact";
    public static final String AUDIO = "Audio";
    public static final String HEADER = "header";
    public static final String PATH = "path";
    public static final String QUERY = "query";
    public static final String DEFAULT_AREA_NAME = "rest";
    public static final String OBJECT = "object";
    // Extension Keys
    public static final String X_KRISTA_FIELD = "x-krista-field";
    public static final String X_KRISTA_DOMAIN = "x-krista-domain";
    public static final String X_KRISTA_AREA_NAME = "x-krista-area-name";
    public static final String X_KRISTA_REQUEST_ID = "x-krista-request-id";
    public static final String X_KRISTA_REQUEST_TYPE = "x-krista-request-type";
    public static final String X_KRISTA_ENTITY_REQUEST = "x-krista-entity-request";
    public static final String X_KRISTA_ENTITY_FIELD_PROPERTIES = "x-krista-entity-field-properties";

    // Krista Field Logical Names
    // Text
    public static final String TEXT_FIELD_LOGICAL_NAME = "com.krista.fields.Text";
    public static final String PARAGRAPH_LOGICAL_NAME = "com.krista.fields.Paragraph";
    public static final String PROPER_NOUN_LOGICAL_NAME = "com.krista.fields.ProperNoun";
    public static final String PERSON_LOGICAL_NAME = "com.krista.fields.Person";
    public static final String IDENTIFIER_LOGICAL_NAME = "com.krista.fields.Identifier";
    public static final String RICH_TEXT_LOGICAL_NAME = "com.krista.fields.RichText";
    // Others
    public static final String PICK_ONE_LOGICAL_NAME = "com.krista.fields.PickOne";
    public static final String SWITCH_LOGICAL_NAME = "com.krista.fields.Switch";
    public static final String LABEL_LOGICAL_NAME = "com.krista.fields.Label";
    public static final String CHECKBOXES_LOGICAL_NAME = "com.krista.fields.Checkboxes";
    public static final String PHONE_LOGICAL_NAME = "com.krista.fields.Phone";
    public static final String EMAIL_LOGICAL_NAME = "com.krista.fields.Email";
    public static final String RATING_LOGICAL_NAME = "com.krista.fields.Rating";
    // Calculation has no valueType
    public static final String CALCULATION_LOGICAL_NAME = "com.krista.fields.Calculation";
    public static final String FILE_LOGICAL_NAME = "com.krista.fields.File";
    public static final String CAMERA_LOGICAL_NAME = "com.krista.fields.Camera";
    // Numbers
    public static final String NUMBER_LOGICAL_NAME = "com.krista.fields.Number";
    public static final String CURRENCY_LOGICAL_NAME = "com.krista.fields.Currency";
    public static final String PERCENTAGE_LOGICAL_NAME = "com.krista.fields.Percentage";
    public static final String UNIT_LOGICAL_NAME = "com.krista.fields.Unit";
    // Location/Date
    public static final String STREET_LOGICAL_NAME = "com.krista.fields.Street";
    public static final String STREET2_LOGICAL_NAME = "com.krista.fields.Street2";
    public static final String CITY_LOGICAL_NAME = "com.krista.fields.City";
    public static final String STATE_LOGICAL_NAME = "com.krista.fields.State";
    public static final String POSTAL_CODE_LOGICAL_NAME = "com.krista.fields.PostalCode";
    public static final String COUNTRY_LOGICAL_NAME = "com.krista.fields.Country";
    public static final String DATE_RANGE_LOGICAL_NAME = "com.krista.fields.DateRange";
    public static final String DATE_LOGICAL_NAME = "com.krista.fields.Date";
    public static final String TIME_LOGICAL_NAME = "com.krista.fields.Time";
    public static final String LOCATION_LOGICAL_NAME = "com.krista.fields.Location";
    public static final String DATE_TIME_RANGE_LOGICAL_NAME = "com.krista.fields.DateTimeRange";
    public static final String ADDRESS_LOGICAL_NAME = "com.krista.fields.Address";
    // Composites
    public static final String ENTITY_REFERENCE_LOGICAL_NAME = "com.krista.fields.EntityReference";
    public static final String UNKNOWN_ENTITY_LOGICAL_NAME = "com.krista.fields.Composite"; // Unknown Entity
    public static final String LIST_LOGICAL_NAME = "com.krista.fields.List";
    public static final String MULTI_FIELD_LOGICAL_NAME = "com.krista.fields.MultiField";
    // Common Schema types
    public static final DomainInfo DEFAULT_DOMAIN_INFO = new DomainInfo(
            "catEntryEcosystem_d3b05047-07b0-4b06-95a3-9fb8f7f608d9",
            "Krista",
            "f11e6c89-11b1-4264-ac9c-a7f174fe1e6c",
            "catEntryDomain_c2394eb0-02d9-4916-a3dc-909b1c631ea0",
            "SDK",
            "cat_7149cea8-244c-4059-8a48-6236798c92b7"
    );
    public static final String EXTENSION_DESCRIPTION_ID = "catEntryExtn_b01bf266-e184-4d2f-ba8d-edd497f26134";
    public static final String INCLUDE_IN_SUMMARY = "includeInSummary";
    public static final String AVAILABLE_SEARCHES = "availableSearches";
    public static final String IS_IN_UNIQUE = "isInUnique";
    public static List<String> compatibleVersions = List.of("1.0.2");
    public static String X_KRISTA_VERSION = "x-krista-version";
    public static final String TOKEN = "Token";

    private Constants() {
    }

}
