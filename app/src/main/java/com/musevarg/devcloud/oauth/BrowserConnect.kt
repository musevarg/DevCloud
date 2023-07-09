package com.musevarg.devcloud.oauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.musevarg.devcloud.MainActivity
import com.musevarg.devcloud.R

class BrowserConnect : AppCompatActivity() {

    private val REDIRECT_URI = "devcloud://com.musevarg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performOAuthLogin()
    }

    private fun performOAuthLogin() {
        val authorizationUri = Uri.parse("https://test.salesforce.com/services/oauth2/authorize").buildUpon()
            .appendQueryParameter("response_type", "token")
            .appendQueryParameter("client_id", getString(R.string.client_id))
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
            Log.d("BrowserConnect", "Call back from Salesforce")
            // Extract the access token from the URI
            val parsedUri = Uri.parse(uri.toString().replace('#', '?'))
            val accessToken = parsedUri.getQueryParameter("access_token")
            // Use the access token for making API requests to Salesforce

            Log.d("BrowserConnect", "Token: $accessToken")
        }
        redirectToOtherView()
    }

    private fun redirectToOtherView() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish the current activity if needed
    }

    override fun onDestroy() {
        super.onDestroy()
        redirectToOtherView()
    }
}