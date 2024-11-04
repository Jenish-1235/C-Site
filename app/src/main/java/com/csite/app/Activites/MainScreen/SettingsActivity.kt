package com.csite.app.Activites.MainScreen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.R
import com.csite.app.Activites.UserRegistrationAndLogin.UserRegistration

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


        var mobileNumber : SharedPreferences = getSharedPreferences("mobileNumber", MODE_PRIVATE)
        var editorMobileNumber : SharedPreferences.Editor = mobileNumber.edit()
        editorMobileNumber.putString("mobileNumber", null)
        editorMobileNumber.commit()

        var memberAccess : SharedPreferences = getSharedPreferences("memberAccess", MODE_PRIVATE)
        var editorMemberAccess : SharedPreferences.Editor = memberAccess.edit()
        editorMemberAccess.putString("memberAccess", null)
        editorMemberAccess.commit()

        var memberName : SharedPreferences = getSharedPreferences("memberName", MODE_PRIVATE)
        var editorMemberName : SharedPreferences.Editor = memberName.edit()
        editorMemberName.putString("memberName", null)
        editorMemberName.commit()

        intent = Intent(this, UserRegistration::class.java)
        startActivity(intent)
        finishAffinity()

    }
}