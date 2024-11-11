package com.csite.app.Fragments.ProjectInternal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewIPaidTransactionActivity
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewIReceivedTransactionActivity
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewPaymentInTransactionActivity
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewPaymentOutTransactionActivity
import com.csite.app.DialogFragments.MoreTransactionDialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactionsTab
import com.csite.app.Objects.CommonTransaction
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.TransactionListAdapter
import com.csite.app.databinding.FragmentProjectInternalTransactionBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab

class ProjectInternalTransactionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_project_internal_transaction, container, false)

        val binding = FragmentProjectInternalTransactionBinding.bind(view)

        val bundle = getArguments()
        var projectId = bundle?.getString("projectId")
        val memberAccess = bundle?.getString("memberAccess")

        if (!memberAccess.equals("manager")){
            binding.projectPaymentInButton.setOnClickListener{
                val newPaymentInTransactionIntent = Intent(activity, NewPaymentInTransactionActivity::class.java)
                newPaymentInTransactionIntent.putExtra("projectId", projectId)
                startActivity(newPaymentInTransactionIntent)
            }
            binding.projectPaymentOutButton.setOnClickListener{
                val newPaymentOutTransactionIntent = Intent(activity, NewPaymentOutTransactionActivity::class.java)
                newPaymentOutTransactionIntent.putExtra("projectId", projectId)
                startActivity(newPaymentOutTransactionIntent)
            }

            binding.projectMoreTransactionButton.setOnClickListener{
                val moreTransactionDialogFragment = MoreTransactionDialogFragment()
                val bundle = Bundle()
                bundle.putString("projectId", projectId)
                moreTransactionDialogFragment.arguments = bundle
                moreTransactionDialogFragment.show(childFragmentManager, "moreTransactionDialogFragment")
            }

            transactionTabLayout(view, projectId!!)

        }else{
            // Handle Manager Role
            binding.projectMoreTransactionButton.visibility = View.GONE
            binding.projectTransactionFilterTabLayout.visibility = View.VISIBLE

            binding.projectPaymentInButton.text = "I paid"
            binding.projectPaymentOutButton.text = "I Received"

            transactionTabLayout(view, projectId!!)
            binding.projectPaymentInButton.setOnClickListener{
                val newIpaidTransactionIntent = Intent(activity, NewIPaidTransactionActivity::class.java)
                newIpaidTransactionIntent.putExtra("projectId", projectId)
                startActivity(newIpaidTransactionIntent)
            }

            binding.projectPaymentOutButton.setOnClickListener{
                val newIReceivedTransactionIntent = Intent(activity, NewIReceivedTransactionActivity::class.java)
                newIReceivedTransactionIntent.putExtra("projectId", projectId)
                startActivity(newIReceivedTransactionIntent)
            }


        }


        val firebaseOperationsForProjectInternalTransactionsTab = FirebaseOperationsForProjectInternalTransactionsTab()
        if (projectId != null) {
            firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched{
                override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                }
            }, object : FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated{
                override fun onCalculated(calculations: ArrayList<String>) {
                    binding.projectBalanceView.text = "\u20b9" +  calculations.get(0)
                    binding.projectTotalExpenseView.text = "\u20b9" + calculations.get(3)
                    binding.projectTotalOutView.text = "\u20b9" + calculations.get(2)
                    binding.projectTotalInView.text = "\u20b9" + calculations.get(1)
                }

            })
        }

        return view
    }


    fun transactionTabLayout(view:View, projectId:String){
        val transactionTabLayout:TabLayout = view.findViewById(R.id.projectTransactionFilterTabLayout)

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


        val tabLayoutAddOnTabSelectedListener = object : TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: Tab?) {
                val position = tab?.position

                val transactionRecyclerView = view.findViewById<RecyclerView>(R.id.projectTransactionRecyclerView)
                transactionRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                var filteredTransactions = mutableListOf<CommonTransaction>()
                val firebaseOperationsForProjectInternalTransactionsTab = FirebaseOperationsForProjectInternalTransactionsTab()
                when(position){
                    0 -> {
                        firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched{
                            override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                val transactionListAdapter = TransactionListAdapter(transactions)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        }, object : FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated{
                            override fun onCalculated(calculations: ArrayList<String>) {
                            }

                        })
                    }
                    1-> {
                        filteredTransactions.clear()
                        firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched{
                            override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions){
                                    if(transaction.transactionType.equals("Payment In")){
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter = TransactionListAdapter(filteredTransactions)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        }, object : FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated{
                            override fun onCalculated(calculations: ArrayList<String>) {
                            }

                        })
                    }
                    2-> {
                        filteredTransactions.clear()
                        Toast.makeText(requireActivity(), "Payment Out", Toast.LENGTH_SHORT).show()
                        firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched{
                            override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions){
                                    if(transaction.transactionType.equals("Payment Out")){
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter = TransactionListAdapter(filteredTransactions)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        }, object : FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated{
                            override fun onCalculated(calculations: ArrayList<String>) {
                            }

                        })
                    }
                    3->{
                        filteredTransactions.clear()
                        firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched{
                            override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions){
                                    if(transaction.transactionType.equals("Sales Invoice")){
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter = TransactionListAdapter(filteredTransactions)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        }, object : FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated{
                            override fun onCalculated(calculations: ArrayList<String>) {
                            }

                        })
                    }4->{
                        filteredTransactions.clear()
                    firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched{
                        override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                            for (transaction in transactions){
                                if(transaction.transactionType.equals("Material Purchase")){
                                    filteredTransactions.add(transaction)
                                }
                            }
                            val transactionListAdapter = TransactionListAdapter(filteredTransactions)
                            transactionRecyclerView.adapter = transactionListAdapter
                            transactionListAdapter.notifyDataSetChanged()
                        }
                    }, object : FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated{
                        override fun onCalculated(calculations: ArrayList<String>) {
                        }

                    })
                    }5-> {
                        filteredTransactions.clear()
                    firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched{
                        override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                            for (transaction in transactions){
                                if(transaction.transactionType.equals("Other Expense")){
                                    filteredTransactions.add(transaction)
                                }
                            }
                            val transactionListAdapter = TransactionListAdapter(filteredTransactions)
                            transactionRecyclerView.adapter = transactionListAdapter
                            transactionListAdapter.notifyDataSetChanged()
                        }
                    }, object : FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated{
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

                val transactionRecyclerView = view.findViewById<RecyclerView>(R.id.projectTransactionRecyclerView)
                transactionRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                var filteredTransactions = mutableListOf<CommonTransaction>()
                val firebaseOperationsForProjectInternalTransactionsTab = FirebaseOperationsForProjectInternalTransactionsTab()
                when(position){
                    0 -> {
                        firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched{
                            override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                val transactionListAdapter = TransactionListAdapter(transactions)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        }, object : FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated{
                            override fun onCalculated(calculations: ArrayList<String>) {
                            }

                        })
                    }
                    1-> {
                        filteredTransactions.clear()
                        firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched{
                            override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions){
                                    if(transaction.transactionType.equals("Payment In")){
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter = TransactionListAdapter(filteredTransactions)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        }, object : FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated{
                            override fun onCalculated(calculations: ArrayList<String>) {
                            }

                        })
                    }
                    2-> {
                        filteredTransactions.clear()
                        Toast.makeText(requireActivity(), "Payment Out", Toast.LENGTH_SHORT).show()
                        firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched{
                            override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions){
                                    if(transaction.transactionType.equals("Payment Out")){
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter = TransactionListAdapter(filteredTransactions)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        }, object : FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated{
                            override fun onCalculated(calculations: ArrayList<String>) {
                            }

                        })
                    }
                    3->{
                        filteredTransactions.clear()
                        firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched{
                            override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                for (transaction in transactions){
                                    if(transaction.transactionType.equals("Sales Invoice")){
                                        filteredTransactions.add(transaction)
                                    }
                                }
                                val transactionListAdapter = TransactionListAdapter(filteredTransactions)
                                transactionRecyclerView.adapter = transactionListAdapter
                                transactionListAdapter.notifyDataSetChanged()
                            }
                        },object : FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated{
                            override fun onCalculated(calculations: ArrayList<String>) {
                            }

                        })
                    }4->{
                    filteredTransactions.clear()
                    firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched{
                        override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                            for (transaction in transactions){
                                if(transaction.transactionType.equals("Material Purchase")){
                                    filteredTransactions.add(transaction)
                                }
                            }
                            val transactionListAdapter = TransactionListAdapter(filteredTransactions)
                            transactionRecyclerView.adapter = transactionListAdapter
                            transactionListAdapter.notifyDataSetChanged()
                        }
                    }, object : FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated{
                        override fun onCalculated(calculations: ArrayList<String>) {
                        }

                    })
                }5-> {
                    filteredTransactions.clear()
                    firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched{
                        override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                            for (transaction in transactions){
                                if(transaction.transactionType.equals("Other Expense")){
                                    filteredTransactions.add(transaction)
                                }
                            }
                            val transactionListAdapter = TransactionListAdapter(filteredTransactions)
                            transactionRecyclerView.adapter = transactionListAdapter
                            transactionListAdapter.notifyDataSetChanged()
                        }
                    },object : FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated{
                        override fun onCalculated(calculations: ArrayList<String>) {
                        }

                    })
                }
                }

            }

        }

        transactionTabLayout.addOnTabSelectedListener(tabLayoutAddOnTabSelectedListener)
        transactionTabLayout.selectTab(allTransactionTab)

    }

}