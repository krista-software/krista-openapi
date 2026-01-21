# Extension Configuration

## Overview

The Krista Access Existing REST Service extension requires proper configuration to connect to external REST APIs. This configuration includes authentication setup, server connection details, and security settings to ensure secure and reliable communication with your REST services.

## Configuration Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| Auth Type | Dropdown | Yes | Authentication method for the REST service | API Key, Basic Auth |
| Server Address | Text | Yes | Base URL of the REST service | `https://petstore.swagger.io/v2` |
| Test URL Path | Text | No | Optional path to test server connectivity | `store/inventory` |
| API Key | Text | Conditional | Required when Auth Type is "API Key" | `your-api-key-here` |
| Username | Text | Conditional | Required when Auth Type is "Basic Auth" | `admin` |
| Password | Password | Conditional | Required when Auth Type is "Basic Auth" | `password123` |
| Security | Dropdown | No | Access point security configuration | Default |

## Step-by-Step Setup

### 1. Choose Authentication Type

Select the appropriate authentication method based on your REST service requirements:

- **API Key**: Use when your service requires an API key for authentication
- **Basic Auth**: Use when your service requires username/password authentication

### 2. Configure Server Connection

1. **Server Address**: Enter the base URL of your REST service
   - Must be a valid HTTP/HTTPS URL
   - Should not include trailing slashes
   - Example: `https://api.example.com/v1`

2. **Test URL Path** (Optional): 
   - Specify a path to test connectivity
   - Used for validation when clicking "Validate Attributes"
   - Should be a GET endpoint that returns 200 OK

### 3. Authentication Configuration

#### For API Key Authentication:
1. Select "API Key" from the Auth Type dropdown
2. Enter your API key in the "API Key" field
3. The API key will be included in request headers

#### For Basic Authentication:
1. Select "Basic Auth" from the Auth Type dropdown
2. Enter your username in the "Username" field
3. Enter your password in the "Password" field
4. Credentials will be base64 encoded and sent in Authorization header

### 4. Security Configuration

Choose the appropriate security access point from the dropdown if your organization has specific security requirements.

### 5. Validation and Testing

1. Click **Validate Attributes** to test the connection
2. The system will send a GET request to `{Server Address}/{Test URL Path}`
3. A successful validation returns HTTP 200 OK
4. If validation fails, check your configuration parameters

### 6. Save Configuration

After successful validation, click **Save changes** to apply the configuration.

## Authentication Type Selection

### When to Use API Key
- Your REST service uses API key-based authentication
- You have a valid API key from the service provider
- The service expects the API key in request headers
- Simpler authentication mechanism

### When to Use Basic Auth
- Your REST service uses username/password authentication
- You have valid credentials for the service
- The service expects Basic Authentication header
- More traditional authentication approach

## Security Considerations

### API Key Security
- Store API keys securely
- Never expose API keys in logs or error messages
- Rotate API keys regularly according to service provider recommendations
- Use environment-specific API keys (dev, staging, production)

### Basic Auth Security
- Use strong passwords
- Enable HTTPS for all communications
- Consider using service accounts instead of personal accounts
- Implement proper credential rotation policies

### Network Security
- Ensure all communications use HTTPS
- Verify SSL certificates
- Use appropriate firewall rules
- Consider using VPN for sensitive communications

## Troubleshooting

### Common Configuration Issues

#### OpenAPI Specification Errors

**Error**: "OpenAPI specification error: No valid API endpoints were found"
**Cause**: The uploaded specification doesn't contain valid API endpoints
**Resolution**:
1. Ensure your OpenAPI specification includes at least one path definition
2. Verify each path has supported HTTP methods (GET, POST, PUT, DELETE, PATCH)
3. Check that operations have proper definitions with required fields
4. Use an OpenAPI validator to check specification syntax

**Error**: "OpenAPI specification validation failed: Your specification contains critical errors"
**Cause**: The specification has syntax errors or missing required fields
**Resolution**:
1. Use an online OpenAPI validator (like Swagger Editor)
2. Fix all critical errors listed in the error message
3. Ensure required fields like `openapi`, `info`, and `paths` are present
4. Validate JSON/YAML syntax

**Error**: "Version compatibility error: Your OpenAPI specification was created for extension version 'X'"
**Cause**: Specification version doesn't match extension compatibility
**Resolution**:
1. Update your extension to a compatible version, or
2. Modify your OpenAPI specification to use supported version (1.0.2)
3. Check the `x-krista-version` field in your specification

#### Connection and Authentication Issues

**Error**: "Authentication Failed: Your API credentials are invalid or expired"
**Cause**: Authentication configuration problems
**Resolution**:
1. Verify API credentials in extension setup page
2. Check if credentials have expired
3. Confirm account has necessary permissions
4. Test credentials directly with the API provider

**Error**: "Access Denied: You don't have permission to access this resource"
**Cause**: Insufficient permissions for the requested operation
**Resolution**:
1. Contact your administrator to verify account permissions
2. Check if your API key/account has access to the specific endpoint
3. Review API documentation for required permission levels

**Error**: "Resource Not Found: The requested endpoint or resource doesn't exist"
**Cause**: Incorrect API URL or endpoint path
**Resolution**:
1. Verify the Server Address in extension configuration
2. Check the endpoint paths in your OpenAPI specification
3. Confirm the API version and base path are correct
4. Test the endpoint manually using a tool like Postman

**Error**: "Rate Limit Exceeded: Too many requests have been made"
**Cause**: API rate limiting is active
**Resolution**:
1. Wait for the rate limit window to reset
2. Reduce the frequency of API calls
3. Implement request batching if supported
4. Contact API provider about rate limit increases

#### Legacy Configuration Issues

**Connection Failed**
**Cause**: Invalid server address or network connectivity issues
**Resolution**:
1. Verify the server address is correct and accessible
2. Check network connectivity
3. Ensure firewall rules allow outbound connections

**Validation Timeout**
**Cause**: Server is slow to respond or unreachable
**Resolution**:
1. Check server status
2. Verify test URL path is correct
3. Try a different test endpoint

**SSL/TLS Errors**
**Cause**: Certificate validation issues
**Resolution**:
1. Ensure server has valid SSL certificate
2. Check certificate chain
3. Verify system trust store is up to date

## Best Practices

### 1. Use Environment-Specific Configurations
Configure different settings for development, staging, and production environments.

### 2. Test Connectivity Regularly
Use the validation feature to ensure ongoing connectivity to your REST services.

### 3. Monitor Authentication
Regularly verify that authentication credentials are working and haven't expired.

### 4. Document Service Dependencies
Maintain documentation of which REST services your extension depends on.

### 5. Implement Error Handling
Ensure your conversations handle authentication and connection failures gracefully.

## See Also

- [Authentication](/pages/Authentication.md)
- [Store Token](/pages/StoreToken.md)
- [Get Token](/pages/GetToken.md)
- [Delete Token](/pages/DeleteToken.md)
