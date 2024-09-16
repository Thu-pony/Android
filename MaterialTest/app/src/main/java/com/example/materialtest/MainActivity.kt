package com.example.materialtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_launcher_background)
        }
        val nav = findViewById<NavigationView>(R.id.nav_view)
        nav.setCheckedItem(R.id.navCall)
        nav.setNavigationItemSelectedListener {
            Toast.makeText(this,"点击了 ${it.itemId}",Toast.LENGTH_LONG).show()
            true
        }
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {view -> Snackbar.make(view,"Data deleted",Snackbar.LENGTH_SHORT)
            .setAction("Undo") {
                Toast.makeText(this,"Data Restroed", Toast.LENGTH_LONG).show()
            }.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.backup -> Toast.makeText(this,"Backup",Toast.LENGTH_LONG).show()
            R.id.delete -> Toast.makeText(this,"Delete",Toast.LENGTH_LONG).show()
            R.id.settings -> Toast.makeText(this,"setting",Toast.LENGTH_LONG).show()
            android.R.id.home -> {
                val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
                drawerLayout.openDrawer(GravityCompat.START)

            }
        }
        return true
    }
}