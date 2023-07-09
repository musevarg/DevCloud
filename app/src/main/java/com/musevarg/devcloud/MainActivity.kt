package com.musevarg.devcloud

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.musevarg.devcloud.databinding.ActivityMainBinding
import com.musevarg.devcloud.ui.add_salesforce_account.AddSalesforceAccountDialog

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_add_salesforce_account, R.id.nav_view_salesforce_accounts, R.id.nav_about), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation item clicks here
        when (item.itemId) {
            R.id.nav_add_salesforce_account -> {
                //Toast.makeText( this, "Add Account Clicked", Toast.LENGTH_SHORT).show()
                AddSalesforceAccountDialog().show(supportFragmentManager, AddSalesforceAccountDialog.TAG)
            }
            R.id.nav_view_salesforce_accounts -> {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.nav_view_salesforce_accounts)
            }
            R.id.nav_about -> {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.nav_about)
            }
        }

        // Close the navigation drawer after handling the item click
        val drawerLayout: DrawerLayout = binding.drawerLayout
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }
}