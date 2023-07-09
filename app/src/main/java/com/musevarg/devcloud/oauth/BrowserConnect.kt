package com.musevarg.devcloud.oauth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.musevarg.devcloud.MainActivity
import com.musevarg.devcloud.R

class BrowserConnect : AppCompatActivity() {

    private val REQUEST_CODE_REDIRECT = 123
    private val REDIRECT_URI = "devcloud://com.musevarg"
    private val CLIENT_ID = getString(R.string.client_id)
    //private val CLIENT_SECRET = getString(R.string.client_secret)
    private val redirectLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Handle the result and perform redirection
            redirectToOtherView()
        }
    }

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
        redirectLauncher.launch(intent)
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