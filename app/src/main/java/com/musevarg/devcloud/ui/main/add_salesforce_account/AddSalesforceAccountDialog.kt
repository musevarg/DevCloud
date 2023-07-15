package com.musevarg.devcloud.ui.main.add_salesforce_account

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.musevarg.devcloud.R
import com.musevarg.devcloud.oauth.BrowserConnect

class AddSalesforceAccountDialog : DialogFragment(), AdapterView.OnItemSelectedListener {

    private var orgInstanceSelected : String = "Sandbox"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val dialogView = inflater.inflate(R.layout.dialog_add_account, null)

            val spinner: Spinner = dialogView.findViewById(R.id.account_org_instance)
            spinner.onItemSelectedListener = this
            // Create an ArrayAdapter using the string array and a default spinner layout
            this.context?.let { it1 ->
                ArrayAdapter.createFromResource(
                    it1,
                    R.array.dialog_org_instance_array,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    // Apply the adapter to the spinner
                    spinner.adapter = adapter
                }
            }

            val buttonConnectViaBrowser = dialogView.findViewById<Button>(R.id.button_connect_via_browser)
            val accountNameInput = dialogView.findViewById<EditText>(R.id.account_name_input)
            val clientIdInput = dialogView.findViewById<EditText>(R.id.account_client_id)

            buttonConnectViaBrowser.setOnClickListener {
                if (accountNameInput.text.isEmpty()) {
                    Toast.makeText(this.context, getText(R.string.dialog_name_field_empty), Toast.LENGTH_SHORT).show()
                } else if (clientIdInput.text.isEmpty()) {
                    Toast.makeText(this.context, getText(R.string.dialog_client_id_field_empty), Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this.context, BrowserConnect::class.java)
                    intent.putExtra("accountNameInput", accountNameInput.text.toString())
                    intent.putExtra("clientIdInput", clientIdInput.text.toString())
                    intent.putExtra("orgInstance", orgInstanceSelected)
                    startActivity(intent)
                }
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

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        orgInstanceSelected = parent.getItemAtPosition(pos).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

}