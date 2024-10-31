package com.csite.app.activities.UserRegistrationAndLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.FirebaseOperations.FirebaseOperationsForMembers
import com.csite.app.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRegistration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_registration)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val memberReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Members")
        
        // Get Mobile Number Input
        val mobileNumberEditText : TextInputEditText = findViewById(R.id.mobileNumber)
        mobileNumberEditText.requestFocus()

        lateinit var mobileNumber : String

        // Get Sign Up Button
        val signUpButton : Button = findViewById(R.id.signUpButton)
        signUpButton.setOnClickListener {
            // Sign Up
            mobileNumber = mobileNumberEditText.text.toString().trim()
            if (mobileNumber.isEmpty() || mobileNumber.length != 10){
                mobileNumberEditText.error = "Please enter your mobile number"
                return@setOnClickListener
            }else{
                checkMemberExistence(memberReference, mobileNumber)
            }

        }



    }

    // Check if member exists or not in database
    fun checkMemberExistence(memberReference: DatabaseReference, mobileNumber: String){
        val firebaseOperationsForMembers: FirebaseOperationsForMembers = FirebaseOperationsForMembers()
        var passwordActivityIntent : Intent = Intent(this, PasswordActivity::class.java)
        firebaseOperationsForMembers.checkExistingMember(memberReference, mobileNumber, object :
            FirebaseOperationsForMembers.MemberExistenceCallback {
            override fun isMemberExists(exists: Boolean) {
                if (exists){
//                    Toast.makeText(this@UserRegistration , "Member exists", Toast.LENGTH_SHORT).show()
                    passwordActivityIntent.putExtra("mobileNumber", mobileNumber)
                    passwordActivityIntent.putExtra("isMember", true)
                    startActivity(passwordActivityIntent)

                }else{
//                    Toast.makeText(this@UserRegistration , "Member does not exist", Toast.LENGTH_SHORT).show()
                    passwordActivityIntent.putExtra("mobileNumber", mobileNumber)
                    passwordActivityIntent.putExtra("isMember", false)
                    startActivity(passwordActivityIntent)
                }
            }
        })



    }

}


