package com.nepreconsultintg.edigital

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

internal expect fun HttpClientConfig<*>.configureForPlatform()