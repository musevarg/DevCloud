package com.musevarg.devcloud.api_salesforce.calls

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import com.musevarg.devcloud.R
import com.musevarg.devcloud.api_salesforce.wrappers.AuthWrapper
import com.musevarg.devcloud.global.GlobalVariables
import kotlinx.serialization.json.Json
import okhttp3.*
import java.io.IOException

class AuthApi (private val activity: Activity) {

    private val client = OkHttpClient()

    fun getToken () {
        val request = Request.Builder()
            .url(getAuthEndpoint())
            .post(getAuthBody())
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle error
                e.printStackTrace()
                showErrorDialog(e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("ApiCall", responseBody.toString())

                isResponseSuccess(responseBody.toString())
            }
        })
    }

    private fun getAuthEndpoint(): String {
        val root = if (GlobalVariables.selectedAccount.isSandbox) "test.salesforce.com" else "login.salesforce.com"
        return "https://" + root + "/services/oauth2/token"
    }

    private fun getAuthBody(): RequestBody {
        val requestBody = FormBody.Builder()
            .apply {
                add("grant_type", "refresh_token")
                add("client_id", GlobalVariables.selectedAccount.clientId)
                add("refresh_token", GlobalVariables.selectedAccount.refreshToken)
            }
            .build()
        return requestBody
    }

    private fun isResponseSuccess(responseBody: String?) : Boolean{
        if (responseBody.isNullOrBlank()) return false

        return try{
            val json = Json { ignoreUnknownKeys = true }
            val authResponse : AuthWrapper = json.decodeFromString(responseBody)
            Log.d("ApiCall", authResponse.toString())
            if (authResponse.access_token != null){
                GlobalVariables.selectedAccount.accessToken = authResponse.access_token
                Log.d("ApiCall", "Successfully retrieved new access token")
            }
            else {
                if (authResponse.error != null && authResponse.error_description != null) {
                    showErrorDialog(authResponse.error + "\n" + authResponse.error_description)
                } else {
                    showErrorDialog(responseBody)
                }
            }
            true
        } catch (e: Exception) {
            Log.d("ApiCall", e.toString())
            showErrorDialog(responseBody)
            false
        }
    }

    private fun showErrorDialog(message: String) {
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(R.string.dev_dialog_error_title)
        dialogBuilder.setMessage(message)

        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        activity.runOnUiThread(){
            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }

}