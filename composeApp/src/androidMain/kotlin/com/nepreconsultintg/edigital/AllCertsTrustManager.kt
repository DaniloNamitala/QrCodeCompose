package com.nepreconsultintg.edigital

import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class AllCertsTrustManager : X509TrustManager  {
    @Suppress("TrustAllX509TrustManager")
    override fun checkServerTrusted(
        chain: Array<X509Certificate>,
        authType: String
    ) {
        // no-op
    }

    @Suppress("TrustAllX509TrustManager")
    override fun checkClientTrusted(
        chain: Array<X509Certificate>,
        authType: String
    ) {
        // no-op
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
}