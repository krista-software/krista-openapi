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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import app.krista.extension.executor.Invoker;
import app.krista.extension.util.InvokerAttributeProvider;
import app.krista.extensions.krista.access_existing_rest_service.fake.FakeFileRepository;
import app.krista.extensions.krista.access_existing_rest_service.fake.FakeInvoker;
import app.krista.extensions.krista.access_existing_rest_service.fake.FakeKeyValueStore;
import app.krista.extensions.krista.access_existing_rest_service.fake.FakeRequestContext;
import app.krista.ksdk.context.ConversationContext;
import app.krista.ksdk.files.FileRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

import static app.krista.extensions.krista.access_existing_rest_service.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestUtils {

    private TestUtils() {
    }

    public static ServiceLocator createDefaultServiceLocator() {
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create(null);

        // Load extension.json
        List<String> services = fetchServiceList();
        for (String s : services) {
            try {
                Class<?> aClass = Class.forName(s);
                ServiceLocatorUtilities.addClasses(serviceLocator, aClass);
            } catch (ClassNotFoundException cause) {
                throw new RuntimeException(cause);
            }
        }
        ServiceLocatorUtilities.addClasses(serviceLocator, FakeKeyValueStore.class);
        ServiceLocatorUtilities.addClasses(serviceLocator, FakeInvoker.class);
        ServiceLocatorUtilities.addClasses(serviceLocator, FakeRequestContext.class);
        Invoker invoker = serviceLocator.getService(Invoker.class);
        InvokerAttributeProvider<String> serverAddress = new InvokerAttributeProvider<>(invoker, SERVER_ADDRESS);
        Type invokerAttributeProviderType = new TypeToken<InvokerAttributeProvider<String>>() {
        }.getType();
        ServiceLocatorUtilities.addOneConstant(serviceLocator, serverAddress, SERVER_ADDRESS,
                invokerAttributeProviderType);
        ServiceLocatorUtilities.addOneConstant(serviceLocator, new FakeFileRepository(), null, FileRepository.class);
        ServiceLocatorUtilities.addClasses(serviceLocator, OpenApiExtension.class);
        return serviceLocator;
    }

    @SuppressWarnings("unchecked")
    private static List<String> fetchServiceList() {
        String filename = "/META-INF/krista/extension.json";
        InputStream is = TestUtils.class.getResourceAsStream(filename);
        assertNotNull(is, "The file should exist in the resources folder");
        List<String> services;
        try {
            Map<String, Object> map =
                    (Map<String, Object>) new Gson().fromJson(new String(is.readAllBytes()), Map.class);
            services = (List<String>) map.get("services");
        } catch (IOException cause) {
            throw new RuntimeException(cause);
        }
        return services;
    }

    public static void addAttribute(ServiceLocator serviceLocator, String key, String value) {
        Invoker invoker = serviceLocator.getService(Invoker.class);
        Type type = new TypeToken<InvokerAttributeProvider<String>>() {
        }.getType();
        InvokerAttributeProvider<String> apiKey = new InvokerAttributeProvider<>(invoker, key);
        ServiceLocatorUtilities.addOneConstant(serviceLocator, apiKey, key, type);
        Map<String, Object> attributes = invoker.getAttributes();
        attributes.put(key, value);
    }

    public static void addConversationContext(ServiceLocator serviceLocator, ConversationContext context) {
        Type type = new TypeToken<ConversationContext>() {}.getType();
        ServiceLocatorUtilities.addOneConstant(serviceLocator, context, null, type);
    }

    public static <T> void addService(ServiceLocator serviceLocator, Class<T> serviceClass, T serviceInstance) {
        ServiceLocatorUtilities.addOneConstant(serviceLocator, serviceInstance, null, serviceClass);
    }

    public static String load(String name) throws IOException {
        InputStream inputStream = Objects
                .requireNonNull(TestUtils.class.getClassLoader().getResourceAsStream(name));
        return new String(inputStream.readAllBytes());
    }

}
