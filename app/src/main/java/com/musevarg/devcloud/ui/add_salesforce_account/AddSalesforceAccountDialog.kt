package com.musevarg.devcloud.ui.add_salesforce_account

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.musevarg.devcloud.R

class AddSalesforceAccountDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_add_account, null))
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