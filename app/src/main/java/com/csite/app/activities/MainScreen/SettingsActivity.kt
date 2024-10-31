package com.csite.app.activities.MainScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.R
import com.csite.app.activities.UserRegistrationAndLogin.UserRegistration

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Back button
    fun backButton(view: View){
        finish()
    }

    // Log out button
    fun logOutButton(view: View){
        val editor = getSharedPreferences("isSignedIn", MODE_PRIVATE).edit()
        editor.putBoolean("isSignedIn", false)
        editor.commit()
        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()
        val userRegistrationIntent = Intent(this, UserRegistration::class.java)
        startActivity(userRegistrationIntent)
        finishAffinity()

    }
}