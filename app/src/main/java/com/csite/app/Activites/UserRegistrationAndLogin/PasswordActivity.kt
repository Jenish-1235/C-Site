package com.csite.app.Activites.UserRegistrationAndLogin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import at.favre.lib.crypto.bcrypt.BCrypt
import com.csite.app.FirebaseOperations.FirebaseOperationsForMembers
import com.csite.app.Objects.Member
import com.csite.app.R
import com.csite.app.Activites.MainScreen.MainActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PasswordActivity : AppCompatActivity() {

    val memberReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Members")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val passwordActivityIntent : Intent = intent
        val mobileNumber : String = passwordActivityIntent.getStringExtra("mobileNumber").toString()
        val isMember : Boolean = passwordActivityIntent.getBooleanExtra("isMember", false)



        val heading : TextView = findViewById(R.id.heading)
        val setOrEnterPassword : TextView = findViewById(R.id.setOrEnterPassword)
        val nameEnter : TextInputEditText = findViewById(R.id.nameEnter)
        val passwordEnter : TextInputEditText = findViewById(R.id.passwordEnter)
        val passwordVerify : TextInputEditText = findViewById(R.id.passwordVerify)

        // Get views
        val passwordVerifyLayout : com.google.android.material.textfield.TextInputLayout = findViewById(R.id.passwordVerifyLayout)
        val nameEnterLayout : com.google.android.material.textfield.TextInputLayout = findViewById(R.id.nameEnterLayout)
        val verifyButton: Button = findViewById(R.id.verifyButton)


        // Set or enter password based on whether the user is a member or not
        if (isMember){
            heading.text = "Login to Your Account"
            setOrEnterPassword.text = "Enter password for +91$mobileNumber"

            passwordVerifyLayout.visibility = android.view.View.GONE
            nameEnterLayout.visibility = android.view.View.GONE

            verifyButton.text = "Verify"

            verifyButton.setOnClickListener {
                if (passwordEnter.text.toString().isEmpty()){
                    passwordEnter.error = "Please enter your password"
                    return@setOnClickListener
                }else{
                    passwordVerify(passwordEnter.text.toString(), mobileNumber)
                }
            }
        }
        else{
            heading.text = "Create Profile"
            setOrEnterPassword.text = "Set password for +91$mobileNumber"

            passwordVerifyLayout.visibility = android.view.View.VISIBLE
            nameEnterLayout.visibility = android.view.View.VISIBLE

            verifyButton.text = "Set Password"

            verifyButton.setOnClickListener {
                if (passwordEnter.text.toString() != passwordVerify.text.toString() || passwordEnter.text.toString().isEmpty() || passwordVerify.text.toString().isEmpty()){
                    passwordVerify.error = "Passwords do not match"
                    return@setOnClickListener
                }else if (nameEnter.text.toString().isEmpty()){
                    nameEnter.error = "Please enter your name"
                    return@setOnClickListener
                }else{
                    passwordSet(mobileNumber, nameEnter.text.toString().trim(), passwordHash(passwordEnter.text.toString()))
                }
            }
        }
    }

    // Hash password
    fun passwordHash(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    // Verify password for existing member
    fun passwordVerify(password: String, mobileNumber: String){
        FirebaseOperationsForMembers().getMemberFromDatabase(memberReference, mobileNumber, object : FirebaseOperationsForMembers.MemberCallback {
            override fun onMemberRetrieved(member: Member) {
                // Verify password
                if (BCrypt.verifyer().verify(password.toCharArray(), member.password).verified){
                    Toast.makeText(this@PasswordActivity, "Password verified", Toast.LENGTH_SHORT).show()

                    // Update login shared preferences
                    updateLoginSharedPreferences(member)


                    // Navigate to main activity
                    val mainActivityIntent : Intent = Intent(this@PasswordActivity, MainActivity::class.java)
                    startActivity(mainActivityIntent)
                    finish()
                    finishAffinity()
                }else{
                    Toast.makeText(this@PasswordActivity, "Password not verified", Toast.LENGTH_SHORT).show()
                }
            }
        })


    }

    // Set password for new member
    fun passwordSet(mobileNumber: String, name: String, password: String){

        val member : Member = Member(name, mobileNumber, password, "admin")
        FirebaseOperationsForMembers().saveNewMemberToDatabase(memberReference, member)
        Toast.makeText(this, "Created Account", Toast.LENGTH_SHORT).show()

        // Navigate to main activity
        updateLoginSharedPreferences(member)

        val mainActivityIntent : Intent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)

        finish()
        finishAffinity()


    }

    // Update login shared preferences
    fun updateLoginSharedPreferences(member: Member){
        var isSignedIn : SharedPreferences = getSharedPreferences("isSignedIn", MODE_PRIVATE)
        var editor : SharedPreferences.Editor = isSignedIn.edit()
        editor.putBoolean("isSignedIn", true)
        editor.commit()

        var mobileNumber : SharedPreferences = getSharedPreferences("mobileNumber", MODE_PRIVATE)
        var editorMobileNumber : SharedPreferences.Editor = mobileNumber.edit()
        editorMobileNumber.putString("mobileNumber", member.mobileNumber)
        editorMobileNumber.commit()

        var memberAccess : SharedPreferences = getSharedPreferences("memberAccess", MODE_PRIVATE)
        var editorMemberAccess : SharedPreferences.Editor = memberAccess.edit()
        editorMemberAccess.putString("memberAccess", member.memberAccess)
        editorMemberAccess.commit()

        var memberName : SharedPreferences = getSharedPreferences("memberName", MODE_PRIVATE)
        var editorMemberName : SharedPreferences.Editor = memberName.edit()
        editorMemberName.putString("memberName", member.name)
        editorMemberName.commit()


    }

}