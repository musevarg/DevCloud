package com.musevarg.devcloud.oauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent

class BrowserConnect : AppCompatActivity() {

    private val REDIRECT_URI = "https://test.salesforce.com/services/oauth2/callback"
    private val CLIENT_ID = "3MVG92u_V3UMpV.iafsw913291tcmGbHLz5B4r1LgDPXTM._F4TVgHTzTmNjmnG5tP58lf1rYYNbWPuBISQzQ"
    //private val CLIENT_SECRET = "1B070FED65E83905229416F2AC3161C5713F0351CF32268645869D16DD063DDE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performOAuthLogin()
    }

    private fun performOAuthLogin() {
        val authorizationUri = Uri.parse("https://test.salesforce.com/services/oauth2/authorize").buildUpon()
            .appendQueryParameter("response_type", "token")
            .appendQueryParameter("client_id", CLIENT_ID)
            .appendQueryParameter("redirect_uri", REDIRECT_URI)
            .build()

        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(this, authorizationUri)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleAuthorizationResponse(intent)
    }

    private fun handleAuthorizationResponse(intent: Intent) {
        val uri = intent.data
        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
            // Extract the access token from the URI
            val accessToken = uri.getQueryParameter("access_token")
            // Use the access token for making API requests to Salesforce
            Log.d("BrowserConnect", "Connection successful")
            Log.d("BrowserConnect", "Token: $accessToken")
        }
    }
}