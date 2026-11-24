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

package app.krista.extensions.krista.access_existing_rest_service.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import app.krista.extension.executor.ExtensionCatalogRequest;
import app.krista.extension.executor.Invoker;
import app.krista.extension.executor.impl.DefaultExtensionCatalogRequest;
import app.krista.extensions.krista.access_existing_rest_service.Constants;
import app.krista.extensions.krista.access_existing_rest_service.OpenApiExtension;
import app.krista.extensions.krista.access_existing_rest_service.TestUtils;
import app.krista.extensions.krista.access_existing_rest_service.fake.FakeFileHandle;
import app.krista.extensions.krista.access_existing_rest_service.model.CatalogRequestInfo;
import app.krista.extensions.krista.access_existing_rest_service.model.JaxrsRequestItem;
import app.krista.extensions.krista.access_existing_rest_service.model.OpenApiContext;
import app.krista.extensions.krista.access_existing_rest_service.model.RequestInfo;
import app.krista.extensions.krista.access_existing_rest_service.openapi.SpecParser;
import app.krista.extensions.krista.access_existing_rest_service.openapi.SpecStore;
import app.krista.extensions.krista.access_existing_rest_service.openapi.TokenStore;
import app.krista.ksdk.context.ConversationContext;
import app.krista.ksdk.files.FileRepository;
import app.krista.ksdk.workspace.ConversationExecution;
import app.krista.model.base.File;
import com.google.gson.Gson;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static app.krista.extensions.krista.access_existing_rest_service.Constants.*;
import static org.junit.Assert.*;

public class KristaSpecStoreTest {

    private ServiceLocator serviceLocator;
    private SpecParser specParser;
    private Invoker invoker;
    private OpenApiExtension openApiExtension;
    private SpecStore specStore;

    @Before
    public void setUp() {
        serviceLocator = TestUtils.createDefaultServiceLocator();
        specParser = serviceLocator.getService(SpecParser.class);
        invoker = serviceLocator.getService(Invoker.class);
        openApiExtension = serviceLocator.getService(OpenApiExtension.class);
        specStore = serviceLocator.getService(SpecStore.class);
    }

    @Test
    public void createSpec() throws IOException {
        var fileNames = new String[]{"petstore.json", "byom.json", "geoip.json", "pdfblocks.json",
                "insurance-openapi.json"};
        for (var fileName : fileNames) {
            String s = TestUtils.load(fileName);
            OpenApiContext specFromString = specParser.parseSpec(s);
            assertNotNull(specFromString);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void kristaExtendedSpec() throws IOException {
        String s = TestUtils.load("insurance-openapi.json");
        OpenApiContext specFromString = specParser.parseSpec(s);
        assertNotNull(specFromString);
        Map<String, Object> map = serviceLocator.getService(Gson.class).fromJson(s, Map.class);
        Map<String, Object> paths = (Map<String, Object>) map.get("paths");
        assertEquals(paths.size(), specFromString.getRequestInfoMap().size());
    }

    @Test
    public void fullSpecDeserialize() throws IOException {
        String s = TestUtils.load("petstore.json");
        OpenApiContext specFromString = specParser.parseSpec(s);
        assertNotNull(specFromString);
        specStore.saveSpec(specFromString);
        specStore.clearCachedSpec();
        OpenApiContext currentSpec = specStore.getSpec();
        @SuppressWarnings("rawtypes")
        Schema schema = currentSpec.getRequestInfoMap()
                .values()
                .stream()
                .map(RequestInfo::getJaxrsRequestItem)
                .filter(c -> "/pet/findByStatus".equals(c.getPath()))
                .filter(c -> Objects.nonNull(c.getPathItem().getGet())).map(JaxrsRequestItem::getResponseSchema)
                .findFirst()
                .orElseThrow();
        Assert.assertTrue(schema instanceof ArraySchema);
    }

    @Test
    public void serviceLocatorTest() throws ClassNotFoundException {
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create(null);
        Class<?> gsonClass =
                Class.forName("app.krista.extensions.krista.access_existing_rest_service.json.GsonFactory");
        ServiceLocatorUtilities.addClasses(serviceLocator, true, gsonClass);
        Gson gson1 = serviceLocator.getService(Gson.class);
        assertNotNull(gson1);
        Gson gson2 = serviceLocator.getService(Gson.class);
        Assert.assertSame(gson1, gson2);
    }

    @Test
    public void testExecuteCatalogRequest() throws IOException {
        ConversationContext mockContext = Mockito.mock(ConversationContext.class);
        TokenStore mockTokenStore = Mockito.mock(TokenStore.class);
        ConversationExecution mockExecution = Mockito.mock(ConversationExecution.class);
        Mockito.when(mockContext.getExecution()).thenReturn(mockExecution);
        Mockito.when(mockExecution.getExecutionId()).thenReturn("executionId");
        Mockito.when(mockTokenStore.get("executionId" + TOKEN)).thenReturn("random");
        TestUtils.addService(serviceLocator, TokenStore.class, mockTokenStore);
        TestUtils.addConversationContext(serviceLocator, mockContext);
        TestUtils.addAttribute(serviceLocator, Constants.API_KEY, "random");
        TestUtils.addAttribute(serviceLocator, SERVER_ADDRESS, "https://petstore.swagger.io/v2/");
        OpenApiExtension openApiExtension = serviceLocator.getService(OpenApiExtension.class);
        String s = TestUtils.load("petstore.json");
        OpenApiContext specFromString = specParser.parseSpec(s);
        specStore.saveSpec(specFromString);
        assertNotNull(specFromString);
        CatalogRequestInfo catalogRequestInfo = specFromString.getRequestInfoMap()
                .values()
                .stream()
                .map(RequestInfo::getCatalogRequestInfo)
                .filter(e -> e.getRequestName().equals("Returns pet inventories by status"))
                .findFirst()
                .orElseThrow();
        String requestId = catalogRequestInfo.getRequestId();
        ExtensionCatalogRequest extensionCatalogRequest = new DefaultExtensionCatalogRequest<>(invoker.getExtension(),
                null,
                requestId, "",
                "",
                "",
                null,
                catalogRequestInfo.getInputFields(),
                catalogRequestInfo.getOutputFields());
        Map<String, Object> stringObjectMap = openApiExtension.executeCatalogRequest(extensionCatalogRequest, Map.of());
        Assert.assertNotNull(stringObjectMap);
    }

    @Test
    public void validateAttributesTest() {
        OpenApiExtension openApiExtension = serviceLocator.getService(OpenApiExtension.class);
        Map<String, Object> map = Map.of(
                SERVER_ADDRESS, "https://petstore.swagger.io/v2/",
                TEST_PATH, "/store/inventory"
        );
        openApiExtension.validateAttributes(map);

    }

    @Test
    public void fileApiTest() throws IOException {
        ConversationContext mockContext = Mockito.mock(ConversationContext.class);
        TokenStore mockTokenStore = Mockito.mock(TokenStore.class);
        ConversationExecution mockExecution = Mockito.mock(ConversationExecution.class);
        Mockito.when(mockContext.getExecution()).thenReturn(mockExecution);
        Mockito.when(mockExecution.getExecutionId()).thenReturn("executionId");
        Mockito.when(mockTokenStore.get("executionId" + TOKEN)).thenReturn("random");
        TestUtils.addService(serviceLocator, TokenStore.class, mockTokenStore);
        TestUtils.addConversationContext(serviceLocator, mockContext);
        TestUtils.addAttribute(serviceLocator, Constants.API_KEY, "random");
        TestUtils.addAttribute(serviceLocator, SERVER_ADDRESS, "https://petstore.swagger.io/v2/");
        String s = TestUtils.load("petstore.json");
        OpenApiContext specFromString = specParser.parseSpec(s);
        assertNotNull(specFromString);
        specStore.saveSpec(specFromString);
        specStore.clearCachedSpec();
        OpenApiContext currentSpec = specStore.getSpec();
        assertNotNull(currentSpec);
        String requestId = currentSpec.getRequestInfoMap()
                .values()
                .stream()
                .map(RequestInfo::getJaxrsRequestItem)
                .filter(e -> e.getPath().equals("/pet/{petId}/uploadImage"))
                .findFirst()
                .map(JaxrsRequestItem::getRequestId)
                .orElseThrow();
        CatalogRequestInfo catalogRequestInfo = currentSpec.getRequestInfoMap().get(requestId).getCatalogRequestInfo();
        FileRepository fileRepository = serviceLocator.getService(FileRepository.class);
        ByteArrayInputStream content = new ByteArrayInputStream(new byte[]{80, 69, 84, 32, 74, 80, 69, 71});
        File file = new File(0, "pet.jpeg", "jpeg", "pet.jpeg");
        FakeFileHandle fileHandle = new FakeFileHandle(file);

        fileHandle.setContent(content);
        fileRepository.addFile(fileHandle);
        Invoker invoker = serviceLocator.getService(Invoker.class);
        ExtensionCatalogRequest extensionCatalogRequest = new DefaultExtensionCatalogRequest<>(invoker.getExtension(),
                null,
                requestId, "",
                "",
                "",
                null,
                catalogRequestInfo.getInputFields(),
                catalogRequestInfo.getOutputFields());
        openApiExtension.executeCatalogRequest(extensionCatalogRequest,
                Map.of("petId", "1", "file", List.of(file)));
    }

    @Test
    public void testXRDEV() throws IOException {
        String s = TestUtils.load("xr-dev-openapi.json");
        OpenApiContext specFromString = specParser.parseSpec(s);

        assertNotNull(specFromString);
        specStore.saveSpec(specFromString);
        specStore.clearCachedSpec();
        OpenApiContext currentSpec = specStore.getSpec();
        Objects.requireNonNull(currentSpec);
        OpenAPI openAPI = specParser.readContents(s);
        int totalRequests = getTotalRequests(openAPI);
        assertEquals(totalRequests, currentSpec.getRequestInfoMap().size());
    }

    private int getTotalRequests(OpenAPI openAPI) {
        return openAPI.getPaths().values().stream().mapToInt(p -> p.readOperations().size()).sum();
    }

    @Test
    public void testCorrectParameters() throws IOException {
        String s = TestUtils.load("xr-dev-openapi.json");
        OpenApiContext specFromString = specParser.parseSpec(s);
        assertNotNull(specFromString);
        RequestInfo requestInfo = specFromString.getRequestInfoMap().values().stream()
                .filter(e -> e.getCatalogRequestInfo().getRequestName().equals("Gets a media plan."))
                .findFirst().orElseThrow();
        assertEquals(1, requestInfo.getCatalogRequestInfo().getInputFields().size());
    }

}