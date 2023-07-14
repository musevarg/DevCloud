package com.musevarg.devcloud.ui.account_pages.apex_run

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.musevarg.devcloud.R
import com.musevarg.devcloud.api_salesforce.ApexApi
import com.musevarg.devcloud.global.GlobalVariables
import com.musevarg.devcloud.global.GlobalVariablesUtil
import com.musevarg.devcloud.helpers.NavigationViewHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RunApexFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RunApexFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_run_apex, container, false)

        val gvu = GlobalVariablesUtil()
        val navigationView = gvu.getNavigationView()

        val activity = requireActivity() as AppCompatActivity
        NavigationViewHelper.setAccountNavView(navigationView, activity)

        /*val navigationView = gvu.getNavigationView()
        navigationView.menu.clear()
        navigationView.inflateMenu(R.menu.activity_account_drawer)

        val activity = requireActivity() as AppCompatActivity
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        val drawerLayout = activity.findViewById<DrawerLayout>(R.id.drawer_layout)

        toolbar.setNavigationIcon(R.drawable.ic_menu)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle navigation item clicks here
            when (menuItem.itemId) {
                R.id.nav_back -> {
                    val navController = Navigation.findNavController(view)
                    navController.navigate(R.id.nav_view_salesforce_accounts)
                }
            }

            // Close the drawer after handling the click
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }*/

        val headerText: TextView = view.findViewById(R.id.dev_console_header)
        val text = gvu.getAccount().accountName + " - Dev Console"
        headerText.text = text

        val codeInput: TextView = view.findViewById(R.id.dev_console_input)

        val sendButton: Button = view.findViewById(R.id.button_run_apex)
        sendButton.setOnClickListener {
            Toast.makeText(this.context, R.string.dev_run_apex_toast, Toast.LENGTH_SHORT).show()
            hideKeyboard()

            val apiClient = ApexApi()
            apiClient.runApex(codeInput.text.toString(), view, activity)
        }

        GlobalVariables.isMainActivity = false

        return view
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RunApexFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RunApexFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}