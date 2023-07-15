package com.musevarg.devcloud.api_salesforce.wrappers

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class RunApexWrapper(
    val compiled: Boolean,
    val success: Boolean,
    @Transient val compileProblem: String? = null,
    @Transient val exceptionStackTrace: String? = null,
    @Transient val exceptionMessage: String? = null
)