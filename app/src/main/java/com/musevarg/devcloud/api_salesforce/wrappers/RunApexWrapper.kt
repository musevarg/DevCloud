package com.musevarg.devcloud.api_salesforce.wrappers

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class RunApexWrapper(
    val compiled: Boolean,
    val success: Boolean,
    @SerialName("compileProblem") val compileProblem: String? = null,
    @SerialName("exceptionStackTrace") val exceptionStackTrace: String? = null,
    @SerialName("exceptionMessag") val exceptionMessage: String? = null
)