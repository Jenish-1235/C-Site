package com.csite.app.activities.MainScreen

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.R
import com.csite.app.activities.UserRegistrationAndLogin.UserRegistration
import com.csite.app.fragments.PartyFragment
import com.csite.app.fragments.ProjectFragment
import com.csite.app.fragments.QuotationFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    val prefs_name = "MyPrefsFile"
    val prefs_isSignedIn = "isSignedIn"
    lateinit var metaReference : DatabaseReference;
    lateinit var memberReference: DatabaseReference;

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
                var companyName : TextView = findViewById(R.id.companyName)
                metaReference.child("companyName").get().addOnSuccessListener {
                    companyName.text = it.value.toString()
                }

                // check roll of mobile number for admin or manager
                val mobileNumber: String = getSharedPreferences("mobileNumber", MODE_PRIVATE).getString("mobileNumber", "").toString()
                memberReference.child(mobileNumber).child("memberAccess").get().addOnSuccessListener {
                    if(it.value.toString() == "admin"){
                        bottomTabLayoutFormationForAdmins(view)
                    }else if (it.value.toString() == "manager"){
                        bottomTabLayoutFormationForManagers(view)
                    }
                }

            }catch (e : Exception){
                Toast.makeText(this, "Firebase Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }


        val settingsIcon : ImageView = findViewById(R.id.settingsIcon)
        settingsIcon.setOnClickListener {
            var settingsIntent : Intent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }

    // Check for first launch of app and if so, show the registration screen
    fun goToRegistration(): Boolean {
        var isFirstTime : SharedPreferences = getSharedPreferences(prefs_name, MODE_PRIVATE)
        var isSignedIn : SharedPreferences = getSharedPreferences(prefs_isSignedIn, MODE_PRIVATE)

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
    fun bottomTabLayoutFormationForAdmins(view: View){

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

        bottomTabLayout.selectTab(projectTab)
        projectTabImageView.setImageResource(R.drawable.project_icon_yellow)
        projectTabTextView.setTextColor(Color.rgb(120,92,42))
        quotationTabTextView.setTextColor(Color.BLACK)
        partyTabTextView.setTextColor(Color.BLACK)
        quotationTabImageView.setImageResource(R.drawable.quotation_icon_black)
        partyTabImageView.setImageResource(R.drawable.party_icon_black)
        mainScreenFrameLayout.removeAllViews()
        val projectFragment = ProjectFragment()
        supportFragmentManager.beginTransaction().replace(R.id.mainScreenFrameLayout, projectFragment).commit()

        bottomTabLayout.isSmoothScrollingEnabled = true


        val bottomTabLayoutItemSelectedListener: OnTabSelectedListener = object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                if (position == 0){
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

                }else if (position == 1){
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

                }else if (position == 2){
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
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val position = tab?.position
                if (position == 0){
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

                }else if (position == 1){
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

                }else if (position == 2){
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
                }
            }

        }

        bottomTabLayout.addOnTabSelectedListener(bottomTabLayoutItemSelectedListener)
    }

    // Bottom tab layout formation for managers
    fun bottomTabLayoutFormationForManagers(view: View){

        var bottomTabLayout : com.google.android.material.tabs.TabLayout = findViewById(R.id.mainScreenBottomTabLayout)
        bottomTabLayout.setSelectedTabIndicatorColor(Color.WHITE)
        bottomTabLayout.setTabTextColors(Color.WHITE, Color.WHITE)
        bottomTabLayout.setSelectedTabIndicatorHeight(0)
//        bottomTabLayout.removeAllTabs()


        var projectTab : com.google.android.material.tabs.TabLayout.Tab = bottomTabLayout.newTab()
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