package com.musevarg.devcloud.api_salesforce.wrappers

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class AuthWrapper(
    @Transient val access_token: String? = null,
    @Transient val error: String? = null,
    @Transient val error_description: String? = null
)