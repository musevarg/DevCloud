package com.musevarg.devcloud.api_salesforce

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.musevarg.devcloud.R
import com.musevarg.devcloud.global.GlobalVariables
import okhttp3.*
import java.io.IOException

class ApexApi {

    private val client = OkHttpClient()

    fun runApex (apexCode: String, view: View, activity: Activity) {
        val request = Request.Builder()
            .url(getRunApexEndpoint(apexCode))
            .build()

        val responseTextView : TextView = view.findViewById(R.id.dev_console_logs)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle error
                e.printStackTrace()
                activity.runOnUiThread {
                    responseTextView.text = e.stackTrace.toString()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                // Process the response body
                println(responseBody)

                activity.runOnUiThread {
                    responseTextView.text = responseBody
                }
            }
        })
    }

    private fun getRunApexEndpoint(apexCode: String): String {
        return GlobalVariables.selectedAccount.instanceUrl + "/services/data/v58.0/tooling/executeAnonymous/?anonymousBody=" + apexCode
    }

}