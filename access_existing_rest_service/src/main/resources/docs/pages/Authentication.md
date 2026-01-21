# Authentication

## Overview

The Krista Access Existing REST Service extension supports multiple authentication modes to securely connect to external REST APIs. The extension provides flexible authentication options including API Key authentication, Basic Authentication, and dynamic token management for enhanced security and session control.

## Authentication Modes

### API Key Authentication

API Key authentication is a simple and widely-used method where a unique key is passed with each request to identify and authenticate the client.

**When to Use:**
- Your REST service requires API key-based authentication
- You have a static API key from the service provider
- Simple authentication without complex token management
- Services that don't require user-specific authentication

**How it Works:**
1. API key is configured in the extension setup
2. Key is automatically included in request headers for all API calls
3. Service validates the key and processes the request

**Security Considerations:**
- API keys are long-lived credentials
- Should be stored securely and never exposed in logs
- Regular rotation is recommended
- Use different keys for different environments

### Basic Authentication

Basic Authentication uses a username and password combination encoded in Base64 format and sent in the Authorization header.

**When to Use:**
- Your REST service uses traditional username/password authentication
- You have valid user credentials for the service
- Service expects Basic Authentication header
- Legacy systems that don't support modern authentication methods

**How it Works:**
1. Username and password are configured in the extension setup
2. Credentials are Base64 encoded: `base64(username:password)`
3. Encoded credentials are sent in Authorization header: `Basic <encoded-credentials>`
4. Service validates credentials and processes the request

**Security Considerations:**
- Credentials are only Base64 encoded, not encrypted
- Always use HTTPS to protect credentials in transit
- Use strong passwords and consider service accounts
- Implement proper credential rotation policies

## Dynamic Token Management

The extension provides advanced token management capabilities through dedicated catalog requests, allowing for dynamic token handling within conversations.

### Token Storage Mechanism

**Conversation-Scoped Tokens:**
- Tokens are tied to specific conversation executions
- A token stored in Conversation A cannot be used in Conversation B
- Each conversation maintains its own token storage
- Tokens override the setup page authentication for that conversation

**Token Lifecycle:**
1. **Store**: Use the "Store Token" catalog request to save a token
2. **Retrieve**: Use the "Get Token" catalog request to access stored token
3. **Delete**: Use the "Delete Token" catalog request to remove stored token
4. **Automatic Cleanup**: Tokens are automatically cleaned up when conversation ends

### Token Management Flow

```
1. Conversation starts with setup page authentication (API Key or Basic Auth)
2. During conversation, a dynamic token can be stored using "Store Token" request
3. Subsequent API calls in the same conversation use the stored token
4. Token can be retrieved or deleted as needed
5. Conversation ends, token is automatically cleaned up
```

## Authentication Flow

### Standard Authentication Flow

1. **Configuration**: Set up authentication in extension configuration
2. **Request Initiation**: Conversation makes API call through extension
3. **Authentication**: Extension adds appropriate authentication headers
4. **API Call**: Request is sent to external REST service
5. **Response**: Service validates authentication and returns response

### Dynamic Token Flow

1. **Initial Authentication**: Use setup page credentials for initial requests
2. **Token Acquisition**: Obtain token through API call or external process
3. **Token Storage**: Use "Store Token" catalog request to save token
4. **Subsequent Requests**: Extension uses stored token instead of setup credentials
5. **Token Management**: Retrieve, refresh, or delete token as needed

## Token Expiration Scenarios

### Access Token Handling

**Token Expiration:**
- Tokens may have limited lifespans depending on the service
- Extension does not automatically refresh expired tokens
- Applications must handle token expiration and renewal

**Expiration Handling:**
1. API call fails with authentication error (401 Unauthorized)
2. Application detects token expiration
3. Application obtains new token through appropriate mechanism
4. New token is stored using "Store Token" catalog request
5. API call is retried with new token

### Token Revocation Scenarios

**Common Revocation Causes:**
- User explicitly revokes token
- Security policy changes
- Password changes
- Account suspension
- Token lifetime exceeded

**Handling Revocation:**
1. API calls return authentication errors
2. Application detects revocation
3. Stored token is deleted using "Delete Token" catalog request
4. Application falls back to setup page authentication
5. New token acquisition process is initiated if needed

## Security Best Practices

### API Key Security
1. **Secure Storage**: Store API keys in secure configuration systems
2. **Environment Separation**: Use different keys for dev, staging, production
3. **Regular Rotation**: Rotate keys according to service provider recommendations
4. **Access Control**: Limit API key permissions to minimum required scope
5. **Monitoring**: Monitor API key usage for unusual activity

### Basic Auth Security
1. **Strong Passwords**: Use complex passwords for service accounts
2. **HTTPS Only**: Never send credentials over unencrypted connections
3. **Service Accounts**: Use dedicated service accounts instead of personal accounts
4. **Credential Rotation**: Implement regular password rotation policies
5. **Audit Logging**: Monitor authentication attempts and failures

### Token Security
1. **Short Lifespans**: Use tokens with appropriate expiration times
2. **Secure Transmission**: Always transmit tokens over HTTPS
3. **Proper Storage**: Store tokens securely within conversation scope
4. **Cleanup**: Ensure tokens are properly deleted when no longer needed
5. **Monitoring**: Track token usage and detect anomalies

## Troubleshooting Authentication

### Common Authentication Issues

#### Authentication Configuration Errors
**Error Message**: "Authentication configuration error: No authentication type specified"
**Cause**: Extension setup is incomplete or corrupted
**Resolution**:
1. Go to the extension setup page
2. Select either "API Key" or "Basic Auth" authentication method
3. Save the configuration and test the connection

**Error Message**: "Authentication configuration error: The authentication type 'X' is not supported"
**Cause**: Invalid authentication type in configuration
**Resolution**: Use only supported authentication types (API_KEY or BASIC)

#### API Key Authentication Issues
**Error Message**: "Authentication Failed: Your API credentials are invalid or expired"
**Symptoms**: 401 Unauthorized responses from API calls
**Causes**:
- Incorrect API key value
- Expired or revoked API key
- API key lacks required permissions
- API key placed in wrong location (header/query/cookie)

**Resolution Steps**:
1. Verify the API key value in extension setup
2. Check API key expiration with your API provider
3. Confirm the API key has necessary permissions
4. Ensure the API key location matches your API requirements

**Error Message**: "API Key configuration error: The API key location 'X' is not supported"
**Cause**: API key configured for unsupported location
**Resolution**: Configure API key for header, query parameter, or cookie only

#### Basic Authentication Issues
**Error Message**: "Authentication Failed: Your API credentials are invalid or expired"
**Symptoms**: 401 Unauthorized responses with Basic Auth
**Causes**:
- Incorrect username or password
- Account locked or suspended
- Credentials expired
- Special characters not properly encoded

**Resolution Steps**:
1. Verify username and password in extension setup
2. Check account status with your service provider
3. Reset credentials if necessary
4. Ensure special characters are properly handled

#### Token Management Issues
**Error Message**: "Token storage failed: No token value provided"
**Cause**: Attempting to store an empty or null token
**Resolution**: Provide a valid authentication token (JWT, bearer token, or API key)

**Error Message**: "Token storage failed: Unable to save the token for this conversation"
**Cause**: System-level storage failure
**Resolution**:
1. Try the Store Token request again
2. Check system resources and connectivity
3. Contact system administrator if problem persists

**Error Message**: "No token found: No authentication token is currently stored"
**Cause**: No token has been stored in the current conversation
**Resolution**: Use the Store Token request to save an authentication token first

#### Network Issues
**Symptoms**: Connection timeouts, SSL errors
**Causes**: Network connectivity, firewall rules, certificate issues
**Resolution**: Check network, verify firewall settings, validate certificates

## See Also

- [Extension Configuration](/pages/ExtensionConfiguration.md)
- [Store Token](/pages/StoreToken.md)
- [Get Token](/pages/GetToken.md)
- [Delete Token](/pages/DeleteToken.md)
