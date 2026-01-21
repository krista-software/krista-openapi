# Store Token

## Overview

The Store Token catalog request allows you to store an authentication token within a conversation scope, which will be used instead of the setup page authentication for subsequent API calls in the same conversation.

## Request Details

- **Area**: rest
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ✅ Yes - Token storage operations can be safely retried

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| Token Value | Text | Yes | The authentication token to store for this conversation | `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...` |

### Parameter Details

**Token Value**:
- Must be a valid authentication token string
- Can be JWT tokens, bearer tokens, or any string-based token
- Token format depends on the target REST service requirements
- Maximum length: 4096 characters
- Cannot be empty or null

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Status | Text | Confirmation message indicating successful token storage |

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| Token Value is required | "Token Value cannot be empty" | Provide a valid token string |
| Token Value exceeds maximum length | "Token Value is too long" | Use a shorter token or verify token format |
| Invalid token format | "Invalid token format provided" | Verify token is properly formatted |

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Invalid or missing token value
**Common Scenarios**:
- Empty token value provided
- Token exceeds maximum length
- Invalid characters in token

**Resolution**: Verify the token value is correct and properly formatted

### Logic Errors (LOGIC_ERROR)
**Cause**: Business logic validation failures
**Common Scenarios**:
- Token format validation fails
- Conversation context issues

**Resolution**: Check token format and ensure conversation is properly initialized

### System Errors (SYSTEM_ERROR)
**Cause**: System-level failures during token storage
**Common Scenarios**:
- Database storage failures
- Memory allocation issues
- System resource constraints

**Resolution**: Retry the operation or contact system administrator

## Response Messages

### Success Response
```
Token successfully stored for this conversation. The stored token will be used for authentication instead of the setup page credentials.
```

### Error Responses

**Invalid Token Value**
```
Token storage failed: No token value provided. Please provide a valid authentication token (e.g., JWT token, bearer token, or API key) to store for this conversation.
```

**Storage System Error**
```
Token storage failed: Unable to save the token for this conversation. [Technical details]. Please try again or contact your system administrator if the problem persists.
```

## Usage Examples

### Example 1: Store JWT Token

**Input**:
```
Token Value: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
```

**Output**:
```json
{
  "Status": "Token stored successfully"
}
```

**Result**: JWT token is stored and will be used for subsequent API calls in this conversation.

### Example 2: Store Bearer Token

**Input**:
```
Token Value: "Bearer abc123def456ghi789"
```

**Output**:
```json
{
  "Status": "Token stored successfully"
}
```

**Result**: Bearer token is stored and replaces setup page authentication for this conversation.

### Example 3: Store API Key Token

**Input**:
```
Token Value: "sk-1234567890abcdef"
```

**Output**:
```json
{
  "Status": "Token stored successfully"
}
```

**Result**: API key token is stored for use in subsequent requests.

## Business Rules

1. **Conversation Scope**: Tokens are scoped to the specific conversation execution
2. **Override Behavior**: Stored tokens override setup page authentication settings
3. **Single Token**: Only one token can be stored per conversation at a time
4. **Automatic Cleanup**: Tokens are automatically removed when conversation ends
5. **No Cross-Conversation Access**: Tokens cannot be shared between different conversations

## Limitations

1. **Token Validation**: Extension does not validate token authenticity or expiration
2. **Format Agnostic**: Extension treats tokens as opaque strings
3. **No Encryption**: Tokens are stored in memory without additional encryption
4. **Conversation Lifetime**: Tokens are lost when conversation execution completes
5. **Single Storage**: Cannot store multiple tokens simultaneously

## Best Practices

### 1. Token Security
Store only valid, non-expired tokens to avoid authentication failures.

### 2. Error Handling
Implement proper error handling for token storage failures in your conversations.

### 3. Token Lifecycle Management
Store tokens early in conversation flow and clean up when no longer needed.

### 4. Validation Before Storage
Validate token format and authenticity before storing when possible.

## Common Use Cases

### 1. OAuth Flow Integration
```
Scenario: Integrate with OAuth-protected APIs
Action: Obtain OAuth token through authentication flow, then store for API calls
Result: Subsequent API requests use OAuth token instead of basic authentication
```

### 2. Session Token Management
```
Scenario: Manage session-based authentication
Action: Login to service, receive session token, store for conversation duration
Result: All API calls use session token for authentication
```

### 3. Dynamic Authentication
```
Scenario: Switch authentication methods during conversation
Action: Start with API key, obtain JWT token, store JWT for enhanced permissions
Result: Conversation uses JWT token for privileged operations
```

## Related Catalog Requests

- [Get Token](/pages/GetToken.md) - Retrieve the currently stored token
- [Delete Token](/pages/DeleteToken.md) - Remove the stored token

## Technical Implementation

### Helper Class
- **Class**: TokenImpl
- **Package**: app.krista.extensions.krista.access_existing_rest_service.impl
- **Validation**: Token value validation and storage logic
- **Service**: Token storage in conversation-scoped memory

### Telemetry Metrics
- **TOKEN_STORE_SUCCESS**: Successful token storage operations
- **TOKEN_STORE_FAILURE**: Failed token storage attempts
- **TOKEN_VALIDATION_ERROR**: Token validation failures

## Troubleshooting

### Token Storage Fails
**Cause**: Invalid token format or system issues
**Solution**: Verify token format and retry operation

### Authentication Still Uses Setup Page
**Cause**: Token storage failed or token was not properly stored
**Solution**: Verify token was stored successfully using Get Token request

## See Also

- [Authentication](/pages/Authentication.md)
- [Extension Configuration](/pages/ExtensionConfiguration.md)
- [Get Token](/pages/GetToken.md)
- [Delete Token](/pages/DeleteToken.md)
