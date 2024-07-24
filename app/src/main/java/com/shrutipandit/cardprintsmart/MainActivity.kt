package com.shrutipandit.cardprintsmart

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.shrutipandit.cardprintsmart.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment
            )
        )

        // Update the toolbar title when the fragment changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.title = destination.label
        }

        setSupportActionBar(binding.toolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.aboutUsFragment -> {
                try {
                    navController.navigate(R.id.action_homeFragment_to_aboutUsragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                true
            }
            R.id.helpUsFragment -> {
                try {
                    navController.navigate(R.id.action_homeFragment_to_helpUsFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                true
            }
            R.id.rateus -> {
                openPlayStoreForRating()
                true
            }
            R.id.shareAppLink -> {
                shareAppLink()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareAppLink() {
        val packageName = packageName

        // Create a URI for the app on the Play Store
        val appStoreUri = Uri.Builder()
            .scheme("https")
            .authority("play.google.com")
            .appendPath("store")
            .appendPath("apps")
            .appendPath("details")
            .appendQueryParameter("id", packageName)
            .build()

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, appStoreUri.toString())
        }

        startActivity(Intent.createChooser(shareIntent, "Share App Link"))
    }

    private fun openPlayStoreForRating() {
        // Create a URI for the app on the Play Store
        val appStoreUri = Uri.parse("market://details?id=$packageName")

        val rateIntent = Intent(Intent.ACTION_VIEW, appStoreUri)

        // Ensure that the Play Store app is available on the device
        rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            startActivity(rateIntent)
        } catch (e: ActivityNotFoundException) {
            // In case the Play Store app is not available, open the Play Store website
            val webStoreUri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            val webRateIntent = Intent(Intent.ACTION_VIEW, webStoreUri)
            startActivity(webRateIntent)
        }
    }
}
