package com.musevarg.devcloud.ui.view_salesforce_accounts

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.musevarg.devcloud.R
import com.musevarg.devcloud.ui.view_salesforce_accounts.placeholder.PlaceholderContent

/**
 * A fragment representing a list of Items.
 */
class ViewSalesforceAccountsFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_view_salesforce_accounts_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = ViewSalesforceAccountsRecyclerViewAdapter(PlaceholderContent.ITEMS) { position ->
                    // Handle item click here
                    // Example: Show a toast with the clicked item position
                    //val det = adapter.getItem(position);
                    val message = "$position ";
                    Toast.makeText( this.context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }

    companion object {

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
    }
}