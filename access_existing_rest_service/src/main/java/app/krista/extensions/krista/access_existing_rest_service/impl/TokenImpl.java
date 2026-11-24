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

package app.krista.extensions.krista.access_existing_rest_service.impl;

import app.krista.extensions.krista.access_existing_rest_service.openapi.TokenStore;
import org.jvnet.hk2.annotations.Service;
import javax.inject.Inject;

import static app.krista.extensions.krista.access_existing_rest_service.Constants.*;

@Service
public class TokenImpl {

    private final TokenStore tokenStore;
    @Inject
    public TokenImpl(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public String storeToken(String tokenValue, String executionId) {
        if (tokenValue == null) {
            throw new IllegalArgumentException("Token storage failed: No token value provided. Please provide a valid authentication token (e.g., JWT token, bearer token, or API key) to store for this conversation.");
        }
        try {
            tokenStore.put(executionId + TOKEN, tokenValue);
            return "Token successfully stored for this conversation. The stored token will be used for authentication instead of the setup page credentials.";
        } catch (Exception cause) {
            return "Token storage failed: Unable to save the token for this conversation. " + cause.getMessage() + ". Please try again or contact your system administrator if the problem persists.";
        }
    }

    public String deleteToken(String executionId) {
        tokenStore.remove(executionId + TOKEN);
        if (tokenStore.get(executionId + TOKEN) == null){
            return "Token successfully deleted from this conversation. Authentication will now use the credentials configured on the extension setup page.";
        }
        return "Token deletion failed: Unable to remove the stored token from this conversation. Please try again or contact your system administrator if the problem persists.";
    }

    public String getToken(String executionId) {
        String token = tokenStore.get(executionId + TOKEN);
        if (token == null){
            return "No token found: No authentication token is currently stored for this conversation. Use the 'Store Token' request to save a token, or authentication will use the setup page credentials.";
        }
        return token;
    }

}
