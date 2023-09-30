package com.musevarg.devcloud.api_salesforce.calls

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.TextView
import com.musevarg.devcloud.R
import com.musevarg.devcloud.api_salesforce.wrappers.RunApexWrapper
import com.musevarg.devcloud.global.GlobalVariables
import kotlinx.serialization.json.Json
import okhttp3.*
import java.io.IOException

class ApexApi (val activity: Activity) {

    private val client = OkHttpClient()

    fun runApex (apexCode: String, view: View) {

        // Refresh the access token
        /*AuthApi(activity).also {
            it.getToken()
        }*/

        val request = Request.Builder()
            .url(getRunApexEndpoint(apexCode))
            .addHeader("Authorization", "Bearer " + GlobalVariables.selectedAccount.accessToken)
            .build()

        val responseTextView : TextView = view.findViewById(R.id.dev_console_logs)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle error
                e.printStackTrace()
                showErrorDialog("FAILURE\n$e")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("ApiCall", responseBody.toString())

                if (responseBody?.contains("INVALID_SESSION_ID", true) == true){
                    // Refresh the access token
                    AuthApi(activity).also {
                        it.getToken()
                        val apiClient = ApexApi(activity)
                        apiClient.runApex(apexCode, view)
                    }
                    return
                }

                if (isResponseSuccess(responseBody.toString())){
                    activity.runOnUiThread {
                        responseTextView.text = responseBody
                    }
                }
            }
        })
    }

    private fun getRunApexEndpoint(apexCode: String): String {
        return GlobalVariables.selectedAccount.instanceUrl + "/services/data/v58.0/tooling/executeAnonymous/?anonymousBody=" + apexCode
    }

    private fun isResponseSuccess(responseBody: String?) : Boolean{
        if (responseBody.isNullOrBlank()) return false

        try{
            val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
            val runApexResponse : RunApexWrapper = json.decodeFromString(responseBody)
            Log.d("ApiCall", runApexResponse.toString())
            return if (runApexResponse.success && runApexResponse.compiled){
                true
            } else {
                if (runApexResponse.compileProblem != null){
                    showErrorDialog(runApexResponse.compileProblem)
                } else if (runApexResponse.exceptionStackTrace != null){
                    showErrorDialog(runApexResponse.exceptionStackTrace)
                } else if (runApexResponse.exceptionMessage != null) {
                    showErrorDialog(runApexResponse.exceptionMessage)
                } else {
                    showErrorDialog(runApexResponse.toString())
                }
                false
            }
        } catch (e: Exception) {
            Log.d("ApiCall", e.toString())
            showErrorDialog(responseBody)
            return false
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