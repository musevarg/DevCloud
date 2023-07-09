package com.musevarg.devcloud.ui.add_salesforce_account

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.musevarg.devcloud.R
import com.musevarg.devcloud.oauth.BrowserConnect

class AddSalesforceAccountDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val dialogView = inflater.inflate(R.layout.dialog_add_account, null)
            val buttonConnectViaBrowser = dialogView.findViewById<Button>(R.id.button_connect_via_browser)

            buttonConnectViaBrowser.setOnClickListener {
                val intent = Intent(this.context, BrowserConnect::class.java)
                startActivity(intent)
            }

            builder.setView(dialogView)
                .setNegativeButton(R.string.dialog_close) { dialog, id ->
                    getDialog()?.cancel()
                }
            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val TAG = "AddSalesforceAccountDialog"
    }

}