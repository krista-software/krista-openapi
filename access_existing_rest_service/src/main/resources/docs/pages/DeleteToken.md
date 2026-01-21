# Delete Token

## Overview

The Delete Token catalog request removes the authentication token that was previously stored in the current conversation scope, reverting authentication back to the setup page configuration.

## Request Details

- **Area**: rest
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ✅ Yes - Token deletion operations are safe to retry

## Input Parameters

This catalog request requires no input parameters.

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Status | Text | Confirmation message indicating the result of the token deletion operation |

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| Conversation context required | "No active conversation context" | Ensure request is made within a valid conversation |
| Token deletion failure | "Failed to delete token" | Check system status and retry |

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
**Cause**: System-level failures during token deletion
**Common Scenarios**:
- Memory access issues
- System resource constraints
- Storage system failures

**Resolution**: Retry the operation or contact system administrator

## Response Messages

### Success Response
```
Token successfully deleted from this conversation. Authentication will now use the credentials configured on the extension setup page.
```

### Error Response
```
Token deletion failed: Unable to remove the stored token from this conversation. Please try again or contact your system administrator if the problem persists.
```

## Usage Examples

### Example 1: Delete Stored Token

**Input**: None

**Output**:
```json
{
  "Status": "Token deleted successfully"
}
```

**Result**: The stored token is removed and authentication reverts to setup page configuration.

### Example 2: Delete When No Token Exists

**Input**: None

**Output**:
```json
{
  "Status": "No token to delete"
}
```

**Result**: Operation completes successfully even when no token was stored.

### Example 3: Security Cleanup

**Input**: None

**Output**:
```json
{
  "Status": "Token deleted successfully"
}
```

**Result**: Token is securely removed from memory for security purposes.

## Business Rules

1. **Conversation Scope**: Only affects tokens stored within the current conversation
2. **Authentication Reversion**: After deletion, authentication reverts to setup page configuration
3. **Idempotent Operation**: Safe to call multiple times, even when no token exists
4. **Immediate Effect**: Token deletion takes effect immediately for subsequent API calls
5. **No Recovery**: Deleted tokens cannot be recovered and must be re-stored if needed

## Limitations

1. **No Token Recovery**: Once deleted, tokens cannot be restored
2. **Conversation Bound**: Cannot delete tokens from other conversations
3. **Immediate Effect**: All subsequent API calls will use setup page authentication
4. **No Confirmation**: Operation succeeds even if no token was stored
5. **Memory Only**: Only affects in-memory token storage

## Best Practices

### 1. Security Cleanup
Delete tokens when they are no longer needed to minimize security exposure.

### 2. Error Handling
Implement proper error handling even though the operation is generally safe.

### 3. Authentication Fallback
Ensure setup page authentication is properly configured before deleting tokens.

### 4. Conversation Flow
Consider the impact on subsequent API calls when deleting tokens.

## Common Use Cases

### 1. Security Cleanup
```
Scenario: Remove sensitive tokens when conversation completes sensitive operations
Action: Delete token after completing privileged operations
Result: Reduces security exposure by removing stored credentials
```

### 2. Authentication Reset
```
Scenario: Reset authentication to setup page configuration
Action: Delete stored token to revert to original authentication
Result: Subsequent API calls use setup page credentials
```

### 3. Token Refresh Workflow
```
Scenario: Clear expired token before storing new one
Action: Delete old token, obtain new token, store new token
Result: Ensures clean token state during refresh operations
```

### 4. Error Recovery
```
Scenario: Recover from authentication errors by clearing bad tokens
Action: Delete potentially invalid token to reset authentication state
Result: Allows conversation to continue with setup page authentication
```

## Related Catalog Requests

- [Store Token](/pages/StoreToken.md) - Store an authentication token
- [Get Token](/pages/GetToken.md) - Retrieve the currently stored token

## Technical Implementation

### Helper Class
- **Class**: TokenImpl
- **Package**: app.krista.extensions.krista.access_existing_rest_service.impl
- **Validation**: Conversation context validation
- **Service**: Token removal from conversation-scoped memory

### Telemetry Metrics
- **TOKEN_DELETE_SUCCESS**: Successful token deletion operations
- **TOKEN_DELETE_NO_TOKEN**: Token deletion when no token was stored
- **TOKEN_DELETE_FAILURE**: Failed token deletion attempts

## Troubleshooting

### Token Deletion Fails
**Cause**: System issues or invalid conversation context
**Solution**: Verify conversation is active and retry operation

### Authentication Still Uses Token
**Cause**: Token deletion failed or caching issues
**Solution**: Verify deletion was successful and restart conversation if needed

### Unexpected Authentication Behavior
**Cause**: Setup page authentication not properly configured
**Solution**: Verify setup page authentication settings are correct

## See Also

- [Authentication](/pages/Authentication.md)
- [Extension Configuration](/pages/ExtensionConfiguration.md)
- [Store Token](/pages/StoreToken.md)
- [Get Token](/pages/GetToken.md)
