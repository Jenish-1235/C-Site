package com.csite.app.Activites.MainScreen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.R
import com.csite.app.Activites.UserRegistrationAndLogin.UserRegistration
import com.csite.app.Fragments.MainScreen.PartyFragment
import com.csite.app.Fragments.MainScreen.ProjectFragment
import com.csite.app.Fragments.MainScreen.QuotationFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private val prefs_name = "MyPrefsFile"
    private val prefs_isSignedIn = "isSignedIn"
    private lateinit var metaReference : DatabaseReference
    private lateinit var memberReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val view : View = findViewById(R.id.main)

        // Check for first launch of app and if so, show the registration screen
        if(!goToRegistration()){
            Toast.makeText(this, "Welcome to C-Site", Toast.LENGTH_SHORT).show()
            try {

                // Initialize Firebase
                FirebaseApp.initializeApp(this)

                // Initialize database references
                metaReference = FirebaseDatabase.getInstance().getReference("Meta")
                memberReference = FirebaseDatabase.getInstance().getReference("Members")

                // set company name and check working of firebase connection
                val companyName : TextView = findViewById(R.id.companyName)
                metaReference.child("companyName").get().addOnSuccessListener {
                    companyName.text = it.value.toString()
                }

                // check roll of mobile number for admin or manager
                val mobileNumber: String = getSharedPreferences("mobileNumber", MODE_PRIVATE).getString("mobileNumber", "").toString()
                memberReference.child(mobileNumber).child("memberAccess").get().addOnSuccessListener {
                    if(it.value.toString() == "admin"){
                        bottomTabLayoutFormationForAdmins(view)
                        val memberAccess: SharedPreferences = getSharedPreferences("memberAccess", MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = memberAccess.edit()
                        editor.putString("memberAccess", "admin")
                        editor.commit()
                    }else if (it.value.toString() == "super admin"){
                        bottomTabLayoutFormationForAdmins(view)
                        val memberAccess: SharedPreferences = getSharedPreferences("memberAccess", MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = memberAccess.edit()
                        editor.putString("memberAccess", "super admin")
                        editor.commit()
                    }else if (it.value.toString() == "manager"){
                        bottomTabLayoutFormationForManagers(view)
                        val memberAccess: SharedPreferences = getSharedPreferences("memberAccess", MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = memberAccess.edit()
                        editor.putString("memberAccess", "manager")
                        editor.commit()
                    }else{
                        Toast.makeText(this, "You are not authorized to access this app", Toast.LENGTH_SHORT).show()
                        val isFirstTime : SharedPreferences = getSharedPreferences(prefs_name, MODE_PRIVATE)
                        val isSignedIn : SharedPreferences = getSharedPreferences(prefs_isSignedIn, MODE_PRIVATE)
                        val editor : SharedPreferences.Editor = isFirstTime.edit()
                        editor.putBoolean("firstTime", true)
                        editor.commit()
                        val editor2 : SharedPreferences.Editor = isSignedIn.edit()
                        editor2.putBoolean("isSignedIn", false)
                        editor2.commit()

                        var mobileNumberPreference : SharedPreferences = getSharedPreferences("mobileNumber", MODE_PRIVATE)
                        var editorMobileNumber : SharedPreferences.Editor = mobileNumberPreference.edit()
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
                        finish()
                        goToRegistration()

                    }
                }

            }catch (e : Exception){
                Toast.makeText(this, "Firebase Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Set settings icon click listener
        val settingsIcon : ImageView = findViewById(R.id.settingsIcon)
        settingsIcon.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }

    // Check for first launch of app and if so, show the registration screen
   fun goToRegistration(): Boolean {
        val isFirstTime : SharedPreferences = getSharedPreferences(prefs_name, MODE_PRIVATE)
        val isSignedIn : SharedPreferences = getSharedPreferences(prefs_isSignedIn, MODE_PRIVATE)

        if (isFirstTime.getBoolean("firstTime", true) || !isSignedIn.getBoolean("isSignedIn", false)) {

            var userRegistrationIntent : Intent = Intent(this, UserRegistration::class.java)
            startActivity(userRegistrationIntent)

            var editor : SharedPreferences.Editor = isFirstTime.edit()
            editor.putBoolean("firstTime", false)
            editor.commit()
            finish()

            return true
        }
        return false
    }

    // Bottom tab layout formation for admins
    private fun bottomTabLayoutFormationForAdmins(view: View){

        var bottomTabLayout : com.google.android.material.tabs.TabLayout = findViewById(R.id.mainScreenBottomTabLayout)
        bottomTabLayout.setSelectedTabIndicatorColor(Color.WHITE)
        bottomTabLayout.setTabTextColors(Color.WHITE, Color.WHITE)
        bottomTabLayout.setSelectedTabIndicatorHeight(0)

        val mainScreenFrameLayout : FrameLayout = findViewById(R.id.mainScreenFrameLayout)


        var quotationTab : com.google.android.material.tabs.TabLayout.Tab = bottomTabLayout.newTab()
        val quotationTabView : View = layoutInflater.inflate(R.layout.bottom_tab_layout_item, null)
        val quotationTabImageView : ImageView = quotationTabView.findViewById(R.id.tab_icon)
        val quotationTabTextView : TextView = quotationTabView.findViewById(R.id.tab_text)
        quotationTabImageView.setImageResource(R.drawable.quotation_icon_yellow)
        quotationTabTextView.text = "Quotation"
        quotationTab.customView = quotationTabView
        bottomTabLayout.addTab(quotationTab)

        var projectTab : com.google.android.material.tabs.TabLayout.Tab = bottomTabLayout.newTab()
        val projectTabView : View = layoutInflater.inflate(R.layout.bottom_tab_layout_item, null)
        val projectTabImageView : ImageView = projectTabView.findViewById(R.id.tab_icon)
        val projectTabTextView : TextView = projectTabView.findViewById(R.id.tab_text)
        projectTabImageView.setImageResource(R.drawable.project_icon_yellow)
        projectTabTextView.text = "Project"
        projectTab.customView = projectTabView
        bottomTabLayout.addTab(projectTab)

        var partyTab : com.google.android.material.tabs.TabLayout.Tab = bottomTabLayout.newTab()
        val partyTabView : View = layoutInflater.inflate(R.layout.bottom_tab_layout_item, null)
        val partyTabImageView : ImageView = partyTabView.findViewById(R.id.tab_icon)
        val partyTabTextView : TextView = partyTabView.findViewById(R.id.tab_text)
        partyTabImageView.setImageResource(R.drawable.party_icon_yellow)
        partyTabTextView.text = "Party"
        partyTab.customView = partyTabView
        bottomTabLayout.addTab(partyTab)

        bottomTabLayout.isSmoothScrollingEnabled = true


        val bottomTabLayoutItemSelectedListener: OnTabSelectedListener = object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                when (position) {
                    0 -> {
                        Toast.makeText(this@MainActivity, "Quotation", Toast.LENGTH_SHORT).show()
                        quotationTabImageView.setImageResource(R.drawable.quotation_icon_yellow)
                        quotationTabTextView.setTextColor(Color.rgb(120,92,42))
                        projectTabImageView.setImageResource(R.drawable.project_icon_black)
                        projectTabTextView.setTextColor(Color.BLACK)
                        partyTabImageView.setImageResource(R.drawable.party_icon_black)
                        partyTabTextView.setTextColor(Color.BLACK)

                        mainScreenFrameLayout.removeAllViews()
                        val quotationFragment = QuotationFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.mainScreenFrameLayout, quotationFragment).commit()

                    }
                    1 -> {
                        Toast.makeText(this@MainActivity, "Project", Toast.LENGTH_SHORT).show()
                        quotationTabImageView.setImageResource(R.drawable.quotation_icon_black)
                        quotationTabTextView.setTextColor(Color.BLACK)
                        projectTabImageView.setImageResource(R.drawable.project_icon_yellow)
                        projectTabTextView.setTextColor(Color.rgb(120,92,42))
                        partyTabImageView.setImageResource(R.drawable.party_icon_black)
                        partyTabTextView.setTextColor(Color.BLACK)
                        mainScreenFrameLayout.removeAllViews()
                        val projectFragment = ProjectFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.mainScreenFrameLayout, projectFragment).commit()

                    }
                    2 -> {
                        Toast.makeText(this@MainActivity, "Party", Toast.LENGTH_SHORT).show()
                        quotationTabImageView.setImageResource(R.drawable.quotation_icon_black)
                        quotationTabTextView.setTextColor(Color.BLACK)
                        projectTabImageView.setImageResource(R.drawable.project_icon_black)
                        projectTabTextView.setTextColor(Color.BLACK)
                        partyTabImageView.setImageResource(R.drawable.party_icon_yellow)
                        partyTabTextView.setTextColor(Color.rgb(120,92,42))
                        mainScreenFrameLayout.removeAllViews()
                        val partyFragment = PartyFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.mainScreenFrameLayout, partyFragment).commit()
                        val sharedPreferences = getSharedPreferences("projectId", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("projectId", "")
                        editor.commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val position = tab?.position
                when (position) {
                    0 -> {
                        Toast.makeText(this@MainActivity, "Quotation", Toast.LENGTH_SHORT).show()
                        quotationTabImageView.setImageResource(R.drawable.quotation_icon_yellow)
                        quotationTabTextView.setTextColor(Color.rgb(120,92,42))
                        projectTabImageView.setImageResource(R.drawable.project_icon_black)
                        projectTabTextView.setTextColor(Color.BLACK)
                        partyTabImageView.setImageResource(R.drawable.party_icon_black)
                        partyTabTextView.setTextColor(Color.BLACK)

                        mainScreenFrameLayout.removeAllViews()
                        val quotationFragment = QuotationFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.mainScreenFrameLayout, quotationFragment).commit()

                    }
                    1 -> {
                        Toast.makeText(this@MainActivity, "Project", Toast.LENGTH_SHORT).show()
                        quotationTabImageView.setImageResource(R.drawable.quotation_icon_black)
                        quotationTabTextView.setTextColor(Color.BLACK)
                        projectTabImageView.setImageResource(R.drawable.project_icon_yellow)
                        projectTabTextView.setTextColor(Color.rgb(120,92,42))
                        partyTabImageView.setImageResource(R.drawable.party_icon_black)
                        partyTabTextView.setTextColor(Color.BLACK)
                        mainScreenFrameLayout.removeAllViews()
                        val projectFragment = ProjectFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.mainScreenFrameLayout, projectFragment).commit()

                    }
                    2 -> {
                        Toast.makeText(this@MainActivity, "Party", Toast.LENGTH_SHORT).show()
                        quotationTabImageView.setImageResource(R.drawable.quotation_icon_black)
                        quotationTabTextView.setTextColor(Color.BLACK)
                        projectTabImageView.setImageResource(R.drawable.project_icon_black)
                        projectTabTextView.setTextColor(Color.BLACK)
                        partyTabImageView.setImageResource(R.drawable.party_icon_yellow)
                        partyTabTextView.setTextColor(Color.rgb(120,92,42))
                        mainScreenFrameLayout.removeAllViews()
                        val partyFragment = PartyFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.mainScreenFrameLayout, partyFragment).commit()
                        val sharedPreferences = getSharedPreferences("projectId", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("projectId", "")
                        editor.commit()
                    }
                }
            }

        }

        bottomTabLayout.addOnTabSelectedListener(bottomTabLayoutItemSelectedListener)

        bottomTabLayout.selectTab(projectTab)
    }

    // Bottom tab layout formation for managers
    private fun bottomTabLayoutFormationForManagers(view: View){

        var bottomTabLayout : com.google.android.material.tabs.TabLayout = findViewById(R.id.mainScreenBottomTabLayout)
        bottomTabLayout.setSelectedTabIndicatorColor(Color.WHITE)
        bottomTabLayout.setTabTextColors(Color.WHITE, Color.WHITE)
        bottomTabLayout.setSelectedTabIndicatorHeight(0)
//        bottomTabLayout.removeAllTabs()


        var projectTab : TabLayout.Tab = bottomTabLayout.newTab()
        val projectTabView : View = layoutInflater.inflate(R.layout.bottom_tab_layout_item, null)
        val projectTabImageView : ImageView = projectTabView.findViewById(R.id.tab_icon)
        val projectTabTextView : TextView = projectTabView.findViewById(R.id.tab_text)
        projectTabImageView.setImageResource(R.drawable.project_icon_yellow)
        projectTabTextView.text = "Project"
        projectTab.customView = projectTabView
        bottomTabLayout.addTab(projectTab)


        bottomTabLayout.isSmoothScrollingEnabled = true
        bottomTabLayout.selectTab(projectTab)
        projectTabImageView.setImageResource(R.drawable.project_icon_yellow)
        projectTabTextView.setTextColor(Color.rgb(120,92,42))
        val mainScreenFrameLayout : FrameLayout = findViewById(R.id.mainScreenFrameLayout)
        mainScreenFrameLayout.removeAllViews()
        val projectFragment = ProjectFragment()
        supportFragmentManager.beginTransaction().replace(R.id.mainScreenFrameLayout, projectFragment).commit()
    }

}