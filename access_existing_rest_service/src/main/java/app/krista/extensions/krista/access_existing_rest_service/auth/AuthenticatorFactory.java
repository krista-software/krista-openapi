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

package app.krista.extensions.krista.access_existing_rest_service.auth;

import java.util.Objects;
import javax.inject.Inject;
import app.krista.extensions.krista.access_existing_rest_service.openapi.TokenStore;
import app.krista.extensions.krista.access_existing_rest_service.services.InvokerAttributes;
import app.krista.ksdk.context.ConversationContext;
import org.jvnet.hk2.annotations.Service;

import static app.krista.extensions.krista.access_existing_rest_service.Constants.TOKEN;

@Service
public class AuthenticatorFactory {
    private final InvokerAttributes invokerAttributes;
    private final TokenStore tokenStore;
    private final ConversationContext context;

    @Inject
    public AuthenticatorFactory(InvokerAttributes invokerAttributes, TokenStore tokenStore, ConversationContext context) {
        this.invokerAttributes = invokerAttributes;
        this.tokenStore = tokenStore;
        this.context = context;
    }

    public Authenticator create(AuthenticationInfo<?> authenticationInfo) {
        if (Objects.requireNonNull(authenticationInfo.type) == AuthenticationType.API_KEY) {
            if (tokenStore.get(context.getExecution().getExecutionId() + TOKEN) != null) {
                return new ApiKeyAuthenticator(tokenStore.get(context.getExecution().getExecutionId() + TOKEN));
            }
            return new ApiKeyAuthenticator(invokerAttributes.getApiKey());
        } else if (Objects.requireNonNull(authenticationInfo.type) == AuthenticationType.BASIC) {
            return new BasicAuthAuthenticator(invokerAttributes.getUserName(), invokerAttributes.getPassword());
        } else {
            throw new IllegalStateException(
                    "Authentication setup error: The authentication type '" + authenticationInfo.type + "' is not supported. Please configure either 'API_KEY' or 'BASIC' authentication in your extension setup page and ensure your OpenAPI specification uses supported authentication methods.");
        }
    }

}
