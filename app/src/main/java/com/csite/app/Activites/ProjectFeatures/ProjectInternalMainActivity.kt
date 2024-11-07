package com.csite.app.Activites.ProjectFeatures

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SharedMemory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.Fragments.ProjectInternal.ProjectInternalAttendanceFragment
import com.csite.app.Fragments.ProjectInternal.ProjectInternalMaterialFragment
import com.csite.app.Fragments.ProjectInternal.ProjectInternalPartyFragment
import com.csite.app.Fragments.ProjectInternal.ProjectInternalSiteFragment
import com.csite.app.Fragments.ProjectInternal.ProjectInternalTransactionFragment
import com.csite.app.R
import com.csite.app.databinding.ActivityProjectInternalMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class ProjectInternalMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProjectInternalMainBinding
    var projectId = ""
    var projectName = ""
    var projectLocation = ""
    var projectCity = ""
    var projectStartDate = ""
    var projectEndDate = ""
    var projectValue = ""
    var projectStatus = ""



    lateinit var memberAccessSharedPreference:SharedPreferences
    lateinit var memberAccessValue : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectInternalMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val projectInternalIntent = getIntent()
        projectId = projectInternalIntent.getStringExtra("projectId").toString()
        projectName = projectInternalIntent.getStringExtra("projectName").toString()
        projectLocation = projectInternalIntent.getStringExtra("projectLocation").toString()
        projectCity = projectInternalIntent.getStringExtra("projectCity").toString()
        projectStartDate = projectInternalIntent.getStringExtra("projectStartDate").toString()
        projectEndDate = projectInternalIntent.getStringExtra("projectEndDate").toString()
        projectValue = projectInternalIntent.getStringExtra("projectValue").toString()
        projectStatus = projectInternalIntent.getStringExtra("projectStatus").toString()
        binding.projectNameView.text = projectInternalIntent.getStringExtra("projectName")
        memberAccessSharedPreference = getSharedPreferences("memberAccess", MODE_PRIVATE)
        memberAccessValue = memberAccessSharedPreference.getString("memberAccess", "").toString()
        if (memberAccessValue.equals("manager")){
            projectInternalBottomTabLayoutForManager()
        }else{
            projectInternalBottomTabLayoutForAdmin()
        }

    }

    fun backButton(view: View){
        finish()
    }

    fun projectSettings(view: View){
        val projectSettingsIntent = Intent(this, ProjectSettingsActivity::class.java)
        projectSettingsIntent.putExtra("projectId", projectId)
        projectSettingsIntent.putExtra("projectName", projectName)
        projectSettingsIntent.putExtra("projectLocation", projectLocation)
        projectSettingsIntent.putExtra("projectCity", projectCity)
        projectSettingsIntent.putExtra("projectStartDate", projectStartDate)
        projectSettingsIntent.putExtra("projectEndDate", projectEndDate)
        projectSettingsIntent.putExtra("projectValue", projectValue)
        projectSettingsIntent.putExtra("projectStatus", projectStatus)
        startActivity(projectSettingsIntent)
    }

    fun projectInternalBottomTabLayoutForAdmin(){

        binding.projectInternalBottomTabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.white))
        binding.projectInternalBottomTabLayout.setTabTextColors(resources.getColor(R.color.black), resources.getColor(R.color.white))
        binding.projectInternalBottomTabLayout.setSelectedTabIndicatorHeight(0)
        binding.projectInternalBottomTabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        binding.projectInternalBottomTabLayout.tabGravity = TabLayout.GRAVITY_FILL


        val projectInternalPartyTab: TabLayout.Tab = binding.projectInternalBottomTabLayout.newTab()
        val projectInternalPartyTabView = layoutInflater.inflate(R.layout.bottom_tab_layout_item, null)
        val projectInternalPartyTabIcon = projectInternalPartyTabView.findViewById<ImageView>(R.id.tab_icon)
        projectInternalPartyTabIcon.setImageResource(R.drawable.party_icon_yellow)
        projectInternalPartyTabView.findViewById<TextView>(R.id.tab_text).text = "Party"
        projectInternalPartyTab.customView = projectInternalPartyTabView
        binding.projectInternalBottomTabLayout.addTab(projectInternalPartyTab)

        val projectInternalTransactionTab = binding.projectInternalBottomTabLayout.newTab()
        val projectInternalTransactionTabView = layoutInflater.inflate(R.layout.bottom_tab_layout_item, null)
        val projectInternalTransactionTabIcon = projectInternalTransactionTabView.findViewById<ImageView>(R.id.tab_icon)
        projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_black)
        projectInternalTransactionTabView.findViewById<TextView>(R.id.tab_text).text = "Transaction"
        projectInternalTransactionTab.customView = projectInternalTransactionTabView
        binding.projectInternalBottomTabLayout.addTab(projectInternalTransactionTab)

        val projectInternalSiteTab = binding.projectInternalBottomTabLayout.newTab()
        val projectInternalSiteTabView = layoutInflater.inflate(R.layout.bottom_tab_layout_item, null)
        val projectInternalSiteTabIcon = projectInternalSiteTabView.findViewById<ImageView>(R.id.tab_icon)
        projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
        projectInternalSiteTabView.findViewById<TextView>(R.id.tab_text).text = "Site"
        projectInternalSiteTab.customView = projectInternalSiteTabView
        binding.projectInternalBottomTabLayout.addTab(projectInternalSiteTab)

        val projectInternalAttendanceTab = binding.projectInternalBottomTabLayout.newTab()
        val projectInternalAttendanceTabView = layoutInflater.inflate(R.layout.bottom_tab_layout_item, null)
        val projectInternalAttendanceTabIcon = projectInternalAttendanceTabView.findViewById<ImageView>(R.id.tab_icon)
        projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
        projectInternalAttendanceTabView.findViewById<TextView>(R.id.tab_text).text = "Attendance"
        projectInternalAttendanceTab.customView = projectInternalAttendanceTabView
        binding.projectInternalBottomTabLayout.addTab(projectInternalAttendanceTab)

        val projectInternalMaterialTab = binding.projectInternalBottomTabLayout.newTab()
        val projectInternalMaterialTabView = layoutInflater.inflate(R.layout.bottom_tab_layout_item, null)
        val projectInternalMaterialTabIcon = projectInternalMaterialTabView.findViewById<ImageView>(R.id.tab_icon)
        projectInternalMaterialTabIcon.setImageResource(R.drawable.material)
        projectInternalMaterialTabView.findViewById<TextView>(R.id.tab_text).text = "Material"
        projectInternalMaterialTab.customView = projectInternalMaterialTabView
        binding.projectInternalBottomTabLayout.addTab(projectInternalMaterialTab)

        val bundle = Bundle()
        bundle.putString("projectId", projectId)
        bundle.putString("memberAccess", "admin")
        bundle.putString("projectName", binding.projectNameView.text.toString())


        binding.projectInternalBottomTabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {

                        val projectInternalPartyFragment: ProjectInternalPartyFragment = ProjectInternalPartyFragment()
                        projectInternalPartyFragment.arguments = bundle
                        supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalPartyFragment).commit()
                        projectInternalPartyTabIcon.setImageResource(R.drawable.party_icon_black)
                        projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_yellow)
                        projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
                        projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
                        projectInternalMaterialTabIcon.setImageResource(R.drawable.material)

                    }
                    1 -> {
                        val projectInternalTransactionFragment: ProjectInternalTransactionFragment = ProjectInternalTransactionFragment()
                        projectInternalTransactionFragment.arguments = bundle
                        supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalTransactionFragment).commit()
                        projectInternalPartyTabIcon.setImageResource(R.drawable.party_icon_yellow)
                        projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_black)
                        projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
                        projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
                        projectInternalMaterialTabIcon.setImageResource(R.drawable.material)
                    }
                    2 -> {
                        val projectInternalSiteFragment: ProjectInternalSiteFragment = ProjectInternalSiteFragment()
                        projectInternalSiteFragment.arguments = bundle
                        supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalSiteFragment).commit()
                        projectInternalPartyTabIcon.setImageResource(R.drawable.party_icon_yellow)
                        projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_yellow)
                        projectInternalSiteTabIcon.setImageResource(R.drawable.site_icon_black)
                        projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
                        projectInternalMaterialTabIcon.setImageResource(R.drawable.material)
                    }
                    3 -> {
                        val projectInternalAttendanceFragment: ProjectInternalAttendanceFragment = ProjectInternalAttendanceFragment()
                        projectInternalAttendanceFragment.arguments = bundle
                        supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalAttendanceFragment).commit()
                        projectInternalPartyTabIcon.setImageResource(R.drawable.party_icon_yellow)
                        projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_yellow)
                        projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
                        projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance_icon_black)
                        projectInternalMaterialTabIcon.setImageResource(R.drawable.material)
                    }
                    4 -> {
                        val projectInternalMaterialFragment: ProjectInternalMaterialFragment = ProjectInternalMaterialFragment()
                        projectInternalMaterialFragment.arguments = bundle
                        supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalMaterialFragment).commit()
                        projectInternalPartyTabIcon.setImageResource(R.drawable.party_icon_yellow)
                        projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_yellow)
                        projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
                        projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
                        projectInternalMaterialTabIcon.setImageResource(R.drawable.material_icon_black)
                    }
                    else -> {
                        val projectInternalTransactionFragment: ProjectInternalTransactionFragment = ProjectInternalTransactionFragment()
                        projectInternalTransactionFragment.arguments = bundle
                        supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalTransactionFragment).commit()
                        projectInternalPartyTabIcon.setImageResource(R.drawable.party_icon_yellow)
                        projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_black)
                        projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
                        projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
                        projectInternalMaterialTabIcon.setImageResource(R.drawable.material)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        projectInternalTransactionTab.select()

    }

    fun projectInternalBottomTabLayoutForManager(){

        binding.projectInternalBottomTabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.white))
        binding.projectInternalBottomTabLayout.setTabTextColors(resources.getColor(R.color.black), resources.getColor(R.color.white))
        binding.projectInternalBottomTabLayout.setSelectedTabIndicatorHeight(0)
        binding.projectInternalBottomTabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        binding.projectInternalBottomTabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val projectInternalTransactionTab = binding.projectInternalBottomTabLayout.newTab()
        val projectInternalTransactionTabView = layoutInflater.inflate(R.layout.bottom_tab_layout_item, null)
        val projectInternalTransactionTabIcon = projectInternalTransactionTabView.findViewById<ImageView>(R.id.tab_icon)
        projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_black)
        projectInternalTransactionTabView.findViewById<TextView>(R.id.tab_text).text = "Transaction"
        projectInternalTransactionTab.customView = projectInternalTransactionTabView
        binding.projectInternalBottomTabLayout.addTab(projectInternalTransactionTab)

        val projectInternalSiteTab = binding.projectInternalBottomTabLayout.newTab()
        val projectInternalSiteTabView = layoutInflater.inflate(R.layout.bottom_tab_layout_item, null)
        val projectInternalSiteTabIcon = projectInternalSiteTabView.findViewById<ImageView>(R.id.tab_icon)
        projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
        projectInternalSiteTabView.findViewById<TextView>(R.id.tab_text).text = "Site"
        projectInternalSiteTab.customView = projectInternalSiteTabView
        binding.projectInternalBottomTabLayout.addTab(projectInternalSiteTab)

        val projectInternalAttendanceTab = binding.projectInternalBottomTabLayout.newTab()
        val projectInternalAttendanceTabView = layoutInflater.inflate(R.layout.bottom_tab_layout_item, null)
        val projectInternalAttendanceTabIcon = projectInternalAttendanceTabView.findViewById<ImageView>(R.id.tab_icon)
        projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
        projectInternalAttendanceTabView.findViewById<TextView>(R.id.tab_text).text = "Attendance"
        projectInternalAttendanceTab.customView = projectInternalAttendanceTabView
        binding.projectInternalBottomTabLayout.addTab(projectInternalAttendanceTab)

        val projectInternalMaterialTab = binding.projectInternalBottomTabLayout.newTab()
        val projectInternalMaterialTabView = layoutInflater.inflate(R.layout.bottom_tab_layout_item, null)
        val projectInternalMaterialTabIcon = projectInternalMaterialTabView.findViewById<ImageView>(R.id.tab_icon)
        projectInternalMaterialTabIcon.setImageResource(R.drawable.material)
        projectInternalMaterialTabView.findViewById<TextView>(R.id.tab_text).text = "Material"
        projectInternalMaterialTab.customView = projectInternalMaterialTabView
        binding.projectInternalBottomTabLayout.addTab(projectInternalMaterialTab)

        val bundle = Bundle()
        bundle.putString("projectId", projectId)
        bundle.putString("memberAccess", "manager")


        binding.projectInternalBottomTabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {

                    0 -> {
                        val projectInternalTransactionFragment: ProjectInternalTransactionFragment = ProjectInternalTransactionFragment()
                        projectInternalTransactionFragment.arguments = bundle
                        supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalTransactionFragment).commit()
                        projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_black)
                        projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
                        projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
                        projectInternalMaterialTabIcon.setImageResource(R.drawable.material)
                    }
                    1 -> {
                        val projectInternalSiteFragment: ProjectInternalSiteFragment = ProjectInternalSiteFragment()
                        projectInternalSiteFragment.arguments = bundle
                        supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalSiteFragment).commit()
                        projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_yellow)
                        projectInternalSiteTabIcon.setImageResource(R.drawable.site_icon_black)
                        projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
                        projectInternalMaterialTabIcon.setImageResource(R.drawable.material)
                    }
                    2 -> {
                        val projectInternalAttendanceFragment: ProjectInternalAttendanceFragment = ProjectInternalAttendanceFragment()
                        projectInternalAttendanceFragment.arguments = bundle
                        supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalAttendanceFragment).commit()
                        projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_yellow)
                        projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
                        projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance_icon_black)
                        projectInternalMaterialTabIcon.setImageResource(R.drawable.material)
                    }
                    3 -> {
                        val projectInternalMaterialFragment: ProjectInternalMaterialFragment = ProjectInternalMaterialFragment()
                        projectInternalMaterialFragment.arguments = bundle
                        supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalMaterialFragment).commit()
                        projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_yellow)
                        projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
                        projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
                        projectInternalMaterialTabIcon.setImageResource(R.drawable.material_icon_black)
                    }
                    else -> {
                        val projectInternalTransactionFragment: ProjectInternalTransactionFragment = ProjectInternalTransactionFragment()
                        projectInternalTransactionFragment.arguments = bundle
                        supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalTransactionFragment).commit()
                        projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_black)
                        projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
                        projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
                        projectInternalMaterialTabIcon.setImageResource(R.drawable.material)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) { when (tab?.position) {

                0 -> {
                    val projectInternalTransactionFragment: ProjectInternalTransactionFragment = ProjectInternalTransactionFragment()
                    projectInternalTransactionFragment.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalTransactionFragment).commit()
                    projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_black)
                    projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
                    projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
                    projectInternalMaterialTabIcon.setImageResource(R.drawable.material)
                }
                1 -> {
                    val projectInternalSiteFragment: ProjectInternalSiteFragment = ProjectInternalSiteFragment()
                    projectInternalSiteFragment.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalSiteFragment).commit()
                    projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_yellow)
                    projectInternalSiteTabIcon.setImageResource(R.drawable.site_icon_black)
                    projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
                    projectInternalMaterialTabIcon.setImageResource(R.drawable.material)
                }
                2 -> {
                    val projectInternalAttendanceFragment: ProjectInternalAttendanceFragment = ProjectInternalAttendanceFragment()
                    projectInternalAttendanceFragment.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalAttendanceFragment).commit()
                    projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_yellow)
                    projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
                    projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance_icon_black)
                    projectInternalMaterialTabIcon.setImageResource(R.drawable.material)
                }
                3 -> {
                    val projectInternalMaterialFragment: ProjectInternalMaterialFragment = ProjectInternalMaterialFragment()
                    projectInternalMaterialFragment.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalMaterialFragment).commit()
                    projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_yellow)
                    projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
                    projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
                    projectInternalMaterialTabIcon.setImageResource(R.drawable.material_icon_black)
                }
                else -> {
                    val projectInternalTransactionFragment: ProjectInternalTransactionFragment = ProjectInternalTransactionFragment()
                    projectInternalTransactionFragment.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.projectInternalFrameLayout, projectInternalTransactionFragment).commit()
                    projectInternalTransactionTabIcon.setImageResource(R.drawable.transaction_icon_black)
                    projectInternalSiteTabIcon.setImageResource(R.drawable.site_project_internal)
                    projectInternalAttendanceTabIcon.setImageResource(R.drawable.attendance)
                    projectInternalMaterialTabIcon.setImageResource(R.drawable.material)
                }
            }
            }

        })
        projectInternalTransactionTab.select()


    }
}