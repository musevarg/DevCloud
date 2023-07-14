package com.musevarg.devcloud.helpers

import android.app.Activity
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.musevarg.devcloud.R
import com.musevarg.devcloud.global.GlobalVariables
import com.musevarg.devcloud.global.GlobalVariablesUtil
import com.musevarg.devcloud.ui.main.add_salesforce_account.AddSalesforceAccountDialog

class NavigationViewHelper {
    companion object {

        fun setMainNavView(navigationView: NavigationView, activity: Activity, fragmentManager: FragmentManager) {
            val drawerLayout = GlobalVariables.drawer_layout
            val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)

            navigationView.menu.clear() // Clear existing menu items
            navigationView.inflateMenu(R.menu.activity_main_drawer)
            navigationView.setupWithNavController(GlobalVariables.nav_controller)
            val headerView = navigationView.getHeaderView(0)
            navigationView.removeHeaderView(headerView)
            navigationView.inflateHeaderView(R.layout.nav_header_main)

            val toggle = ActionBarDrawerToggle(
                activity,
                drawerLayout,
                toolbar, // Replace with your toolbar instance
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )

            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()



            // Set up the navigation item selected listener
            navigationView.setNavigationItemSelectedListener { menuItem ->
                // Handle navigation item selection

                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }

            navigationView.setNavigationItemSelectedListener { menuItem ->
                // Handle navigation item clicks here
                when (menuItem.itemId) {
                    R.id.nav_add_salesforce_account -> {
                        AddSalesforceAccountDialog().show(fragmentManager, AddSalesforceAccountDialog.TAG)
                    }
                    R.id.nav_view_salesforce_accounts -> {
                        //val navController = Navigation.findNavController(view)
                        GlobalVariables.nav_controller.navigate(R.id.nav_view_salesforce_accounts)
                    }
                    R.id.nav_about -> {
                        //val navController = Navigation.findNavController(view)
                        GlobalVariables.nav_controller.navigate(R.id.nav_about)
                    }
                }

                // Close the drawer after handling the click
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
        }

        fun setAccountNavView(navigationView: NavigationView, activity: Activity) {
            val gvu = GlobalVariablesUtil()
            val drawerLayout = GlobalVariables.drawer_layout
            val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)

            navigationView.menu.clear() // Clear existing menu items
            navigationView.inflateMenu(R.menu.activity_account_drawer)
            navigationView.setupWithNavController(GlobalVariables.nav_controller)

            var headerView = navigationView.getHeaderView(0)
            navigationView.removeHeaderView(headerView)

            headerView = navigationView.inflateHeaderView(R.layout.nav_header_account)
            val accountNameTextView: TextView = headerView.findViewById(R.id.navHeader_accountName)
            val accountUrlTextView: TextView = headerView.findViewById(R.id.navHeader_accountUrl)
            accountNameTextView.text = gvu.getAccount().accountName
            accountUrlTextView.text = gvu.getUrlDomain()

            val toggle = ActionBarDrawerToggle(
                activity,
                drawerLayout,
                toolbar, // Replace with your toolbar instance
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )

            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            navigationView.setNavigationItemSelectedListener { menuItem ->
                // Handle navigation item clicks here
                when (menuItem.itemId) {
                    R.id.nav_dev_console -> {
                        //val navController = Navigation.findNavController(view)
                        GlobalVariables.nav_controller.navigate(R.id.nav_run_apex)
                    }
                    R.id.nav_logs -> {
                        // TODO: Log fragment
                    }
                    R.id.nav_soql -> {
                        // TODO: SOQL fragment
                    }
                    R.id.nav_limits -> {
                        // TODO: Limits fragment
                    }
                    R.id.nav_back -> {
                        //val navController = Navigation.findNavController(view)
                        GlobalVariables.nav_controller.navigate(R.id.nav_view_salesforce_accounts)
                    }
                }

                // Close the drawer after handling the click
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }

        }
    }
}