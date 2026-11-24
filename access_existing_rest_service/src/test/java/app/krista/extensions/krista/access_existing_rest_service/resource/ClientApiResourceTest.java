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

package app.krista.extensions.krista.access_existing_rest_service.resource;

import java.io.IOException;
import javax.ws.rs.core.Response;
import app.krista.extensions.krista.access_existing_rest_service.TestUtils;
import app.krista.extensions.krista.access_existing_rest_service.resource.dto.SpecView;
import org.glassfish.hk2.api.ServiceLocator;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientApiResourceTest {

    @Test
    public void getSpecResource() throws IOException {
        ServiceLocator serviceLocator = TestUtils.createDefaultServiceLocator();
        SpecApiResource specApiResource = serviceLocator.create(SpecApiResource.class);
        String petStore = TestUtils.load("petstore.json");
        Response response = specApiResource.uploadSpec(petStore);
        response.close();
        ClientApiResource clientApiResource = serviceLocator.create(ClientApiResource.class);
        SpecView specView = clientApiResource.getSpecView();
        assertTrue(specView.methods.size() >= 13);
    }

}