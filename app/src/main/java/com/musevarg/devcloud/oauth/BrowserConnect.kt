package com.musevarg.devcloud.oauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
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
    private val REDIRECT_URI = "devcloud://com.musevarg"

    private fun loadDao() {
        val database = SavedAccountDatabase.getInstance(this.applicationContext) // Replace "MyRoomDatabase" with your actual Room database class name
        if (database != null) {
            dao = database.dao
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountNameInput = intent.getStringExtra("accountNameInput") ?: ""
        loadDao()
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
                tokenType = tokenType.toString()
            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    saveToDb(savedAccount)
                }
            }
        }
        redirectToOtherView()
    }

    fun saveToDb(savedAccount: SavedAccount) {
        dao.upsertSavedAccount(savedAccount)
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