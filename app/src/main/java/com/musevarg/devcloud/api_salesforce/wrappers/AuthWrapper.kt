package com.musevarg.devcloud.api_salesforce.wrappers

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class AuthWrapper(
    @SerialName("access_token") val access_token: String? = null,
    @SerialName("error") val error: String? = null,
    @SerialName("error_description") val error_description: String? = null
)