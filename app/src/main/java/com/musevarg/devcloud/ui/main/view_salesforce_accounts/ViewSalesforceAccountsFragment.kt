package com.musevarg.devcloud.ui.main.view_salesforce_accounts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.musevarg.devcloud.R
import com.musevarg.devcloud.database.SavedAccountDao
import com.musevarg.devcloud.database.SavedAccountDatabase
import com.musevarg.devcloud.global.GlobalVariables
import com.musevarg.devcloud.global.GlobalVariablesUtil
import com.musevarg.devcloud.helpers.NavigationViewHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A fragment representing a list of Items.
 */
class ViewSalesforceAccountsFragment : Fragment() {

    private var columnCount = 1
    private lateinit var dao : SavedAccountDao

    fun loadDao() {
        val database = SavedAccountDatabase.getInstance(this.context) // Replace "MyRoomDatabase" with your actual Room database class name
        if (database != null) {
            dao = database.dao
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }*/
    }

    fun iterateThroughSavedAccounts(view : View) {
        val savedAccounts = dao.getAllSavedAccounts()
        for (savedAccount in savedAccounts) {
            Log.d("BrowserConnect", savedAccount.toString())
        }

        Log.d("BrowserConnect", "Found ${savedAccounts.size} saved accounts")

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                activity?.runOnUiThread {
                    layoutManager = when {
                        columnCount <= 1 -> LinearLayoutManager(context)
                        else -> GridLayoutManager(context, columnCount)
                    }
                    adapter = ViewSalesforceAccountsRecyclerViewAdapter(savedAccounts) { position ->
                        // Handle item click here
                        val det = savedAccounts[position]
                        /*val message = "$position: ${det.accountName}"
                        Toast.makeText( this.context, message, Toast.LENGTH_SHORT).show()*/

                        val gvu = GlobalVariablesUtil()
                        gvu.setAccount(det)

                        val navController = findNavController()
                        navController.navigate(R.id.nav_run_apex)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_view_salesforce_accounts_list, container, false)

        loadDao()
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                iterateThroughSavedAccounts(view)
            }
        }

        if (!GlobalVariables.isMainActivity){
            val gvu = GlobalVariablesUtil()
            val navigationView = gvu.getNavigationView()
            val activity = requireActivity() as AppCompatActivity
            val fragmentManager = this.parentFragmentManager
            NavigationViewHelper.setMainNavView(navigationView, activity, fragmentManager)
            GlobalVariables.isMainActivity = true
        }

        return view
    }

    /*companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ViewSalesforceAccountsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }*/
}