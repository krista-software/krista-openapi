# Get Token

## Overview

The Get Token catalog request retrieves the authentication token that was previously stored in the current conversation scope using the Store Token request.

## Request Details

- **Area**: rest
- **Type**: QUERY_SYSTEM
- **Retry Support**: ✅ Yes - Token retrieval operations are safe to retry

## Input Parameters

This catalog request requires no input parameters.

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Token | Text | The authentication token stored for this conversation, or empty if no token is stored |

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| Conversation context required | "No active conversation context" | Ensure request is made within a valid conversation |
| Token retrieval failure | "Failed to retrieve token" | Check system status and retry |

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Invalid request context
**Common Scenarios**:
- Request made outside of conversation context
- Invalid conversation state

**Resolution**: Ensure request is made within an active conversation

### Logic Errors (LOGIC_ERROR)
**Cause**: Business logic validation failures
**Common Scenarios**:
- Conversation context is invalid
- Token storage system is unavailable

**Resolution**: Verify conversation is properly initialized and retry

### System Errors (SYSTEM_ERROR)
**Cause**: System-level failures during token retrieval
**Common Scenarios**:
- Memory access issues
- System resource constraints
- Storage system failures

**Resolution**: Retry the operation or contact system administrator

## Response Messages

### Success Response (Token Found)
Returns the stored authentication token as a string value.

### No Token Response
```
No token found: No authentication token is currently stored for this conversation. Use the 'Store Token' request to save a token, or authentication will use the setup page credentials.
```

## Usage Examples

### Example 1: Retrieve Stored Token

**Input**: None

**Output**:
```json
{
  "Token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
}
```

**Result**: Returns the JWT token that was previously stored in this conversation.

### Example 2: No Token Stored

**Input**: None

**Output**:
```json
{
  "Token": ""
}
```

**Result**: Returns empty string when no token has been stored in the current conversation.

### Example 3: Verify Token After Storage

**Input**: None

**Output**:
```json
{
  "Token": "Bearer abc123def456ghi789"
}
```

**Result**: Confirms the bearer token was successfully stored and can be retrieved.

## Business Rules

1. **Conversation Scope**: Only returns tokens stored within the current conversation
2. **Single Token**: Returns the most recently stored token (only one token per conversation)
3. **Empty Response**: Returns empty string if no token is currently stored
4. **No Validation**: Does not validate token authenticity or expiration
5. **Read-Only Operation**: Does not modify the stored token

## Limitations

1. **No Token Validation**: Does not check if returned token is valid or expired
2. **Conversation Bound**: Cannot retrieve tokens from other conversations
3. **No History**: Cannot retrieve previously stored tokens that were overwritten
4. **Memory Only**: Tokens are not persisted beyond conversation lifetime
5. **Single Token Storage**: Cannot retrieve multiple tokens

## Best Practices

### 1. Token Verification
Verify the returned token is not empty before using it for API calls.

### 2. Error Handling
Implement proper error handling for cases where no token is stored.

### 3. Token Validation
Consider validating token format and expiration after retrieval.

### 4. Fallback Authentication
Have fallback authentication ready if no token is available.

## Common Use Cases

### 1. Token Verification
```
Scenario: Verify a token was successfully stored
Action: Call Get Token after Store Token operation
Result: Confirms token storage was successful
```

### 2. Conditional Authentication
```
Scenario: Use different authentication based on token availability
Action: Check if token exists using Get Token
Result: Use token authentication if available, otherwise use setup page credentials
```

### 3. Token Debugging
```
Scenario: Debug authentication issues in conversations
Action: Retrieve current token to verify its value
Result: Helps identify token-related authentication problems
```

### 4. Token Refresh Logic
```
Scenario: Implement token refresh workflow
Action: Get current token, check expiration, refresh if needed
Result: Maintains valid authentication throughout conversation
```

## Related Catalog Requests

- [Store Token](/pages/StoreToken.md) - Store an authentication token
- [Delete Token](/pages/DeleteToken.md) - Remove the stored token

## Technical Implementation

### Helper Class
- **Class**: TokenImpl
- **Package**: app.krista.extensions.krista.access_existing_rest_service.impl
- **Validation**: Conversation context validation
- **Service**: Token retrieval from conversation-scoped memory

### Telemetry Metrics
- **TOKEN_GET_SUCCESS**: Successful token retrieval operations
- **TOKEN_GET_EMPTY**: Token retrieval when no token is stored
- **TOKEN_GET_FAILURE**: Failed token retrieval attempts

## Troubleshooting

### Empty Token Returned
**Cause**: No token has been stored in the current conversation
**Solution**: Use Store Token request to store a token first

### Token Retrieval Fails
**Cause**: System issues or invalid conversation context
**Solution**: Verify conversation is active and retry operation

### Unexpected Token Value
**Cause**: Token was overwritten by another Store Token operation
**Solution**: Verify token storage logic in conversation flow

## See Also

- [Authentication](/pages/Authentication.md)
- [Extension Configuration](/pages/ExtensionConfiguration.md)
- [Store Token](/pages/StoreToken.md)
- [Delete Token](/pages/DeleteToken.md)
