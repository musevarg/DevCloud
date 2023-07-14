package com.musevarg.devcloud.global

import android.util.Log
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import com.google.android.material.navigation.NavigationView
import com.musevarg.devcloud.database.SavedAccount

class GlobalVariables {
    companion object {
        lateinit var selectedAccount : SavedAccount
        lateinit var nav_view : NavigationView
        lateinit var nav_controller : NavController
        lateinit var drawer_layout : DrawerLayout
        var isMainActivity : Boolean = true
    }
}

class GlobalVariablesUtil {

    fun setAccount(sAcc : SavedAccount) {
        GlobalVariables.selectedAccount = sAcc
        Log.d("GlobalVariables", "Saved Account:")
        Log.d("GlobalVariables", GlobalVariables.selectedAccount.toString())
    }

    fun getAccount(): SavedAccount {
        Log.d("GlobalVariables", "Returning Selected Account:")
        Log.d("GlobalVariables", GlobalVariables.selectedAccount.toString())
        return GlobalVariables.selectedAccount
    }

    fun getUrlDomain() : String {
        var url = GlobalVariables.selectedAccount.instanceUrl
        url = url.replace(".my.salesforce.com", "")
        url = url.replace(".sandbox", "")
        url = url.replace("https://", "")
        return url
    }

    fun setNavigationView(navView : NavigationView){
        GlobalVariables.nav_view = navView
        Log.d("GlobalVariables", "Saved Nav View")
    }

    fun getNavigationView() : NavigationView{
        Log.d("GlobalVariables", "Returning Nav View")
        return GlobalVariables.nav_view
    }

}