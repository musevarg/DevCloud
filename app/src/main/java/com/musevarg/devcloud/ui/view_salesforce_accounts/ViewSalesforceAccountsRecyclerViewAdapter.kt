package com.musevarg.devcloud.ui.view_salesforce_accounts

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.musevarg.devcloud.database.SavedAccount
import com.musevarg.devcloud.databinding.FragmentViewSalesforceAccountsBinding

class ViewSalesforceAccountsRecyclerViewAdapter(
    private val values: List<SavedAccount>,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<ViewSalesforceAccountsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentViewSalesforceAccountsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.accountName
        holder.contentView.text = item.instanceUrl
        // Handle item click
        holder.itemView.setOnClickListener {
            onItemClick(position)
            // Example: Change background color of clicked item
            //Toast.makeText(this, "Clicked item at position: $position", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentViewSalesforceAccountsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.accountName
        val contentView: TextView = binding.accountDetail

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }

    }

}