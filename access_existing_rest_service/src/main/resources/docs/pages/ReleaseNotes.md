# Release Notes Krista OpenAPI Extension

## Overview

This document contains the release history and changelog for the Krista Access Existing REST Service extension, including new features, bug fixes, improvements, and known issues for each version.
## Version 1.0.10 (Current)

**Krista Service APIs Java**: 1.0.120  
**Global Catalog Version**: GC-2025.12.3  

## Version 1.0.9
**Krista Service APIs Java**: 1.0.118
**Global Catalog Version**: GC-2025.11.5

- **Java 21 Upgrade**: Migrated to Java 21 to leverage the latest language features and performance improvements
- **Documentation Enhancements**: Updated and improved documentation for better readability and comprehension
- **User Experience**: Improved error messages to provide clearer, more actionable feedback
- **Build Improvements**: Updated build and release processes for better automation and consistency

## Version 1.0.8

**Release Date**: Latest  
**Developer**: Pulkit Jareda  
**Krista Service APIs Java**: 1.0.93-rc2  
**Global Catalog Version**: GC-2026.02.02  

### 🚀 New Features

- **Dynamic Token Management**: Enhanced token management with conversation-scoped storage
- **Improved OpenAPI Specification Parsing**: Better support for complex OpenAPI 3.0 specifications
- **Enhanced Error Handling**: More robust error handling and validation throughout the extension
- **Better Documentation**: Comprehensive documentation following official guidelines

### 🐛 Bug Fixes

- **[KC-1876](https://antbrains.atlassian.net/browse/KC-1876)**: Support dynamic tokens in OpenAPI Extension
  - Fixed issue where dynamic tokens were not properly supported
  - Improved token storage and retrieval mechanisms
  - Enhanced conversation-scoped token management

### 🔧 Improvements

- **Authentication Flow**: Streamlined authentication process for both API Key and Basic Auth
- **Validation**: Enhanced connection validation with better error messages
- **Performance**: Optimized catalog request generation from OpenAPI specifications
- **Security**: Improved token security with proper conversation scoping

### 📋 Technical Details

- **Extension Type**: REST Service Integration
- **Supported Authentication**: API Key, Basic Auth, Dynamic Tokens
- **OpenAPI Support**: 3.0+ specifications
- **Entity Support**: Full entity and entity request support
- **Conversation Scope**: Token management scoped to individual conversations

## Version 1.0.7 (Previous)

**Release Date**: Previous Release  
**Developer**: Development Team  
**Krista Service APIs Java**: Previous Version  
**Global Catalog Version**: Previous Version  

### 🚀 Features Introduced

- **Basic OpenAPI Support**: Initial support for OpenAPI specification upload
- **Catalog Request Generation**: Dynamic generation of catalog requests from OpenAPI specs
- **Authentication Methods**: Support for API Key and Basic Authentication
- **Entity Framework**: Basic entity support for OpenAPI-defined entities

### 🐛 Issues Resolved

- Initial stability improvements
- Basic error handling implementation
- Connection validation features

## Known Issues and Limitations

### Current Limitations

❌ **Complex Schema Types**: `anyOf`, `allOf`, and `oneOf` specifications are not supported  
❌ **File Upload**: Only JSON file types are supported for OpenAPI document upload  
❌ **Specification Updates**: No option to re-upload or modify existing specifications  
❌ **Recursive Schemas**: Unstructured or recursive schema definitions are not supported  
❌ **Backward Compatibility**: Extension updates may break existing configurations  

### Workarounds

1. **Complex Schemas**: Simplify schema definitions to use supported types
2. **File Formats**: Convert OpenAPI specifications to JSON format before upload
3. **Specification Changes**: Create new extension instance for specification updates
4. **Schema Complexity**: Use flat schema structures where possible

## Upgrade Instructions

### From Version 1.0.7 to 1.0.8

> **⚠️ Important**: Before upgrading, ensure you have backups of your current configuration.

1. **Backup Configuration**: Export current extension configuration
2. **Note Token Usage**: Document any conversations using token management
3. **Upgrade Extension**: Update to version 1.0.8
4. **Verify Configuration**: Test authentication and connection validation
5. **Update Conversations**: Review conversations using token management features

### Migration Notes

- **Token Management**: Enhanced token management may require conversation updates
- **Authentication**: Existing authentication configurations should work without changes
- **OpenAPI Specs**: Previously uploaded specifications remain compatible
- **Catalog Requests**: Generated catalog requests maintain backward compatibility

## Compatibility Matrix

| Component | Version 1.0.8 | Version 1.0.7 |
|-----------|----------------|----------------|
| Krista Service APIs Java | 1.0.93-rc2 | Previous | 
| Global Catalog Version | 1.0.44 | Previous |
| OpenAPI Support | 3.0+ | 3.0+ |
| Authentication Methods | API Key, Basic Auth, Tokens | API Key, Basic Auth |
| Entity Support | Full | Basic |

## Future Roadmap

### Planned Features

🔮 **Complex Schema Support**: Support for `anyOf`, `allOf`, and `oneOf` specifications  
🔮 **Multiple File Formats**: Support for YAML and other OpenAPI formats  
🔮 **Specification Updates**: Ability to update existing OpenAPI specifications  
🔮 **Advanced Authentication**: OAuth 2.0 and other modern authentication methods  
🔮 **Enhanced Validation**: More comprehensive OpenAPI specification validation  

### Under Consideration

- **Batch Operations**: Support for batch API operations
- **Caching**: Response caching for improved performance
- **Monitoring**: Built-in API monitoring and analytics
- **Testing**: Integrated API testing capabilities

## Support and Feedback

### Getting Help

- **Documentation**: Refer to the comprehensive documentation in this package
- **Configuration Issues**: See [Extension Configuration](/pages/ExtensionConfiguration.md)
- **Authentication Problems**: See [Authentication Guide](/pages/Authentication.md)
- **Token Management**: See token management documentation

### Reporting Issues

When reporting issues, please include:

1. **Extension Version**: Current version number
2. **Krista Version**: Your Krista platform version
3. **Configuration**: Sanitized configuration details (remove sensitive data)
4. **Steps to Reproduce**: Clear steps to reproduce the issue
5. **Expected vs Actual**: What you expected vs what actually happened
6. **Logs**: Relevant log entries (remove sensitive information)

### Feature Requests

We welcome feature requests and suggestions for improving the extension. Please provide:

1. **Use Case**: Describe your specific use case
2. **Current Limitation**: What current limitation you're facing
3. **Proposed Solution**: Your suggested approach
4. **Business Impact**: How this would benefit your organization

## See Also

- [Extension Configuration](/pages/ExtensionConfiguration.md)
- [Authentication](/pages/Authentication.md)
- [Store Token](/pages/StoreToken.md)
- [Get Token](/pages/GetToken.md)
- [Delete Token](/pages/DeleteToken.md)
