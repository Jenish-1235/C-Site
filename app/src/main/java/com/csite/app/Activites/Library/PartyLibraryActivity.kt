package com.csite.app.Activites.Library

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.LinearEasing
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.Objects.Party
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.PartyLibraryListAdapter
import com.google.android.material.tabs.TabLayout

class PartyLibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val addNewPartyButton: Button = findViewById(R.id.addNewPartyButton)
        addNewPartyButton.setOnClickListener{
            val intent = Intent(this, AddNewPartyActivity::class.java)
            startActivity(intent)
        }

    }

    fun setupPartyLibraryTabLayout(){
        val partyLibraryTabLayout: TabLayout = findViewById(R.id.partyLibraryTabLayout)
        partyLibraryTabLayout.removeAllTabs()
        partyLibraryTabLayout.addTab(partyLibraryTabLayout.newTab().setText("All Parties"))
        partyLibraryTabLayout.addTab(partyLibraryTabLayout.newTab().setText("Party Will Receive"))
        partyLibraryTabLayout.addTab(partyLibraryTabLayout.newTab().setText("Party Will Pay"))




        val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
        var partyLibraryRecyclerView: RecyclerView = findViewById(R.id.partyLibraryRecyclerView)
        firebaseOperationsForLibrary.fetchPartyFromPartyLibrary(object : FirebaseOperationsForLibrary.onPartyListReceived {
            override fun onPartyListReceived(partyList: ArrayList<Party>) {
                val partyLibraryListAdapter = PartyLibraryListAdapter(
                    partyList
                )
                partyLibraryRecyclerView.adapter = partyLibraryListAdapter
                partyLibraryRecyclerView.layoutManager =
                    LinearLayoutManager(this@PartyLibraryActivity)
                partyLibraryListAdapter.notifyDataSetChanged()
            }
        })

        partyLibraryTabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // Handle tab selection
                when (tab.position) {
                    0 ->{
                        firebaseOperationsForLibrary.fetchPartyFromPartyLibrary(object :FirebaseOperationsForLibrary.onPartyListReceived{
                            override fun onPartyListReceived(partyList: ArrayList<Party>) {
                                val partyLibraryListAdapter = PartyLibraryListAdapter(partyList)
                                partyLibraryRecyclerView.adapter = partyLibraryListAdapter
                                partyLibraryRecyclerView.layoutManager = LinearLayoutManager(this@PartyLibraryActivity)
                                partyLibraryListAdapter.notifyDataSetChanged()
                            }
                        })
                    }
                    1 -> {
                        // Handle tab 1 selection
                        firebaseOperationsForLibrary.fetchPartyFromPartyLibrary(object : FirebaseOperationsForLibrary.onPartyListReceived{
                            override fun onPartyListReceived(partyList: ArrayList<Party>) {
                                var partyWillReceiveList: ArrayList<Party> = ArrayList()
                                for (party in partyList){
                                    if (party.partyOpeningBalanceDetails.equals("Will Receive")){
                                        partyWillReceiveList.add(party)
                                    }
                                }
                                val partyLibraryListAdapter = PartyLibraryListAdapter(partyWillReceiveList)
                                partyLibraryRecyclerView.adapter = partyLibraryListAdapter
                                partyLibraryRecyclerView.layoutManager = LinearLayoutManager(this@PartyLibraryActivity)
                                partyLibraryListAdapter.notifyDataSetChanged()
                            }

                        })

                    }
                    2 -> {
                        // Handle tab 2 selection
                        val partyLibraryRecyclerView: RecyclerView = findViewById(R.id.partyLibraryRecyclerView)
                        firebaseOperationsForLibrary.fetchPartyFromPartyLibrary(object : FirebaseOperationsForLibrary.onPartyListReceived{
                            override fun onPartyListReceived(partyList: ArrayList<Party>) {
                                var partyWillPayList: ArrayList<Party> = ArrayList()
                                for (party in partyList) {
                                    if (party.partyOpeningBalanceDetails.equals("Will Pay")) {
                                        partyWillPayList.add(party)
                                    }
                                    val partyLibraryListAdapter = PartyLibraryListAdapter(
                                        partyWillPayList
                                    )
                                    partyLibraryRecyclerView.adapter = partyLibraryListAdapter
                                    partyLibraryRecyclerView.layoutManager =
                                        LinearLayoutManager(this@PartyLibraryActivity)
                                    partyLibraryListAdapter.notifyDataSetChanged()
                                }
                            }
                        })
                    }
                    else -> {
                        // Handle other tabs
                        val partyLibraryRecyclerView: RecyclerView = findViewById(R.id.partyLibraryRecyclerView)
                        Toast.makeText(this@PartyLibraryActivity , "Party Will Recieve" , Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Handle tab unselection
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
                // Handle tab reselection
            }
        })
        partyLibraryTabLayout.getTabAt(0)?.select()

    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "Resuming", Toast.LENGTH_SHORT).show()
        setupPartyLibraryTabLayout()
    }

    fun backButton(view: View) {
        finish()
    }
}