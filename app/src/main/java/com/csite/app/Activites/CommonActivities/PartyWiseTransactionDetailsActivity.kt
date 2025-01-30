package com.csite.app.Activites.CommonActivities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.FirebaseOperations.FirebaseOperationsForExternalPartyTab
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactionsTab
import com.csite.app.Objects.CommonTransaction
import com.csite.app.Objects.Party
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.TransactionListAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab

class PartyWiseTransactionDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_wise_transaction_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val partyPaymentDetailsActivity = intent
        val partyId = partyPaymentDetailsActivity.getStringExtra("partyId")

        val sharedPreferences = getSharedPreferences("projectId", MODE_PRIVATE)
        val projectId = sharedPreferences.getString("projectId", "")
        Toast.makeText(this, projectId, Toast.LENGTH_SHORT).show()


        val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
        firebaseOperationsForLibrary.fetchPartyFromPartyLibrary(object :FirebaseOperationsForLibrary.onPartyListReceived{
            override fun onPartyListReceived(partyList: ArrayList<Party>) {
                for(party in partyList){
                    if(party.partyId.equals(partyId)){

                        if(projectId != "") {
                            val partyNameTextView = findViewById<TextView>(R.id.partyNameTextView)
                            partyNameTextView.text = "Name: " + party.partyName
                            if (projectId != null) {
                                transactionTabLayout(projectId, partyId, party.partyName)
                            }
                        }else{
                            val partyNameTextView = findViewById<TextView>(R.id.partyNameTextView)
                            partyNameTextView.text = "Name: " + party.partyName
                            val partyTotalAmountTextView = findViewById<TextView>(R.id.partyTotalAmountTextView)
                            partyTotalAmountTextView.text = "Total Amount: " + party.partyAmountToPayOrReceive.toString() + "\u20b9"
                            val partyStatusTextView = findViewById<TextView>(R.id.partyStatusTextView)
                            partyStatusTextView.text = "Status: " + party.partyOpeningBalanceDetails
                            partyTotalAmountTextView.visibility = View.VISIBLE
                            partyStatusTextView.visibility = View.VISIBLE
                            transactionTabLayout("", partyId, party.partyName)
                        }

                    }
                }
            }
        })


    }

    fun backButton(view:View){
        finish()
    }

    fun transactionTabLayout(projectId:String, partyId:String, partyName:String){
        val transactionTabLayout: TabLayout = findViewById(R.id.projectTransactionFilterTabLayout)

        val allTransactionTab = transactionTabLayout.newTab()
        val paymentInTab = transactionTabLayout.newTab()
        val paymentOutTab = transactionTabLayout.newTab()
        val salesInvoiceTab = transactionTabLayout.newTab()
        val materialPurchaseTab = transactionTabLayout.newTab()
        val otherExpenseTab = transactionTabLayout.newTab()

        allTransactionTab.setText("All Transactions")
        paymentInTab.setText("Payment In")
        paymentOutTab.setText("Payment Out")
        salesInvoiceTab.setText("Sales Invoice")
        materialPurchaseTab.setText("Material Purchase")
        otherExpenseTab.setText("Other Expense")

        transactionTabLayout.addTab(allTransactionTab)
        transactionTabLayout.addTab(paymentInTab)
        transactionTabLayout.addTab(paymentOutTab)
        transactionTabLayout.addTab(salesInvoiceTab)
        transactionTabLayout.addTab(materialPurchaseTab)
        transactionTabLayout.addTab(otherExpenseTab)


        if (projectId != "") {
            val tabLayoutAddOnTabSelectedListener = object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: Tab?) {
                    val position = tab?.position

                    val transactionRecyclerView =
                        findViewById<RecyclerView>(R.id.projectTransactionRecyclerView)
                    transactionRecyclerView.layoutManager =
                        LinearLayoutManager(this@PartyWiseTransactionDetailsActivity)
                    var filteredTransactions = mutableListOf<CommonTransaction>()
                    val firebaseOperationsForProjectInternalTransactionsTab =
                        FirebaseOperationsForProjectInternalTransactionsTab()
                    when (position) {
                        0 -> {
                            firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(
                                projectId,
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched {
                                    override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                        var finalTransaction = mutableListOf<CommonTransaction>();
                                        for(transaction in transactions){
                                            if(transaction.transactionParty == partyName){
                                                finalTransaction.add(transaction)
                                            }
                                        }
                                        val transactionListAdapter =
                                            TransactionListAdapter(finalTransaction, projectId)
                                        transactionRecyclerView.adapter = transactionListAdapter
                                        transactionListAdapter.notifyDataSetChanged()
                                    }
                                },
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated {
                                    override fun onCalculated(calculations: ArrayList<String>) {
                                    }

                                })
                        }

                        1 -> {
                            filteredTransactions.clear()
                            firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(
                                projectId,
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched {
                                    override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                        for (transaction in transactions) {
                                            if (transaction.transactionType.equals("Payment In") && transaction.transactionParty.equals(partyName)) {
                                                filteredTransactions.add(transaction)
                                            }
                                        }
                                        val transactionListAdapter =
                                            TransactionListAdapter(filteredTransactions, projectId)
                                        transactionRecyclerView.adapter = transactionListAdapter
                                        transactionListAdapter.notifyDataSetChanged()
                                    }
                                },
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated {
                                    override fun onCalculated(calculations: ArrayList<String>) {
                                    }

                                })
                        }

                        2 -> {
                            filteredTransactions.clear()
                            firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(
                                projectId,
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched {
                                    override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                        for (transaction in transactions) {
                                            if (transaction.transactionType.equals("Payment Out") && transaction.transactionParty.equals(partyName)) {
                                                filteredTransactions.add(transaction)
                                            }
                                        }
                                        val transactionListAdapter =
                                            TransactionListAdapter(filteredTransactions, projectId)
                                        transactionRecyclerView.adapter = transactionListAdapter
                                        transactionListAdapter.notifyDataSetChanged()
                                    }
                                },
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated {
                                    override fun onCalculated(calculations: ArrayList<String>) {
                                    }

                                })
                        }

                        3 -> {
                            filteredTransactions.clear()
                            firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(
                                projectId,
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched {
                                    override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                        for (transaction in transactions) {
                                            if (transaction.transactionType.equals("Sales Invoice") && transaction.transactionParty.equals(partyName)) {
                                                filteredTransactions.add(transaction)
                                            }
                                        }
                                        val transactionListAdapter =
                                            TransactionListAdapter(filteredTransactions, projectId)
                                        transactionRecyclerView.adapter = transactionListAdapter
                                        transactionListAdapter.notifyDataSetChanged()
                                    }
                                },
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated {
                                    override fun onCalculated(calculations: ArrayList<String>) {
                                    }

                                })
                        }

                        4 -> {
                            filteredTransactions.clear()
                            firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(
                                projectId,
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched {
                                    override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                        for (transaction in transactions) {
                                            if (transaction.transactionType.equals("Material Purchase") && transaction.transactionParty.equals(partyName)) {
                                                filteredTransactions.add(transaction)
                                            }
                                        }
                                        val transactionListAdapter =
                                            TransactionListAdapter(filteredTransactions, projectId)
                                        transactionRecyclerView.adapter = transactionListAdapter
                                        transactionListAdapter.notifyDataSetChanged()
                                    }
                                },
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated {
                                    override fun onCalculated(calculations: ArrayList<String>) {
                                    }

                                })
                        }

                        5 -> {
                            filteredTransactions.clear()
                            firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(
                                projectId,
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched {
                                    override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                        for (transaction in transactions) {
                                            if (transaction.transactionType.equals("Other Expense") && transaction.transactionParty.equals(partyName)) {
                                                filteredTransactions.add(transaction)
                                            }
                                        }
                                        val transactionListAdapter =
                                            TransactionListAdapter(filteredTransactions, projectId)
                                        transactionRecyclerView.adapter = transactionListAdapter
                                        transactionListAdapter.notifyDataSetChanged()
                                    }
                                },
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated {
                                    override fun onCalculated(calculations: ArrayList<String>) {
                                    }

                                })
                        }
                    }

                }

                override fun onTabUnselected(tab: Tab?) {
                }

                override fun onTabReselected(tab: Tab?) {
                    val position = tab?.position

                    val transactionRecyclerView =
                        findViewById<RecyclerView>(R.id.projectTransactionRecyclerView)
                    transactionRecyclerView.layoutManager =
                        LinearLayoutManager(this@PartyWiseTransactionDetailsActivity)
                    var filteredTransactions = mutableListOf<CommonTransaction>()
                    val firebaseOperationsForProjectInternalTransactionsTab =
                        FirebaseOperationsForProjectInternalTransactionsTab()
                    when (position) {
                        0 -> {
                            firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(
                                projectId,
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched {
                                    override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                        var finalTransaction = mutableListOf<CommonTransaction>();
                                        for(transaction in transactions){
                                            if(transaction.transactionParty == partyName){
                                                finalTransaction.add(transaction)
                                            }
                                        }
                                        val transactionListAdapter = TransactionListAdapter(finalTransaction, projectId)
                                        transactionRecyclerView.adapter = transactionListAdapter
                                        transactionListAdapter.notifyDataSetChanged()
                                    }
                                },
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated {
                                    override fun onCalculated(calculations: ArrayList<String>) {
                                    }

                                })
                        }

                        1 -> {
                            filteredTransactions.clear()
                            firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(
                                projectId,
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched {
                                    override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                        for (transaction in transactions) {
                                            if (transaction.transactionType.equals("Payment In") && transaction.transactionParty.equals(partyName)) {
                                                filteredTransactions.add(transaction)
                                            }
                                        }
                                        val transactionListAdapter =
                                            TransactionListAdapter(filteredTransactions, projectId)
                                        transactionRecyclerView.adapter = transactionListAdapter
                                        transactionListAdapter.notifyDataSetChanged()
                                    }
                                },
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated {
                                    override fun onCalculated(calculations: ArrayList<String>) {
                                    }

                                })
                        }

                        2 -> {
                            filteredTransactions.clear()
                            firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(
                                projectId,
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched {
                                    override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                        for (transaction in transactions) {
                                            if (transaction.transactionType.equals("Payment Out") && transaction.transactionParty.equals(partyName)) {
                                                filteredTransactions.add(transaction)
                                            }
                                        }
                                        val transactionListAdapter =
                                            TransactionListAdapter(filteredTransactions, projectId)
                                        transactionRecyclerView.adapter = transactionListAdapter
                                        transactionListAdapter.notifyDataSetChanged()
                                    }
                                },
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated {
                                    override fun onCalculated(calculations: ArrayList<String>) {
                                    }

                                })
                        }

                        3 -> {
                            filteredTransactions.clear()
                            firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(
                                projectId,
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched {
                                    override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                        for (transaction in transactions) {
                                            if (transaction.transactionType.equals("Sales Invoice") && transaction.transactionParty.equals(partyName)) {
                                                filteredTransactions.add(transaction)
                                            }
                                        }
                                        val transactionListAdapter =
                                            TransactionListAdapter(filteredTransactions, projectId)
                                        transactionRecyclerView.adapter = transactionListAdapter
                                        transactionListAdapter.notifyDataSetChanged()
                                    }
                                },
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated {
                                    override fun onCalculated(calculations: ArrayList<String>) {
                                    }

                                })
                        }

                        4 -> {
                            filteredTransactions.clear()
                            firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(
                                projectId,
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched {
                                    override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                        for (transaction in transactions) {
                                            if (transaction.transactionType.equals("Material Purchase") && transaction.transactionParty.equals(partyName)) {
                                                filteredTransactions.add(transaction)
                                            }
                                        }
                                        val transactionListAdapter =
                                            TransactionListAdapter(filteredTransactions, projectId)
                                        transactionRecyclerView.adapter = transactionListAdapter
                                        transactionListAdapter.notifyDataSetChanged()
                                    }
                                },
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated {
                                    override fun onCalculated(calculations: ArrayList<String>) {
                                    }

                                })
                        }

                        5 -> {
                            filteredTransactions.clear()
                            firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(
                                projectId,
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched {
                                    override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                        for (transaction in transactions) {
                                            if (transaction.transactionType.equals("Other Expense") && transaction.transactionParty.equals(partyName)) {
                                                filteredTransactions.add(transaction)
                                            }
                                        }
                                        val transactionListAdapter =
                                            TransactionListAdapter(filteredTransactions, projectId)
                                        transactionRecyclerView.adapter = transactionListAdapter
                                        transactionListAdapter.notifyDataSetChanged()
                                    }
                                },
                                object :
                                    FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated {
                                    override fun onCalculated(calculations: ArrayList<String>) {
                                    }

                                })
                        }
                    }

                }

            }
            transactionTabLayout.addOnTabSelectedListener(tabLayoutAddOnTabSelectedListener)
            transactionTabLayout.selectTab(allTransactionTab)
        }else{
            val tabLayoutAddOnTabSelectedListener = object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: Tab?) {
                    val position = tab?.position


                    val transactionRecyclerView =
                        findViewById<RecyclerView>(R.id.projectTransactionRecyclerView)
                    transactionRecyclerView.layoutManager =
                        LinearLayoutManager(this@PartyWiseTransactionDetailsActivity)
                    var filteredTransactions = mutableListOf<CommonTransaction>()

                    if(position == 0) {
                        val firebaseOperationsForExternalPartyTab =
                            FirebaseOperationsForExternalPartyTab()
                        firebaseOperationsForExternalPartyTab.fetchAllTransactionsForSpecificParty(
                            partyId,
                            object :
                                FirebaseOperationsForExternalPartyTab.partyWiseTransactionFetched {
                                override fun partyWiseTransactionFetched(transactions: MutableList<CommonTransaction>) {
                                    val transactionListAdapter =
                                        TransactionListAdapter(transactions, projectId)
                                    transactionRecyclerView.adapter = transactionListAdapter
                                    transactionRecyclerView.layoutManager =
                                        LinearLayoutManager(this@PartyWiseTransactionDetailsActivity)
                                    transactionListAdapter.notifyDataSetChanged()
                                }

                            })
                    }else if(position == 1){
                        filteredTransactions.clear()
                        val firebaseOperationsForExternalPartyTab = FirebaseOperationsForExternalPartyTab()
                        firebaseOperationsForExternalPartyTab.fetchAllTransactionsForSpecificParty(partyId, object : FirebaseOperationsForExternalPartyTab.partyWiseTransactionFetched {
                            override fun partyWiseTransactionFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions) {
                                    if (transaction.transactionType.equals("Payment In")) {
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter =
                                    TransactionListAdapter(filteredTransactions, projectId)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        })


                    }else if(position == 2){
                        filteredTransactions.clear()
                        val firebaseOperationsForExternalPartyTab = FirebaseOperationsForExternalPartyTab()
                        firebaseOperationsForExternalPartyTab.fetchAllTransactionsForSpecificParty(partyId, object : FirebaseOperationsForExternalPartyTab.partyWiseTransactionFetched {
                            override fun partyWiseTransactionFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions) {
                                    if (transaction.transactionType.equals("Payment Out")) {
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter =
                                    TransactionListAdapter(filteredTransactions, projectId)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        })

                    }else if (position == 3){
                        filteredTransactions.clear()
                        val firebaseOperationsForExternalPartyTab = FirebaseOperationsForExternalPartyTab()
                        firebaseOperationsForExternalPartyTab.fetchAllTransactionsForSpecificParty(partyId, object : FirebaseOperationsForExternalPartyTab.partyWiseTransactionFetched {
                            override fun partyWiseTransactionFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions) {
                                    if (transaction.transactionType.equals("Sales Invoice")) {
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter =
                                    TransactionListAdapter(filteredTransactions, projectId)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        })


                    }else if (position == 4){
                        filteredTransactions.clear()
                        val firebaseOperationsForExternalPartyTab = FirebaseOperationsForExternalPartyTab()
                        firebaseOperationsForExternalPartyTab.fetchAllTransactionsForSpecificParty(partyId, object : FirebaseOperationsForExternalPartyTab.partyWiseTransactionFetched {
                            override fun partyWiseTransactionFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions) {
                                    if (transaction.transactionType.equals("Material Purchase")) {
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter =
                                    TransactionListAdapter(filteredTransactions, projectId)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        })

                    }else if (position == 5){
                        filteredTransactions.clear()
                        val firebaseOperationsForExternalPartyTab = FirebaseOperationsForExternalPartyTab()
                        firebaseOperationsForExternalPartyTab.fetchAllTransactionsForSpecificParty(partyId, object : FirebaseOperationsForExternalPartyTab.partyWiseTransactionFetched {
                            override fun partyWiseTransactionFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions) {
                                    if (transaction.transactionType.equals("Other Expense")) {
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter =
                                    TransactionListAdapter(filteredTransactions, projectId)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        })
                    }

                }

                override fun onTabUnselected(tab: Tab?) {
                }

                override fun onTabReselected(tab: Tab?) {
                    val position = tab?.position


                    val transactionRecyclerView =
                        findViewById<RecyclerView>(R.id.projectTransactionRecyclerView)
                    transactionRecyclerView.layoutManager =
                        LinearLayoutManager(this@PartyWiseTransactionDetailsActivity)
                    var filteredTransactions = mutableListOf<CommonTransaction>()

                    if(position == 0) {
                        val firebaseOperationsForExternalPartyTab =
                            FirebaseOperationsForExternalPartyTab()
                        firebaseOperationsForExternalPartyTab.fetchAllTransactionsForSpecificParty(
                            partyId,
                            object :
                                FirebaseOperationsForExternalPartyTab.partyWiseTransactionFetched {
                                override fun partyWiseTransactionFetched(transactions: MutableList<CommonTransaction>) {
                                    val transactionListAdapter =
                                        TransactionListAdapter(transactions, projectId)
                                    transactionRecyclerView.adapter = transactionListAdapter
                                    transactionRecyclerView.layoutManager =
                                        LinearLayoutManager(this@PartyWiseTransactionDetailsActivity)
                                    transactionListAdapter.notifyDataSetChanged()
                                }

                            })
                    }else if(position == 1){
                        filteredTransactions.clear()
                        val firebaseOperationsForExternalPartyTab = FirebaseOperationsForExternalPartyTab()
                        firebaseOperationsForExternalPartyTab.fetchAllTransactionsForSpecificParty(partyId, object : FirebaseOperationsForExternalPartyTab.partyWiseTransactionFetched {
                            override fun partyWiseTransactionFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions) {
                                    if (transaction.transactionType.equals("Payment In")) {
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter =
                                    TransactionListAdapter(filteredTransactions, projectId)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        })


                    }else if(position == 2){
                        filteredTransactions.clear()
                        val firebaseOperationsForExternalPartyTab = FirebaseOperationsForExternalPartyTab()
                        firebaseOperationsForExternalPartyTab.fetchAllTransactionsForSpecificParty(partyId, object : FirebaseOperationsForExternalPartyTab.partyWiseTransactionFetched {
                            override fun partyWiseTransactionFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions) {
                                    if (transaction.transactionType.equals("Payment Out")) {
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter =
                                    TransactionListAdapter(filteredTransactions, projectId)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        })

                    }else if (position == 3){
                        filteredTransactions.clear()
                        val firebaseOperationsForExternalPartyTab = FirebaseOperationsForExternalPartyTab()
                        firebaseOperationsForExternalPartyTab.fetchAllTransactionsForSpecificParty(partyId, object : FirebaseOperationsForExternalPartyTab.partyWiseTransactionFetched {
                            override fun partyWiseTransactionFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions) {
                                    if (transaction.transactionType.equals("Sales Invoice")) {
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter =
                                    TransactionListAdapter(filteredTransactions, projectId)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        })


                    }else if (position == 4){
                        filteredTransactions.clear()
                        val firebaseOperationsForExternalPartyTab = FirebaseOperationsForExternalPartyTab()
                        firebaseOperationsForExternalPartyTab.fetchAllTransactionsForSpecificParty(partyId, object : FirebaseOperationsForExternalPartyTab.partyWiseTransactionFetched {
                            override fun partyWiseTransactionFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions) {
                                    if (transaction.transactionType.equals("Material Purchase")) {
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter =
                                    TransactionListAdapter(filteredTransactions, projectId)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        })

                    }else if (position == 5){
                        filteredTransactions.clear()
                        val firebaseOperationsForExternalPartyTab = FirebaseOperationsForExternalPartyTab()
                        firebaseOperationsForExternalPartyTab.fetchAllTransactionsForSpecificParty(partyId, object : FirebaseOperationsForExternalPartyTab.partyWiseTransactionFetched {
                            override fun partyWiseTransactionFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions) {
                                    if (transaction.transactionType.equals("Other Expense")) {
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter =
                                    TransactionListAdapter(filteredTransactions, projectId)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        })
                    }
                }

            }
            transactionTabLayout.addOnTabSelectedListener(tabLayoutAddOnTabSelectedListener)
            transactionTabLayout.selectTab(allTransactionTab)
        }


    }
}