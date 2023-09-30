package com.musevarg.devcloud.oauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.musevarg.devcloud.MainActivity
import com.musevarg.devcloud.R
import com.musevarg.devcloud.database.SavedAccount
import com.musevarg.devcloud.database.SavedAccountDao
import com.musevarg.devcloud.database.SavedAccountDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BrowserConnect : AppCompatActivity() {

    private lateinit var dao : SavedAccountDao
    private lateinit var accountNameInput: String
    private lateinit var clientIdInput: String
    private var isSandbox: Boolean = true
    private val REDIRECT_URI = "devcloud://com.musevarg"
    private var closedByUser: Boolean = false

    private fun loadDao() {
        val database = SavedAccountDatabase.getInstance(this.applicationContext) // Replace "MyRoomDatabase" with your actual Room database class name
        if (database != null) {
            dao = database.dao
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountNameInput = intent.getStringExtra("accountNameInput") ?: ""
        clientIdInput = intent.getStringExtra("clientIdInput") ?: ""
        isSandbox = intent.getStringExtra("orgInstance") != "Production"
        loadDao()
        performOAuthLogin()
    }

    private fun performOAuthLogin() {
        val authEndpoint = if (isSandbox) "https://test.salesforce.com/services/oauth2/authorize" else "https://login.salesforce.com/services/oauth2/authorize"
        val authorizationUri = Uri.parse(authEndpoint).buildUpon()
            .appendQueryParameter("response_type", "token")
            .appendQueryParameter("client_id", clientIdInput)
            .appendQueryParameter("redirect_uri", REDIRECT_URI)
            .build()


        val customTabsIntent = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(ContextCompat.getColor(this, R.color.default_dark)).build())
            .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
        .build()
        customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY;Intent.FLAG_ACTIVITY_NEW_TASK;Intent.FLAG_ACTIVITY_CLEAR_TASK;Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        customTabsIntent.intent.putExtra("org.chromium.chrome.browser.customtabs.EXTRA_DISABLE_STAR_BUTTON", true)
        customTabsIntent.intent.putExtra("org.chromium.chrome.browser.customtabs.EXTRA_DISABLE_DOWNLOAD_BUTTON", true)
        customTabsIntent.launchUrl(this, authorizationUri)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleAuthorizationResponse(intent)
    }

    private fun handleAuthorizationResponse(intent: Intent) {
        val uri = intent.data
        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
            Log.d("BrowserConnect", "Callback from Salesforce")
            // Extract the access token from the URI
            val parsedUri = Uri.parse(uri.toString().replace('#', '?'))
            val accessToken = parsedUri.getQueryParameter("access_token")
            val refreshToken = parsedUri.getQueryParameter("refresh_token")
            val instanceUrl = parsedUri.getQueryParameter("instance_url")
            val scope = parsedUri.getQueryParameter("scope")
            val tokenType = parsedUri.getQueryParameter("token_type")
            Log.d("BrowserConnect", "Token: $accessToken")

            val savedAccount = SavedAccount(
                accountName = accountNameInput,
                accessToken = accessToken.toString(),
                refreshToken = refreshToken.toString(),
                instanceUrl = instanceUrl.toString(),
                scope = scope.toString(),
                tokenType = tokenType.toString(),
                clientId = clientIdInput,
                clientSecret = "",
                isSandbox = isSandbox
            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    saveToDb(savedAccount)
                }
            }
        }
    }

    fun saveToDb(savedAccount: SavedAccount) {
        dao.upsertSavedAccount(savedAccount)
    }

    override fun onResume() {
        super.onResume()
        if (closedByUser) {
            redirectToOtherView()
        }
        closedByUser = true
    }


    private fun redirectToOtherView() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish the current activity if needed
    }
}