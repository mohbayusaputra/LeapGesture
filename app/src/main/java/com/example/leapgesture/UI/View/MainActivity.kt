package com.example.leapgesture.UI.View

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.leapgesture.R
import com.example.leapgesture.UI.View.Home.HomeFragment
import com.example.leapgesture.UI.View.Profil.ProfilFragment
import com.example.leapgesture.UI.View.Riwayat.RiwayatFragment
import com.example.leapgesture.UI.View.UjiDL.DlFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        loadFragment(HomeFragment())

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                }
                R.id.uji_dl -> {
                    loadFragment(DlFragment())
                }
                R.id.riwayat -> {
                    loadFragment(RiwayatFragment())
                }
                R.id.profil -> {
                    loadFragment(ProfilFragment())
                }
            }
            true
        }
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}