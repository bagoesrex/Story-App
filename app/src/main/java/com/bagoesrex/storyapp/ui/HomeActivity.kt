package com.bagoesrex.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.bagoesrex.storyapp.R
import com.bagoesrex.storyapp.data.pref.UserPreferences
import com.bagoesrex.storyapp.databinding.ActivityHomeBinding
import com.bagoesrex.storyapp.utils.showToast

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var userPreferences: UserPreferences
    private var doubleBackToExitPressed = false

    private var selectedFragmentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences(this)
        replaceFragment()
        setupUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater.inflate(R.menu.appbar_menu, it)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_setting -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                true
            }

            R.id.menu_logout -> {
                userPreferences.clearToken()
                showToast(this, getString(R.string.logout_success))

                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }, 1000)

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbarHome.toolbar)

        binding.addFab.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressed) {
                    finishAffinity()
                    return
                }

                doubleBackToExitPressed = true
                showToast(this@HomeActivity, getString(R.string.press_again_for_exit))
                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackToExitPressed = false
                }, 3000)
            }
        })

        binding.mainBottomBar.setOnItemSelectedListener { position ->
            if (position != selectedFragmentIndex) {
                selectedFragmentIndex = position

                loadFragment(position)
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun replaceFragment(fragment: Fragment = StoryFragment()) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun loadFragment(index: Int) {
        val selectedFragment = when (index) {
            0 -> StoryFragment()
            1 -> StoryMapFragment()
            else -> StoryFragment()
        }

        replaceFragment(selectedFragment)
    }
}
